import { Routes } from '@angular/router';

export const CONCILIACIONES_ROUTES: Routes = [
  {
    path: 'list',
    loadComponent: () => import('./components/list-conciliaciones/list-conciliaciones.component')
      .then(c => c.ListConciliacionesComponent)
  },
  {
    path: 'descuadradas',
    loadComponent: () => import('./components/conciliaciones-descuadradas/conciliaciones-descuadradas.component')
      .then(c => c.ConciliacionesDescuadradasComponent)
  },
];
