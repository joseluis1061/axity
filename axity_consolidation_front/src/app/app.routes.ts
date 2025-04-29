import { Routes } from '@angular/router';
import { UnauthorizedComponent } from './core/shared/components/unauthorized/unauthorized.component';
import { DashboardComponent } from './core/shared/dashboard/dashboard.component';
import { HomeComponent } from './features/home/pages/home/home.component';

export const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
    children: [
      {
        path: 'home',
        component: HomeComponent
      },
      {
        path: 'conciliaciones',
        loadChildren: () => import('./features/conciliaciones/conciliaciones.routes').then(m => m.CONCILIACIONES_ROUTES)
      },
      {
        path: 'reportes',
        loadChildren: () => import('./features/reportes/reportes.routes').then(m => m.REPORTES_ROUTES)
      },
      // {
      //   path: 'catalogos',
      //   loadChildren: () => import('./features/catalogos/catalogos.routes').then(m => m.CATALOGOS_ROUTES)
      // },

      // {
      //   path: 'admin',
      //   loadChildren: () => import('./features/admin/admin.routes').then(m => m.ADMIN_ROUTES)
      // },
      {
        path: '',
        redirectTo: 'dashboard/home',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: 'no-autorizado',
    component: UnauthorizedComponent
  },
  {
    path: '',
    redirectTo: 'dashboard/home',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'home'
  }
];
