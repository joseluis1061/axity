import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { IConciliacion } from '../../../../core/interfaces/conciliaciones.interface';
import { ConciliacionesService } from '../../../../core/services/conciliaciones/conciliaciones.service';
import { finalize } from 'rxjs';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-reporte-conciliaciones-sucursal',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './reporte-conciliaciones-sucursal.component.html',
  styleUrl: './reporte-conciliaciones-sucursal.component.scss'
})
export class ReporteConciliacionesSucursalComponent implements OnInit {
  private conciliacionesService = inject(ConciliacionesService);

  // Datos del reporte
  conciliaciones: IConciliacion[] = [];
  loading = false;
  error = '';

  // Filtros
  fechaInicio: string = '';
  fechaFin: string = '';
  sucursalSeleccionada: string = '';

  // Agrupación y análisis
  conciliacionesPorSucursal: Map<string, IConciliacion[]> = new Map();
  conciliacionesPorEstado: Map<string, IConciliacion[]> = new Map();
  estadosPorSucursal: Map<string, Map<string, number>> = new Map();

  // Para las sucursales
  sucursales: string[] = [];
  sucursalCodesMap: Map<string, string> = new Map();

  // Estadísticas
  totalConciliaciones = 0;
  totalPorEstado: Map<string, number> = new Map();

  // Flags para secciones colapsables
  showResumenEstados = true;
  showDetalleConciliaciones = true;

  ngOnInit(): void {
    this.establecerFechasDefecto();
    this.cargarSucursales();
  }

  establecerFechasDefecto(): void {
    const hoy = new Date();

    // Fecha fin = hoy
    const yearFin = hoy.getFullYear();
    const monthFin = String(hoy.getMonth() + 1).padStart(2, '0');
    const dayFin = String(hoy.getDate()).padStart(2, '0');
    this.fechaFin = `${yearFin}-${monthFin}-${dayFin}`;

    // Fecha inicio = primer día del mes actual
    const fechaInicio = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
    const yearInicio = fechaInicio.getFullYear();
    const monthInicio = String(fechaInicio.getMonth() + 1).padStart(2, '0');
    const dayInicio = String(fechaInicio.getDate()).padStart(2, '0');
    this.fechaInicio = `${yearInicio}-${monthInicio}-${dayInicio}`;
  }

  cargarSucursales(): void {
    // obtener las sucursales desde las conciliaciones
    this.loading = true;

    this.conciliacionesService.getAll()
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => {
          // Extraer sucursales únicas
          const sucursalesSet = new Set<string>();
          this.sucursalCodesMap.clear();

          data.forEach(c => {
            const nombreSucursal = c.sucursalProducto.sucursal.nombreSucursal;
            const codigoSucursal = c.sucursalProducto.sucursal.codigoSucursal;
            sucursalesSet.add(nombreSucursal);
            this.sucursalCodesMap.set(nombreSucursal, codigoSucursal);
          });

          this.sucursales = Array.from(sucursalesSet).sort();
        },
        error: (err) => {
          console.error('Error al cargar sucursales', err);
          this.error = 'Error al cargar las sucursales. Por favor, intente nuevamente.';
        }
      });
  }

  generarReporte(): void {
    if (!this.fechaInicio || !this.fechaFin) {
      this.error = 'Por favor, seleccione ambas fechas para generar el reporte.';
      return;
    }

    // Verifica que la fecha de inicio sea anterior o igual a la fecha fin
    const fechaInicio = new Date(this.fechaInicio);
    const fechaFin = new Date(this.fechaFin);

    if (fechaInicio > fechaFin) {
      this.error = 'La fecha de inicio debe ser anterior o igual a la fecha fin.';
      return;
    }

    this.loading = true;
    this.error = '';

    // Determinar qué endpoint usar
    let fetchMethod;

    if (this.sucursalSeleccionada) {
      // Si hay una sucursal seleccionada, obtener conciliaciones para esa sucursal
      const codigoSucursal = this.sucursalCodesMap.get(this.sucursalSeleccionada) || '';
      fetchMethod = this.conciliacionesService.getBySucursal(codigoSucursal);
    } else {
      // Si no hay sucursal seleccionada, obtener todas
      fetchMethod = this.conciliacionesService.getAll();
    }

    fetchMethod
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => {
          // Filtrar por el rango de fechas en el cliente
          this.conciliaciones = this.filtrarPorFechas(data, fechaInicio, fechaFin);
          this.procesarDatos();
        },
        error: (err) => {
          console.error('Error al cargar conciliaciones', err);
          this.error = 'Error al cargar las conciliaciones. Por favor, intente nuevamente.';
        }
      });
  }

  filtrarPorFechas(conciliaciones: IConciliacion[], fechaInicio: Date, fechaFin: Date): IConciliacion[] {
    return conciliaciones.filter(c => {
      const fechaConciliacion = new Date(c.fechaConciliacion);
      // Ajustar para incluir todo el día fin
      fechaFin.setHours(23, 59, 59, 999);
      return fechaConciliacion >= fechaInicio && fechaConciliacion <= fechaFin;
    });
  }

  procesarDatos(): void {
    // Reiniciar mapas y contadores
    this.conciliacionesPorSucursal.clear();
    this.conciliacionesPorEstado.clear();
    this.estadosPorSucursal.clear();
    this.totalPorEstado.clear();

    this.totalConciliaciones = this.conciliaciones.length;

    // Agrupar por sucursal
    this.conciliaciones.forEach(conciliacion => {
      const nombreSucursal = conciliacion.sucursalProducto.sucursal.nombreSucursal;
      const estadoCodigo = conciliacion.codigoEstado;
      const estadoDescripcion = conciliacion.estadoConciliacion.descripcion;

      // Agrupar por sucursal
      if (!this.conciliacionesPorSucursal.has(nombreSucursal)) {
        this.conciliacionesPorSucursal.set(nombreSucursal, []);
        this.estadosPorSucursal.set(nombreSucursal, new Map());
      }
      this.conciliacionesPorSucursal.get(nombreSucursal)?.push(conciliacion);

      // Agrupar por estado
      if (!this.conciliacionesPorEstado.has(estadoDescripcion)) {
        this.conciliacionesPorEstado.set(estadoDescripcion, []);
      }
      this.conciliacionesPorEstado.get(estadoDescripcion)?.push(conciliacion);

      // Actualizar contador total por estado
      this.totalPorEstado.set(
        estadoDescripcion,
        (this.totalPorEstado.get(estadoDescripcion) || 0) + 1
      );

      // Actualizar contador de estados por sucursal
      const estadosSucursal = this.estadosPorSucursal.get(nombreSucursal);
      if (estadosSucursal) {
        estadosSucursal.set(
          estadoDescripcion,
          (estadosSucursal.get(estadoDescripcion) || 0) + 1
        );
      }
    });
  }

  exportarExcel(): void {
    // Preparar los datos para Excel
    const datosParaExcel: any[] = [];

    // Añadir encabezado
    datosParaExcel.push({
      'Reporte': 'Conciliaciones por Sucursal',
      'Periodo': `${this.formatDateForDisplay(this.fechaInicio)} al ${this.formatDateForDisplay(this.fechaFin)}`,
      'Total Conciliaciones': this.totalConciliaciones,
    });

    // Añadir resumen de estados
    datosParaExcel.push({});
    datosParaExcel.push({ 'Resumen por Estado': 'Resumen por Estado' });

    this.conciliacionesPorEstado.forEach((conciliaciones, estado) => {
      datosParaExcel.push({
        'Estado': estado,
        'Cantidad': conciliaciones.length,
        'Porcentaje': `${((conciliaciones.length / this.totalConciliaciones) * 100).toFixed(2)}%`
      });
    });

    // Fila vacía
    datosParaExcel.push({});
    datosParaExcel.push({});

    // Añadir resumen por sucursal
    datosParaExcel.push({ 'Resumen por Sucursal': 'Resumen por Sucursal' });
    datosParaExcel.push({
      'Sucursal': 'Sucursal',
      'Código': 'Código',
      'Total': 'Total',
      'Aceptadas': 'Aceptadas',
      'Bajo revisión': 'Bajo revisión',
      'Cuadradas': 'Cuadradas',
      'Descuadradas': 'Descuadradas'
    });

    this.conciliacionesPorSucursal.forEach((conciliaciones, sucursal) => {
      const codigoSucursal = this.sucursalCodesMap.get(sucursal) || '';
      const estadosSucursal = this.estadosPorSucursal.get(sucursal) || new Map();

      datosParaExcel.push({
        'Sucursal': sucursal,
        'Código': codigoSucursal,
        'Total': conciliaciones.length,
        'Aceptadas': estadosSucursal.get('Aceptada') || 0,
        'Bajo revisión': estadosSucursal.get('Bajo revisión') || 0,
        'Cuadradas': estadosSucursal.get('Cuadrada') || 0,
        'Descuadradas': estadosSucursal.get('Descuadrada') || 0
      });
    });

    // Fila vacía
    datosParaExcel.push({});
    datosParaExcel.push({});

    // Añadir detalle de conciliaciones
    datosParaExcel.push({ 'Detalle de Conciliaciones': 'Detalle de Conciliaciones' });
    datosParaExcel.push({
      'ID': 'ID',
      'Fecha': 'Fecha',
      'Sucursal': 'Sucursal',
      'Código Sucursal': 'Código Sucursal',
      'Producto': 'Producto',
      'Código Producto': 'Código Producto',
      'Diferencia Física': 'Diferencia Física',
      'Diferencia Valor': 'Diferencia Valor',
      'Estado': 'Estado'
    });

    this.conciliaciones.forEach(c => {
      datosParaExcel.push({
        'ID': c.idConciliacion,
        'Fecha': this.formatDateForDisplay(c.fechaConciliacion),
        'Sucursal': c.sucursalProducto.sucursal.nombreSucursal,
        'Código Sucursal': c.sucursalProducto.sucursal.codigoSucursal,
        'Producto': c.sucursalProducto.producto.nombreProducto,
        'Código Producto': c.sucursalProducto.producto.codigoProducto,
        'Diferencia Física': c.diferenciaFisica,
        'Diferencia Valor': c.diferenciaValor,
        'Estado': c.estadoConciliacion.descripcion
      });
    });

    // Generar archivo Excel
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(datosParaExcel, { skipHeader: true });

    // Ajustar anchos de columna
    const columnWidths = [
      { wch: 20 }, // A
      { wch: 20 }, // B
      { wch: 15 }, // C
      { wch: 15 }, // D
      { wch: 30 }, // E
      { wch: 15 }, // F
      { wch: 15 }, // G
      { wch: 15 }, // H
      { wch: 15 }  // I
    ];
    worksheet['!cols'] = columnWidths;

    // Crear el libro
    const workbook: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Conciliaciones por Sucursal');

    // Determinar el nombre del archivo
    let nombreArchivo;
    if (this.sucursalSeleccionada) {
      nombreArchivo = `Reporte_Sucursal_${this.sucursalSeleccionada.replace(/\s+/g, '_')}_${this.fechaInicio}_${this.fechaFin}.xlsx`;
    } else {
      nombreArchivo = `Reporte_Todas_Sucursales_${this.fechaInicio}_${this.fechaFin}.xlsx`;
    }

    // Guardar archivo
    XLSX.writeFile(workbook, nombreArchivo);
  }

  // Formatear montos
  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(amount);
  }

  // Formatear fecha para mostrar
  formatDateForDisplay(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-CO', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  // Obtener color según estado
  getEstadoColor(estado: string): string {
    switch (estado) {
      case 'Aceptada': return 'bg-green-100 text-green-800';
      case 'Bajo revisión': return 'bg-yellow-100 text-yellow-800';
      case 'Cuadrada': return 'bg-blue-100 text-blue-800';
      case 'Descuadrada': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  // Obtener color según diferencias
  getDiferenciaClass(valor: number): string {
    if (valor === 0) return 'text-gray-600';
    return valor > 0 ? 'text-red-600' : 'text-green-600';
  }

  // Toggling para secciones
  toggleSeccion(seccion: string): void {
    switch (seccion) {
      case 'resumenEstados':
        this.showResumenEstados = !this.showResumenEstados;
        break;
      case 'detalleConciliaciones':
        this.showDetalleConciliaciones = !this.showDetalleConciliaciones;
        break;
    }
  }

  // Obtener estados como array para iterar
  get estados(): string[] {
    return Array.from(this.conciliacionesPorEstado.keys());
  }

  // Obtener el porcentaje de un estado
  getPorcentajeEstado(estado: string): string {
    const cantidad = this.conciliacionesPorEstado.get(estado)?.length || 0;
    return ((cantidad / this.totalConciliaciones) * 100).toFixed(1);
  }

  // Calcular el número de conciliaciones para un estado en una sucursal
  getEstadoCountPorSucursal(sucursal: string, estado: string): number {
    return this.estadosPorSucursal.get(sucursal)?.get(estado) || 0;
  }

  // Obtener sucursales como array para iterar
  getSucursalesList(): string[] {
    if (this.sucursalSeleccionada) {
      return [this.sucursalSeleccionada];
    }
    return Array.from(this.conciliacionesPorSucursal.keys());
  }
}
