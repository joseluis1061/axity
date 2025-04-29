import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { IConciliacion } from '../../../../core/interfaces/conciliaciones.interface';
import { ConciliacionesService } from '../../../../core/services/conciliaciones/conciliaciones.service';
import { finalize } from 'rxjs';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-reporte-conciliaciones-mensual',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './reporte-conciliaciones-mensual.component.html',
  styleUrl: './reporte-conciliaciones-mensual.component.scss'
})
export class ReporteConciliacionesMensualComponent implements OnInit {
  private conciliacionesService = inject(ConciliacionesService);

  // Datos del reporte
  conciliaciones: IConciliacion[] = [];
  loading = false;
  error = '';

  // Filtros
  anioSeleccionado: number;
  mesSeleccionado: number;

  // Lista de años y meses para selección
  aniosDisponibles: number[] = [];
  mesesDisponibles = [
    { valor: 1, nombre: 'Enero' },
    { valor: 2, nombre: 'Febrero' },
    { valor: 3, nombre: 'Marzo' },
    { valor: 4, nombre: 'Abril' },
    { valor: 5, nombre: 'Mayo' },
    { valor: 6, nombre: 'Junio' },
    { valor: 7, nombre: 'Julio' },
    { valor: 8, nombre: 'Agosto' },
    { valor: 9, nombre: 'Septiembre' },
    { valor: 10, nombre: 'Octubre' },
    { valor: 11, nombre: 'Noviembre' },
    { valor: 12, nombre: 'Diciembre' }
  ];

  // Mapas para almacenar conciliaciones agrupadas
  conciliacionesPorEstado: Map<string, IConciliacion[]> = new Map();
  conciliacionesPorSucursal: Map<string, IConciliacion[]> = new Map();

  // Estadísticas
  totalConciliaciones = 0;
  totalDiferenciaFisica = 0;
  totalDiferenciaValor = 0;
  totalDescuadradas = 0;

  // Controles de visualización
  showResumenEstadistico = true;
  showDetalleConciliaciones = true;

  constructor() {
    // Inicializar con el mes y año actual
    const fechaActual = new Date();
    this.anioSeleccionado = fechaActual.getFullYear();
    this.mesSeleccionado = fechaActual.getMonth() + 1; // getMonth() devuelve 0-11

    // Generar años disponibles (desde 2020 hasta el año actual)
    const anioActual = fechaActual.getFullYear();
    for (let anio = 2020; anio <= anioActual; anio++) {
      this.aniosDisponibles.push(anio);
    }
  }

  ngOnInit(): void {
    this.generarReporte();
  }

  generarReporte(): void {
    if (!this.anioSeleccionado || !this.mesSeleccionado) {
      this.error = 'Por favor, seleccione año y mes para generar el reporte.';
      return;
    }

    this.loading = true;
    this.error = '';

    // Generar fecha para el primer día del mes
    const fecha = new Date(this.anioSeleccionado, this.mesSeleccionado - 1, 1);
    const fechaFormateada = this.formatearFecha(fecha);

    this.conciliacionesService.getByDate(fechaFormateada)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => {
          this.conciliaciones = data;
          this.procesarDatos();
        },
        error: (err) => {
          console.error('Error al cargar conciliaciones', err);
          this.error = 'Error al cargar las conciliaciones. Por favor, intente nuevamente.';
        }
      });
  }

  formatearFecha(fecha: Date): string {
    const year = fecha.getFullYear();
    const month = String(fecha.getMonth() + 1).padStart(2, '0');
    const day = String(fecha.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  procesarDatos(): void {
    // Limpiar datos previos
    this.conciliacionesPorEstado.clear();
    this.conciliacionesPorSucursal.clear();

    // Reiniciar contadores
    this.totalConciliaciones = this.conciliaciones.length;
    this.totalDiferenciaFisica = 0;
    this.totalDiferenciaValor = 0;
    this.totalDescuadradas = 0;

    // Procesar cada conciliación
    this.conciliaciones.forEach(conciliacion => {
      // Sumar totales
      this.totalDiferenciaFisica += conciliacion.diferenciaFisica;
      this.totalDiferenciaValor += conciliacion.diferenciaValor;

      // Contabilizar descuadradas
      if (conciliacion.estadoConciliacion.codigoEstado === 'D') {
        this.totalDescuadradas++;
      }

      // Agrupar por estado
      const estado = conciliacion.estadoConciliacion.descripcion;
      if (!this.conciliacionesPorEstado.has(estado)) {
        this.conciliacionesPorEstado.set(estado, []);
      }
      this.conciliacionesPorEstado.get(estado)?.push(conciliacion);

      // Agrupar por sucursal
      const sucursal = conciliacion.sucursalProducto.sucursal.nombreSucursal;
      if (!this.conciliacionesPorSucursal.has(sucursal)) {
        this.conciliacionesPorSucursal.set(sucursal, []);
      }
      this.conciliacionesPorSucursal.get(sucursal)?.push(conciliacion);
    });
  }

  exportarExcel(): void {
    // Datos para el Excel
    const datosExcel: any[] = [];

    // Encabezado
    datosExcel.push({
      'Reporte': 'Reporte Mensual de Conciliaciones',
      'Periodo': `${this.getNombreMes(this.mesSeleccionado)} ${this.anioSeleccionado}`,
      'Total': this.totalConciliaciones,
      'Descuadradas': this.totalDescuadradas
    });

    // Espacio en blanco
    datosExcel.push({});

    // Resumen de estados
    datosExcel.push({ 'Resumen por Estado': 'Resumen por Estado' });
    datosExcel.push({
      'Estado': 'Estado',
      'Cantidad': 'Cantidad',
      'Porcentaje': 'Porcentaje'
    });

    this.estados.forEach(estado => {
      const cantidad = this.conciliacionesPorEstado.get(estado)?.length || 0;
      const porcentaje = this.calcularPorcentaje(cantidad, this.totalConciliaciones);

      datosExcel.push({
        'Estado': estado,
        'Cantidad': cantidad,
        'Porcentaje': `${porcentaje}%`
      });
    });

    // Espacio en blanco
    datosExcel.push({});
    datosExcel.push({});

    // Detalle de conciliaciones
    datosExcel.push({ 'Detalle de Conciliaciones': 'Detalle de Conciliaciones' });
    datosExcel.push({
      'ID': 'ID',
      'Fecha': 'Fecha',
      'Sucursal': 'Sucursal',
      'Producto': 'Producto',
      'Diferencia Física': 'Diferencia Física',
      'Diferencia Valor': 'Diferencia Valor',
      'Estado': 'Estado'
    });

    this.conciliaciones.forEach(c => {
      datosExcel.push({
        'ID': c.idConciliacion,
        'Fecha': this.formatearFechaParaMostrar(c.fechaConciliacion),
        'Sucursal': c.sucursalProducto.sucursal.nombreSucursal,
        'Producto': c.sucursalProducto.producto.nombreProducto,
        'Diferencia Física': c.diferenciaFisica,
        'Diferencia Valor': c.diferenciaValor,
        'Estado': c.estadoConciliacion.descripcion
      });
    });

    // Crear Excel
    const worksheet = XLSX.utils.json_to_sheet(datosExcel, { skipHeader: true });
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Reporte Mensual');

    // Guardar archivo
    const nombreArchivo = `Reporte_Mensual_${this.getNombreMes(this.mesSeleccionado)}_${this.anioSeleccionado}.xlsx`;
    XLSX.writeFile(workbook, nombreArchivo);
  }

  // Métodos auxiliares
  toggleSeccion(seccion: string): void {
    if (seccion === 'resumen') {
      this.showResumenEstadistico = !this.showResumenEstadistico;
    } else if (seccion === 'detalle') {
      this.showDetalleConciliaciones = !this.showDetalleConciliaciones;
    }
  }

  getNombreMes(mes: number): string {
    return this.mesesDisponibles.find(m => m.valor === mes)?.nombre || '';
  }

  formatearFechaParaMostrar(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-CO', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  formatearMoneda(valor: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 2
    }).format(valor);
  }

  getColorEstado(estado: string): string {
    switch (estado) {
      case 'Aceptada': return 'bg-green-100 text-green-800';
      case 'Bajo revisión': return 'bg-yellow-100 text-yellow-800';
      case 'Cuadrada': return 'bg-blue-100 text-blue-800';
      case 'Descuadrada': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  getColorDiferencia(valor: number): string {
    if (valor === 0) return 'text-gray-600';
    return valor > 0 ? 'text-red-600' : 'text-green-600';
  }

  calcularPorcentaje(valor: number, total: number): string {
    if (total === 0) return '0.0';
    return ((valor / total) * 100).toFixed(1);
  }

  contarPorEstado(estado: string): number {
    return this.conciliacionesPorEstado.get(estado)?.length || 0;
  }

  contarDescuadradasPorSucursal(sucursal: string): number {
    const conciliaciones = this.conciliacionesPorSucursal.get(sucursal) || [];
    return conciliaciones.filter(c => c.estadoConciliacion.codigoEstado === 'D').length;
  }

  // Getters para el template
  get estados(): string[] {
    return Array.from(this.conciliacionesPorEstado.keys());
  }

  get sucursales(): string[] {
    return Array.from(this.conciliacionesPorSucursal.keys());
  }
}
