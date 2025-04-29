import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { IConciliacion } from '../../../../core/interfaces/conciliaciones.interface';
import { ConciliacionesService } from '../../../../core/services/conciliaciones/conciliaciones.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-conciliaciones-descuadradas',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './conciliaciones-descuadradas.component.html',
  styleUrls: ['./conciliaciones-descuadradas.component.scss']
})
export class ConciliacionesDescuadradasComponent implements OnInit {
  private conciliacionesService = inject(ConciliacionesService);

  conciliacionesDescuadradas: IConciliacion[] = [];
  loading = false;
  error = '';

  // Filtro por fecha
  fechaFilter: string = '';

  // Para mostrar resultados agrupados
  conciliacionesPorSucursal: Map<string, IConciliacion[]> = new Map();
  totalDescuadradas = 0;
  totalDiferenciaFisica = 0;
  totalDiferenciaValor = 0;

  // Datos de la fecha actual
  fechaActual: Date = new Date();
  fechaFormateada: string = '';

  ngOnInit(): void {
    // Establecer la fecha actual como filtro por defecto
    this.setFechaHoy();
    this.loadConciliacionesDescuadradas();
  }

  setFechaHoy(): void {
    // Formatear fecha actual como YYYY-MM-DD para el input date
    const year = this.fechaActual.getFullYear();
    const month = String(this.fechaActual.getMonth() + 1).padStart(2, '0');
    const day = String(this.fechaActual.getDate()).padStart(2, '0');
    this.fechaFilter = `${year}-${month}-${day}`;

    // Formatear fecha para mostrar
    this.fechaFormateada = this.formatDate(this.fechaFilter);
  }

  loadConciliacionesDescuadradas(): void {
    if (!this.fechaFilter) {
      this.error = 'Por favor, seleccione una fecha para consultar.';
      return;
    }

    this.loading = true;
    this.error = '';

    this.conciliacionesService.getDescuadradasPorFecha(this.fechaFilter)
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
    this.totalDescuadradas = this.conciliacionesDescuadradas.length;
    this.totalDiferenciaFisica = 0;
    this.totalDiferenciaValor = 0;

    // Agrupar por sucursal y calcular totales
    this.conciliacionesDescuadradas.forEach(conciliacion => {
      // Acumular totales
      this.totalDiferenciaFisica += conciliacion.diferenciaFisica;
      this.totalDiferenciaValor += conciliacion.diferenciaValor;

      // Agrupar por sucursal
      const nombreSucursal = conciliacion.sucursalProducto.sucursal.nombreSucursal;
      if (!this.conciliacionesPorSucursal.has(nombreSucursal)) {
        this.conciliacionesPorSucursal.set(nombreSucursal, []);
      }

      this.conciliacionesPorSucursal.get(nombreSucursal)?.push(conciliacion);
    });
  }

  applyFechaFilter(): void {
    this.loadConciliacionesDescuadradas();
  }

  ejecutarProcesoBatch(): void {
    if (!this.fechaFilter) {
      this.error = 'Por favor, seleccione una fecha para ejecutar el proceso batch.';
      return;
    }

    this.loading = true;
    this.error = '';

    this.conciliacionesService.procesarConciliaciones(this.fechaFilter)
      .pipe(finalize(() => {
        this.loading = false;
        // Recargar datos después de procesar
        this.loadConciliacionesDescuadradas();
      }))
      .subscribe({
        next: (response) => {
          console.log('Proceso batch ejecutado:', response);
        },
        error: (err) => {
          console.error('Error al ejecutar proceso batch', err);
          this.error = 'Error al ejecutar el proceso batch. Por favor, intente nuevamente.';
        }
      });
  }

  // Obtener color según diferencias
  getDiferenciaClass(valor: number): string {
    if (valor === 0) return 'text-gray-600';
    return valor > 0 ? 'text-red-600' : 'text-green-600';
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

  // Formatear fecha
  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-CO', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  // Obtener sucursales como array para iterar
  get sucursales(): string[] {
    return Array.from(this.conciliacionesPorSucursal.keys());
  }

  // Obtener conciliaciones para una sucursal
  getConciliacionesPorSucursal(sucursal: string): IConciliacion[] {
    return this.conciliacionesPorSucursal.get(sucursal) || [];
  }

  // Calcular total de diferencia física por sucursal
  getTotalDiferenciaFisicaPorSucursal(sucursal: string): number {
    const conciliaciones = this.conciliacionesPorSucursal.get(sucursal) || [];
    return conciliaciones.reduce((sum, conciliacion) => sum + conciliacion.diferenciaFisica, 0);
  }

  // Calcular total de diferencia valor por sucursal
  getTotalDiferenciaValorPorSucursal(sucursal: string): number {
    const conciliaciones = this.conciliacionesPorSucursal.get(sucursal) || [];
    return conciliaciones.reduce((sum, conciliacion) => sum + conciliacion.diferenciaValor, 0);
  }
}
