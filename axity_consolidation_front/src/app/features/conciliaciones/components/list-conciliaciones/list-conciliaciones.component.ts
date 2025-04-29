import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { IConciliacion } from '../../../../core/interfaces/conciliaciones.interface';
import { ConciliacionesService } from '../../../../core/services/conciliaciones/conciliaciones.service';
import { finalize, forkJoin } from 'rxjs';
import { Dialog, DialogModule } from '@angular/cdk/dialog';
import { CargarConciliacionComponent } from '../cargar-conciliacion/cargar-conciliacion.component';

@Component({
  selector: 'app-list-conciliaciones',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink, DialogModule],
  templateUrl: './list-conciliaciones.component.html',
  styleUrls: ['./list-conciliaciones.component.scss']
})
export class ListConciliacionesComponent implements OnInit {
  private conciliacionesService = inject(ConciliacionesService);
  private dialog = inject(Dialog);

  conciliaciones: IConciliacion[] = [];
  filteredConciliaciones: IConciliacion[] = [];
  loading = false;
  error = '';

  // Filtros
  fechaFilter: string = '';
  sucursalFilter: string = '';
  productoFilter: string = '';
  estadoFilter: string = '';

  // Valores únicos para filtros dropdown
  sucursales: Set<string> = new Set();
  productos: Set<string> = new Set();
  estados: Set<string> = new Set();

  // Mapeo de códigos a nombres para filtros
  sucursalCodesMap: Map<string, string> = new Map();
  productoCodesMap: Map<string, string> = new Map();
  estadoCodesMap: Map<string, string> = new Map();

  ngOnInit(): void {
    this.loadConciliaciones();
  }

  loadConciliaciones(): void {
    this.loading = true;
    this.error = '';

    this.conciliacionesService.getAll()
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => {
          this.conciliaciones = data;
          this.filteredConciliaciones = data;
          this.extractFilterValues();
        },
        error: (err) => {
          console.error('Error al cargar conciliaciones', err);
          this.error = 'Error al cargar las conciliaciones. Por favor, intente nuevamente.';
        }
      });
  }

  extractFilterValues(): void {
    // Extraer valores únicos para los filtros y crear mapeos
    this.sucursales = new Set();
    this.productos = new Set();
    this.estados = new Set();

    this.sucursalCodesMap.clear();
    this.productoCodesMap.clear();
    this.estadoCodesMap.clear();

    this.conciliaciones.forEach(c => {
      // Sucursales
      const sucursalNombre = c.sucursalProducto.sucursal.nombreSucursal;
      const sucursalCodigo = c.sucursalProducto.sucursal.codigoSucursal;
      this.sucursales.add(sucursalNombre);
      this.sucursalCodesMap.set(sucursalNombre, sucursalCodigo);

      // Productos
      const productoNombre = c.sucursalProducto.producto.nombreProducto;
      const productoCodigo = c.sucursalProducto.producto.codigoProducto;
      this.productos.add(productoNombre);
      this.productoCodesMap.set(productoNombre, productoCodigo);

      // Estados
      const estadoDescripcion = c.estadoConciliacion.descripcion;
      const estadoCodigo = c.estadoConciliacion.codigoEstado;
      this.estados.add(estadoDescripcion);
      this.estadoCodesMap.set(estadoDescripcion, estadoCodigo);
    });
  }

  applyFilters(): void {
    this.loading = true;
    this.error = '';

    // Determinar qué endpoint usar basado en los filtros seleccionados
    let fetchMethod;

    // Caso 1: Filtro por fecha y estado
    if (this.fechaFilter && this.estadoFilter) {
      const estadoCodigo = this.estadoCodesMap.get(this.estadoFilter) || '';
      fetchMethod = this.conciliacionesService.getByFechaYEstado(this.fechaFilter, estadoCodigo);
    }
    // Caso 2: Solo filtro por fecha
    else if (this.fechaFilter) {
      fetchMethod = this.conciliacionesService.getByDate(this.fechaFilter);
    }
    // Caso 3: Solo filtro por sucursal
    else if (this.sucursalFilter) {
      const sucursalCodigo = this.sucursalCodesMap.get(this.sucursalFilter) || '';
      fetchMethod = this.conciliacionesService.getBySucursal(sucursalCodigo);
    }
    // Caso 4: Solo filtro por producto
    else if (this.productoFilter) {
      const productoCodigo = this.productoCodesMap.get(this.productoFilter) || '';
      fetchMethod = this.conciliacionesService.getByCodigoProducto(productoCodigo);
    }
    // Caso 5: Solo filtro por estado
    else if (this.estadoFilter) {
      const estadoCodigo = this.estadoCodesMap.get(this.estadoFilter) || '';
      fetchMethod = this.conciliacionesService.getByCodigoEstado(estadoCodigo);
    }
    // Caso 6: Sin filtros, obtener todas
    else {
      fetchMethod = this.conciliacionesService.getAll();
    }

    // Realizar la petición
    fetchMethod.pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => {
          // Aplicar filtros adicionales en el cliente si es necesario
          this.filteredConciliaciones = this.applyAdditionalClientFilters(data);
        },
        error: (err) => {
          console.error('Error al aplicar filtros', err);
          this.error = 'Error al aplicar los filtros. Por favor, intente nuevamente.';
        }
      });
  }

  applyAdditionalClientFilters(data: IConciliacion[]): IConciliacion[] {
    // Aplicar filtros adicionales en el cliente para combinaciones que no tienen endpoint directo
    return data.filter(c => {
      let matches = true;

      // Si tenemos fecha y estado, ya están filtrados por el endpoint
      if (this.fechaFilter && this.estadoFilter) {
        return true;
      }

      // Filtrar por sucursal si no se usó el endpoint específico
      if (this.sucursalFilter && !this.fechaFilter && !this.estadoFilter) {
        matches = matches && c.sucursalProducto.sucursal.nombreSucursal === this.sucursalFilter;
      }

      // Filtrar por producto si no se usó el endpoint específico
      if (this.productoFilter && !this.fechaFilter && !this.estadoFilter) {
        matches = matches && c.sucursalProducto.producto.nombreProducto === this.productoFilter;
      }

      // Filtrar por estado si no se usó el endpoint específico
      if (this.estadoFilter && !this.fechaFilter) {
        matches = matches && c.estadoConciliacion.descripcion === this.estadoFilter;
      }

      // Aplicar combinaciones de filtros que no tienen endpoint directo
      if (this.sucursalFilter && this.productoFilter) {
        matches = matches &&
          c.sucursalProducto.sucursal.nombreSucursal === this.sucursalFilter &&
          c.sucursalProducto.producto.nombreProducto === this.productoFilter;
      }

      return matches;
    });
  }

  resetFilters(): void {
    this.fechaFilter = '';
    this.sucursalFilter = '';
    this.productoFilter = '';
    this.estadoFilter = '';
    this.loadConciliaciones();
  }

  // Método para abrir el modal de carga de archivo
  openCargarConciliacionModal(): void {
    const dialogRef = this.dialog.open<boolean>(CargarConciliacionComponent, {
      minWidth: '500px',
      minHeight: '300px',
      disableClose: false,
    });

    dialogRef.closed.subscribe((result) => {
      if (result) {
        // Si el resultado es true, recargar las conciliaciones
        this.loadConciliaciones();
      }
    });
  }

  // Obtener color según estado
  getStatusColor(estado: string): string {
    switch (estado) {
      case 'A': return 'bg-green-100 text-green-800'; // Aceptada
      case 'B': return 'bg-yellow-100 text-yellow-800'; // Bajo revisión
      case 'C': return 'bg-blue-100 text-blue-800'; // Cuadrada
      case 'D': return 'bg-red-100 text-red-800'; // Descuadrada
      default: return 'bg-gray-100 text-gray-800';
    }
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
}
