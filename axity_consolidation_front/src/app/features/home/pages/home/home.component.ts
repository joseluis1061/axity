import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MenuItem } from '../../../../core/interfaces/menu-item.interface';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  user: any = null;
  userRole: Array<string> | null = null;

  cardItems: MenuItem[] = [
    {
      iconUrl: 'assets/icons/conciliaciones.svg',
      alt: 'Conciliaciones',
      link: '/conciliaciones/list',
      title: 'Conciliaciones',
      description: 'Gestión y administración de conciliaciones bancarias. Permite listar, crear, editar y visualizar conciliaciones descuadradas.'
    },
    {
      iconUrl: 'assets/icons/catalogos.svg',
      alt: 'Catálogos',
      link: '/catalogos',
      title: 'Catálogos',
      description: 'Administración de sucursales, productos, documentos, estados de conciliación y relaciones entre entidades del sistema.'
    },
    {
      iconUrl: 'assets/icons/proceso-batch.svg',
      alt: 'Proceso Batch',
      link: '/conciliaciones/proceso-batch',
      title: 'Proceso Batch',
      description: 'Ejecución del proceso automatizado para identificar conciliaciones descuadradas en una fecha específica.'
    },
    {
      iconUrl: 'assets/icons/reportes.svg',
      alt: 'Reportes',
      link: '/reportes',
      title: 'Reportes',
      description: 'Visualización de informes consolidados, por sucursal, por producto y análisis de tendencias para la toma de decisiones.'
    },
    {
      iconUrl: 'assets/icons/admin.svg',
      alt: 'Administración',
      link: '/admin',
      title: 'Administración',
      description: 'Gestión de usuarios y configuración general del sistema de conciliación bancaria.'
    }
  ];

  async ngOnInit() {
  }

  logout(): void {
    console.log('Cerrando sesión...');
  }

  // Método para obtener el icono Material basado en el tipo de tarjeta
  getIconForCard(cardType: string): string {
    const iconMap: Record<string, string> = {
      'Conciliaciones': 'sync_alt',
      'Catálogos': 'category',
      'Proceso Batch': 'batch_prediction',
      'Reportes': 'assessment',
      'Administración': 'settings'
    };

    return iconMap[cardType] || 'help_outline'; // Icono predeterminado si no hay coincidencia
  }
}
