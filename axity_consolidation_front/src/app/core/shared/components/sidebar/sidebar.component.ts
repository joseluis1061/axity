import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Subject } from 'rxjs';

import {
  CdkMenu,
  CdkMenuTrigger,
  CdkMenuItem,
  CdkMenuBar,
} from '@angular/cdk/menu';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    CdkMenuBar,
    CdkMenuItem,
    CdkMenuTrigger,
    CdkMenu
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
  host: {}
})
export class SidebarComponent {
  // --- DASHBOARD ---
  showDashboard = signal(false);

  // --- CONCILIACIONES ---
  showConciliaciones = signal(false);

  // --- CATALOGOS ---
  showCatalogos = signal(false);

  // --- REPORTES ---
  showReportes = signal(false);

  // --- ADMINISTRACION ---
  showAdmin = signal(false);

  private destroy$ = new Subject<void>();

  // --- DASHBOARD CONTROLS ---
  toggleDashboard() {
    this.showDashboard.update(v => !v);
    this.showConciliaciones.set(false);
    this.showCatalogos.set(false);
    this.showReportes.set(false);
    this.showAdmin.set(false);
  }

  // --- CONCILIACIONES CONTROLS ---
  toggleConciliaciones() {
    this.showConciliaciones.update(v => !v);
    this.showDashboard.set(false);
    this.showCatalogos.set(false);
    this.showReportes.set(false);
    this.showAdmin.set(false);
  }

  // --- CATALOGOS CONTROLS ---
  toggleCatalogos() {
    this.showCatalogos.update(v => !v);
    this.showDashboard.set(false);
    this.showConciliaciones.set(false);
    this.showReportes.set(false);
    this.showAdmin.set(false);
  }

  // --- REPORTES CONTROLS ---
  toggleReportes() {
    this.showReportes.update(v => !v);
    this.showDashboard.set(false);
    this.showConciliaciones.set(false);
    this.showCatalogos.set(false);
    this.showAdmin.set(false);
  }

  // --- ADMIN CONTROLS ---
  toggleAdmin() {
    this.showAdmin.update(v => !v);
    this.showDashboard.set(false);
    this.showConciliaciones.set(false);
    this.showCatalogos.set(false);
    this.showReportes.set(false);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
