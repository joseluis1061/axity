import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { IConciliacion } from '../../../../core/interfaces/conciliaciones.interface';
import { ConciliacionesService } from '../../../../core/services/conciliaciones/conciliaciones.service';
import { finalize } from 'rxjs';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-reporte-conciliaciones-descuadradas',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './reporte-conciliaciones-descuadradas.component.html',
  styleUrls: ['./reporte-conciliaciones-descuadradas.component.scss']
})
export class ReporteConciliacionesDescuadradasComponent implements OnInit {
  private conciliacionesService = inject(ConciliacionesService);

  // Datos del reporte
  conciliacionesDescuadradas: IConciliacion[] = [];
  loading = false;
  error = '';

  // Filtros de fecha
  fechaInicio: string = '';
  fechaFin: string = '';

  // Agrupación y totales
  conciliacionesPorSucursal: Map<string, IConciliacion[]> = new Map();
  conciliacionesPorProducto: Map<string, IConciliacion[]> = new Map();
  conciliacionesPorMes: Map<string, IConciliacion[]> = new Map();

  // Estadísticas
  totalDescuadradas = 0;
  totalDiferenciaFisica = 0;
  totalDiferenciaValor = 0;

  // Flag para mostrar/ocultar secciones
  showPorSucursal = true;
  showPorProducto = true;
  showPorMes = true;

  ngOnInit(): void {
    this.establecerFechasDefecto();
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

  loadConciliacionesDescuadradas(): void {
    if (!this.fechaInicio || !this.fechaFin) {
      this.error = 'Por favor, seleccione ambas fechas para generar el reporte.';
      return;
    }

    this.loading = true;
    this.error = '';

    // Como no hay un endpoint específico para rango de fechas, hacemos una solicitud por cada día
    const fechaInicio = new Date(this.fechaInicio);
    const fechaFin = new Date(this.fechaFin);

    // Si la fecha de inicio es posterior a la fecha fin, mostrar error
    if (fechaInicio > fechaFin) {
      this.error = 'La fecha de inicio debe ser anterior o igual a la fecha fin.';
      this.loading = false;
      return;
    }

    // Cargar las conciliaciones descuadradas para la fecha de inicio
    this.conciliacionesService.getDescuadradasPorFecha(this.fechaInicio)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => {
          this.conciliacionesDescuadradas = data;
          this.procesarResultados();
        },
        error: (err) => {
          console.error('Error al cargar conciliaciones descuadradas', err);
          this.error = 'Error al cargar las conciliaciones descuadradas. Por favor, intente nuevamente.';
        }
      });
  }

  procesarResultados(): void {
    // Inicializar contadores y agrupaciones
    this.conciliacionesPorSucursal.clear();
    this.conciliacionesPorProducto.clear();
    this.conciliacionesPorMes.clear();

    this.totalDescuadradas = this.conciliacionesDescuadradas.length;
    this.totalDiferenciaFisica = 0;
    this.totalDiferenciaValor = 0;

    // Agrupar y calcular totales
    this.conciliacionesDescuadradas.forEach(conciliacion => {
      // Acumular totales generales
      this.totalDiferenciaFisica += conciliacion.diferenciaFisica;
      this.totalDiferenciaValor += conciliacion.diferenciaValor;

      // Agrupar por sucursal
      const nombreSucursal = conciliacion.sucursalProducto.sucursal.nombreSucursal;
      if (!this.conciliacionesPorSucursal.has(nombreSucursal)) {
        this.conciliacionesPorSucursal.set(nombreSucursal, []);
      }
      this.conciliacionesPorSucursal.get(nombreSucursal)?.push(conciliacion);

      // Agrupar por producto
      const nombreProducto = conciliacion.sucursalProducto.producto.nombreProducto;
      if (!this.conciliacionesPorProducto.has(nombreProducto)) {
        this.conciliacionesPorProducto.set(nombreProducto, []);
      }
      this.conciliacionesPorProducto.get(nombreProducto)?.push(conciliacion);

      // Agrupar por mes
      const fecha = new Date(conciliacion.fechaConciliacion);
      const mes = `${fecha.getFullYear()}-${String(fecha.getMonth() + 1).padStart(2, '0')}`;
      if (!this.conciliacionesPorMes.has(mes)) {
        this.conciliacionesPorMes.set(mes, []);
      }
      this.conciliacionesPorMes.get(mes)?.push(conciliacion);
    });
  }

  generarReporte(): void {
    this.loadConciliacionesDescuadradas();
  }

  exportarExcel(): void {
    // Preparar los datos para Excel
    const datosParaExcel: any[] = [];

    // Añadir encabezado
    datosParaExcel.push({
      'Reporte': 'Conciliaciones Descuadradas',
      'Periodo': `${this.formatDateForDisplay(this.fechaInicio)} al ${this.formatDateForDisplay(this.fechaFin)}`,
      'Total': this.totalDescuadradas,
      'Diferencia Física Total': this.totalDiferenciaFisica,
      'Diferencia Valor Total': this.totalDiferenciaValor
    });

    // Fila vacía
    datosParaExcel.push({});

    // Añadir conciliaciones con todos los detalles
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

    this.conciliacionesDescuadradas.forEach(c => {
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

    // Fila vacía
    datosParaExcel.push({});
    datosParaExcel.push({});

    // Añadir resumen por sucursal
    datosParaExcel.push({
      'Resumen por Sucursal': 'Resumen por Sucursal',
      'Cantidad': 'Cantidad',
      'Diferencia Física': 'Diferencia Física',
      'Diferencia Valor': 'Diferencia Valor'
    });

    Array.from(this.conciliacionesPorSucursal.entries()).forEach(([sucursal, conciliaciones]) => {
      const totalFisica = conciliaciones.reduce((sum, c) => sum + c.diferenciaFisica, 0);
      const totalValor = conciliaciones.reduce((sum, c) => sum + c.diferenciaValor, 0);

      datosParaExcel.push({
        'Resumen por Sucursal': sucursal,
        'Cantidad': conciliaciones.length,
        'Diferencia Física': totalFisica,
        'Diferencia Valor': totalValor
      });
    });

    // Fila vacía
    datosParaExcel.push({});
    datosParaExcel.push({});

    // Añadir resumen por producto
    datosParaExcel.push({
      'Resumen por Producto': 'Resumen por Producto',
      'Cantidad': 'Cantidad',
      'Diferencia Física': 'Diferencia Física',
      'Diferencia Valor': 'Diferencia Valor'
    });

    Array.from(this.conciliacionesPorProducto.entries()).forEach(([producto, conciliaciones]) => {
      const totalFisica = conciliaciones.reduce((sum, c) => sum + c.diferenciaFisica, 0);
      const totalValor = conciliaciones.reduce((sum, c) => sum + c.diferenciaValor, 0);

      datosParaExcel.push({
        'Resumen por Producto': producto,
        'Cantidad': conciliaciones.length,
        'Diferencia Física': totalFisica,
        'Diferencia Valor': totalValor
      });
    });

    // Generar archivo Excel
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(datosParaExcel, { skipHeader: true });

    // Ajustar anchos de columna
    const columnWidths = [
      { wch: 20 }, // A
      { wch: 20 }, // B
      { wch: 30 }, // C
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
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Conciliaciones Descuadradas');

    // Guardar archivo
    const nombreArchivo = `Reporte_Conciliaciones_Descuadradas_${this.fechaInicio}_${this.fechaFin}.xlsx`;
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

  // Obtener color según diferencias
  getDiferenciaClass(valor: number): string {
    if (valor === 0) return 'text-gray-600';
    return valor > 0 ? 'text-red-600' : 'text-green-600';
  }

  // Toggling para secciones
  toggleSeccion(seccion: string): void {
    switch (seccion) {
      case 'sucursal':
        this.showPorSucursal = !this.showPorSucursal;
        break;
      case 'producto':
        this.showPorProducto = !this.showPorProducto;
        break;
      case 'mes':
        this.showPorMes = !this.showPorMes;
        break;
    }
  }

  // Helpers para templates
  get sucursales(): string[] {
    return Array.from(this.conciliacionesPorSucursal.keys());
  }

  get productos(): string[] {
    return Array.from(this.conciliacionesPorProducto.keys());
  }

  get meses(): string[] {
    return Array.from(this.conciliacionesPorMes.keys());
  }

  getConciliacionesPor(tipo: string, key: string): IConciliacion[] {
    switch (tipo) {
      case 'sucursal':
        return this.conciliacionesPorSucursal.get(key) || [];
      case 'producto':
        return this.conciliacionesPorProducto.get(key) || [];
      case 'mes':
        return this.conciliacionesPorMes.get(key) || [];
      default:
        return [];
    }
  }

  getDiferenciaFisicaPor(tipo: string, key: string): number {
    const conciliaciones = this.getConciliacionesPor(tipo, key);
    return conciliaciones.reduce((sum, c) => sum + c.diferenciaFisica, 0);
  }

  getDiferenciaValorPor(tipo: string, key: string): number {
    const conciliaciones = this.getConciliacionesPor(tipo, key);
    return conciliaciones.reduce((sum, c) => sum + c.diferenciaValor, 0);
  }

  // Formatear nombre del mes
  formatMes(mesStr: string): string {
    const [year, month] = mesStr.split('-');
    const fecha = new Date(parseInt(year), parseInt(month) - 1, 1);
    return fecha.toLocaleDateString('es-CO', { year: 'numeric', month: 'long' });
  }
}
