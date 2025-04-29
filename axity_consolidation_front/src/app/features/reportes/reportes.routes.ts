import { Routes } from '@angular/router';

export const REPORTES_ROUTES: Routes = [
  {
    path: 'descuadradas',
    loadComponent: () => import('./components/reporte-conciliaciones-descuadradas/reporte-conciliaciones-descuadradas.component')
      .then(c => c.ReporteConciliacionesDescuadradasComponent)
  },
  {
    path: 'por-sucursal',
    loadComponent: () => import('./components/reporte-conciliaciones-sucursal/reporte-conciliaciones-sucursal.component')
      .then(c => c.ReporteConciliacionesSucursalComponent)
  },
  {
    path: 'consolidado-mensual',
    loadComponent: () => import('./components/reporte-conciliaciones-mensual/reporte-conciliaciones-mensual.component')
      .then(c => c.ReporteConciliacionesMensualComponent)
  },

  {
    path: 'por-producto',
    loadComponent: () => import('./components/reporte-conciliaciones-productos/reporte-conciliaciones-productos.component')
      .then(c => c.ReporteConciliacionesProductosComponent)
  },
  // {
  //   path: 'tendencias',
  //   loadComponent: () => import('./components/tendencias/tendencias.component')
  //     .then(c => c.TendenciasComponent)
  // },
  {
    path: '',
    redirectTo: 'descuadradas',
    pathMatch: 'full'
  }
];
