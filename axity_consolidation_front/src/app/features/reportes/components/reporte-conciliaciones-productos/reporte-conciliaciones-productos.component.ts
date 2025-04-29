import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { IConciliacion } from '../../../../core/interfaces/conciliaciones.interface';
import { IProducto } from '../../../../core/interfaces/conciliaciones.interface';
import { ConciliacionesService } from '../../../../core/services/conciliaciones/conciliaciones.service';
import { finalize } from 'rxjs';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-reporte-conciliaciones-productos',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './reporte-conciliaciones-productos.component.html',
  styleUrl: './reporte-conciliaciones-productos.component.scss'
})
export class ReporteConciliacionesProductosComponent implements OnInit {
  private conciliacionesService = inject(ConciliacionesService);

  // Datos del reporte
  conciliaciones: IConciliacion[] = [];
  loading = false;
  error = '';

  // Filtros
  fechaInicio: string = '';
  fechaFin: string = '';
  productoSeleccionado: string = '';

  // Productos disponibles
  productos: IProducto[] = [];
  productosUnicos: Map<string, IProducto> = new Map();

  // Agrupación y análisis
  conciliacionesPorProducto: Map<string, IConciliacion[]> = new Map();
  conciliacionesPorMes: Map<string, IConciliacion[]> = new Map();
  conciliacionesPorEstado: Map<string, IConciliacion[]> = new Map();

  // Estadísticas
  totalConciliaciones = 0;
  totalDiferenciaFisica = 0;
  totalDiferenciaValor = 0;
  totalDescuadradas = 0;

  // Controles de visualización
  showTendenciaMensual = true;
  showDistribucionEstados = true;
  showDetalleConciliaciones = true;

  ngOnInit(): void {
    this.establecerFechasPorDefecto();
    this.cargarProductos();
  }

  establecerFechasPorDefecto(): void {
    const hoy = new Date();

    // Fecha fin (hoy)
    const yearFin = hoy.getFullYear();
    const monthFin = String(hoy.getMonth() + 1).padStart(2, '0');
    const dayFin = String(hoy.getDate()).padStart(2, '0');
    this.fechaFin = `${yearFin}-${monthFin}-${dayFin}`;

    // Fecha inicio (3 meses atrás)
    const fechaInicio = new Date();
    fechaInicio.setMonth(fechaInicio.getMonth() - 3);
    const yearInicio = fechaInicio.getFullYear();
    const monthInicio = String(fechaInicio.getMonth() + 1).padStart(2, '0');
    const dayInicio = String(fechaInicio.getDate()).padStart(2, '0');
    this.fechaInicio = `${yearInicio}-${monthInicio}-${dayInicio}`;
  }

  cargarProductos(): void {
    this.loading = true;
    // Obtener productos de las conciliaciones existentes
    this.conciliacionesService.getAll()
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => {
          // Extraer productos únicos
          data.forEach(conciliacion => {
            const producto = conciliacion.sucursalProducto.producto;
            if (!this.productosUnicos.has(producto.codigoProducto)) {
              this.productosUnicos.set(producto.codigoProducto, producto);
            }
          });

          // Convertir el mapa a array para usar en el selector
          this.productos = Array.from(this.productosUnicos.values());
        },
        error: (err) => {
          console.error('Error al cargar productos', err);
          this.error = 'Error al cargar la lista de productos. Por favor, intente nuevamente.';
        }
      });
  }

  generarReporte(): void {
    if (!this.fechaInicio || !this.fechaFin) {
      this.error = 'Por favor, seleccione ambas fechas para generar el reporte.';
      return;
    }

    const fechaInicio = new Date(this.fechaInicio);
    const fechaFin = new Date(this.fechaFin);

    if (fechaInicio > fechaFin) {
      this.error = 'La fecha de inicio debe ser anterior o igual a la fecha fin.';
      return;
    }

    this.loading = true;
    this.error = '';

    // Determinar el endpoint a usar
    let fetchMethod;

    if (this.productoSeleccionado) {
      // Si hay un producto seleccionado, filtrar por ese producto
      fetchMethod = this.conciliacionesService.getByCodigoProducto(this.productoSeleccionado);
    } else {
      // Si no hay producto seleccionado, obtener todas las conciliaciones
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
    // Ajustar para incluir todo el día fin
    fechaFin.setHours(23, 59, 59, 999);

    return conciliaciones.filter(c => {
      const fechaConciliacion = new Date(c.fechaConciliacion);
      return fechaConciliacion >= fechaInicio && fechaConciliacion <= fechaFin;
    });
  }

  procesarDatos(): void {
    // Limpiar datos previos
    this.conciliacionesPorProducto.clear();
    this.conciliacionesPorMes.clear();
    this.conciliacionesPorEstado.clear();

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

      // Agrupar por estado
      const estado = conciliacion.estadoConciliacion.descripcion;
      if (!this.conciliacionesPorEstado.has(estado)) {
        this.conciliacionesPorEstado.set(estado, []);
      }
      this.conciliacionesPorEstado.get(estado)?.push(conciliacion);
    });

    // Ordenar los meses cronológicamente
    this.ordenarMeses();
  }

  ordenarMeses(): void {
    // Convertir el mapa a array, ordenar, y luego volver a convertir a mapa
    const mesesArray = Array.from(this.conciliacionesPorMes.entries());
    mesesArray.sort((a, b) => a[0].localeCompare(b[0]));

    // Crear un nuevo mapa ordenado
    this.conciliacionesPorMes = new Map(mesesArray);
  }

  exportarExcel(): void {
    // Datos para el Excel
    const datosExcel: any[] = [];

    // Encabezado
    datosExcel.push({
      'Reporte': 'Reporte de Conciliaciones por Producto',
      'Periodo': `${this.formatearFechaParaMostrar(this.fechaInicio)} - ${this.formatearFechaParaMostrar(this.fechaFin)}`,
      'Total': this.totalConciliaciones,
      'Descuadradas': this.totalDescuadradas
    });

    // Espacio en blanco
    datosExcel.push({});

    // Resumen por producto
    datosExcel.push({ 'Resumen por Producto': 'Resumen por Producto' });
    datosExcel.push({
      'Producto': 'Producto',
      'Conciliaciones': 'Conciliaciones',
      'Descuadradas': 'Descuadradas',
      'Diferencia Física': 'Diferencia Física',
      'Diferencia Valor': 'Diferencia Valor'
    });

    this.nombresProductos.forEach(producto => {
      const conciliaciones = this.conciliacionesPorProducto.get(producto) || [];
      const descuadradas = conciliaciones.filter(c => c.estadoConciliacion.codigoEstado === 'D').length;
      const diferenciaFisica = conciliaciones.reduce((sum, c) => sum + c.diferenciaFisica, 0);
      const diferenciaValor = conciliaciones.reduce((sum, c) => sum + c.diferenciaValor, 0);

      datosExcel.push({
        'Producto': producto,
        'Conciliaciones': conciliaciones.length,
        'Descuadradas': descuadradas,
        'Diferencia Física': diferenciaFisica,
        'Diferencia Valor': this.formatearMoneda(diferenciaValor)
      });
    });

    // Espacio en blanco
    datosExcel.push({});
    datosExcel.push({});

    // Resumen por mes
    datosExcel.push({ 'Tendencia Mensual': 'Tendencia Mensual' });
    datosExcel.push({
      'Mes': 'Mes',
      'Conciliaciones': 'Conciliaciones',
      'Descuadradas': 'Descuadradas',
      'Diferencia Física': 'Diferencia Física',
      'Diferencia Valor': 'Diferencia Valor'
    });

    this.meses.forEach(mes => {
      const conciliaciones = this.conciliacionesPorMes.get(mes) || [];
      const descuadradas = conciliaciones.filter(c => c.estadoConciliacion.codigoEstado === 'D').length;
      const diferenciaFisica = conciliaciones.reduce((sum, c) => sum + c.diferenciaFisica, 0);
      const diferenciaValor = conciliaciones.reduce((sum, c) => sum + c.diferenciaValor, 0);

      datosExcel.push({
        'Mes': this.formatearMes(mes),
        'Conciliaciones': conciliaciones.length,
        'Descuadradas': descuadradas,
        'Diferencia Física': diferenciaFisica,
        'Diferencia Valor': this.formatearMoneda(diferenciaValor)
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
      'Producto': 'Producto',
      'Sucursal': 'Sucursal',
      'Diferencia Física': 'Diferencia Física',
      'Diferencia Valor': 'Diferencia Valor',
      'Estado': 'Estado'
    });

    this.conciliaciones.forEach(c => {
      datosExcel.push({
        'ID': c.idConciliacion,
        'Fecha': this.formatearFechaParaMostrar(c.fechaConciliacion),
        'Producto': c.sucursalProducto.producto.nombreProducto,
        'Sucursal': c.sucursalProducto.sucursal.nombreSucursal,
        'Diferencia Física': c.diferenciaFisica,
        'Diferencia Valor': c.diferenciaValor,
        'Estado': c.estadoConciliacion.descripcion
      });
    });

    // Crear Excel
    const worksheet = XLSX.utils.json_to_sheet(datosExcel, { skipHeader: true });
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Reporte por Producto');

    // Guardar archivo
    let nombreArchivo;
    if (this.productoSeleccionado) {
      const producto = this.productosUnicos.get(this.productoSeleccionado);
      nombreArchivo = `Reporte_${producto?.nombreProducto.replace(/\s+/g, '_')}_${this.fechaInicio}_${this.fechaFin}.xlsx`;
    } else {
      nombreArchivo = `Reporte_Todos_Productos_${this.fechaInicio}_${this.fechaFin}.xlsx`;
    }

    XLSX.writeFile(workbook, nombreArchivo);
  }

  // Métodos auxiliares
  toggleSeccion(seccion: string): void {
    switch (seccion) {
      case 'tendenciaMensual':
        this.showTendenciaMensual = !this.showTendenciaMensual;
        break;
      case 'distribucionEstados':
        this.showDistribucionEstados = !this.showDistribucionEstados;
        break;
      case 'detalleConciliaciones':
        this.showDetalleConciliaciones = !this.showDetalleConciliaciones;
        break;
    }
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

  formatearMes(mesStr: string): string {
    const [year, month] = mesStr.split('-');
    const fecha = new Date(parseInt(year), parseInt(month) - 1, 1);
    return fecha.toLocaleDateString('es-CO', { year: 'numeric', month: 'long' });
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

  // Método para contar conciliaciones descuadradas por producto
  contarDescuadradasPorProducto(producto: string): number {
    const conciliaciones = this.conciliacionesPorProducto.get(producto) || [];
    return conciliaciones.filter(c => c.estadoConciliacion.codigoEstado === 'D').length;
  }

  // Métodos para obtener diferencias por producto
  getDiferenciaFisicaPorProducto(producto: string): number {
    const conciliaciones = this.conciliacionesPorProducto.get(producto) || [];
    return conciliaciones.reduce((sum, c) => sum + c.diferenciaFisica, 0);
  }

  getDiferenciaValorPorProducto(producto: string): number {
    const conciliaciones = this.conciliacionesPorProducto.get(producto) || [];
    return conciliaciones.reduce((sum, c) => sum + c.diferenciaValor, 0);
  }

  // Métodos para contar conciliaciones por mes
  contarConciliacionesPorMes(mes: string): number {
    return this.conciliacionesPorMes.get(mes)?.length || 0;
  }

  contarDescuadradasPorMes(mes: string): number {
    const conciliaciones = this.conciliacionesPorMes.get(mes) || [];
    return conciliaciones.filter(c => c.estadoConciliacion.codigoEstado === 'D').length;
  }

  // Métodos para obtener diferencias por mes
  getDiferenciaFisicaPorMes(mes: string): number {
    const conciliaciones = this.conciliacionesPorMes.get(mes) || [];
    return conciliaciones.reduce((sum, c) => sum + c.diferenciaFisica, 0);
  }

  getDiferenciaValorPorMes(mes: string): number {
    const conciliaciones = this.conciliacionesPorMes.get(mes) || [];
    return conciliaciones.reduce((sum, c) => sum + c.diferenciaValor, 0);
  }

  // Getters para el template
  get nombresProductos(): string[] {
    return Array.from(this.conciliacionesPorProducto.keys());
  }

  get meses(): string[] {
    return Array.from(this.conciliacionesPorMes.keys());
  }

  get estados(): string[] {
    return Array.from(this.conciliacionesPorEstado.keys());
  }
}
