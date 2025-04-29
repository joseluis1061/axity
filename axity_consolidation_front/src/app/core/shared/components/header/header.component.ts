import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
  host:{
    class: 'flex justify-between items-center bg-blue_secondary py-3 px-12 w-full'
  }
})
export class HeaderComponent {
  constructor() {}

}
