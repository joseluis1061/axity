import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DialogRef } from '@angular/cdk/dialog';
import { ConciliacionesService } from '../../../../core/services/conciliaciones/conciliaciones.service';
import { finalize } from 'rxjs';


@Component({
  selector: 'app-cargar-conciliacion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cargar-conciliacion.component.html',
  styleUrl: './cargar-conciliacion.component.scss'
})
export class CargarConciliacionComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  private dialogRef = inject<DialogRef<boolean>>(DialogRef<boolean>);
  private fb = inject(FormBuilder);
  private conciliacionesService = inject(ConciliacionesService);

  cargarForm!: FormGroup;
  selectedFile: File | null = null;
  loading: boolean = false;
  error: string = '';
  success: string = '';
  meses: {value: string, descripcion: string}[] = [];
  anios: number[] = [];

  ngOnInit(): void {
    this.initForm();
    this.initListaMesesYAnios();
  }

  initForm(): void {
    this.cargarForm = this.fb.group({
      anio: ['', [Validators.required]],
      mes: ['', [Validators.required]],
      archivoAS400: [null, [Validators.required]]
    });
  }

  initListaMesesYAnios(): void {
    // Lista de meses
    this.meses = [
      { value: '01', descripcion: 'Enero' },
      { value: '02', descripcion: 'Febrero' },
      { value: '03', descripcion: 'Marzo' },
      { value: '04', descripcion: 'Abril' },
      { value: '05', descripcion: 'Mayo' },
      { value: '06', descripcion: 'Junio' },
      { value: '07', descripcion: 'Julio' },
      { value: '08', descripcion: 'Agosto' },
      { value: '09', descripcion: 'Septiembre' },
      { value: '10', descripcion: 'Octubre' },
      { value: '11', descripcion: 'Noviembre' },
      { value: '12', descripcion: 'Diciembre' }
    ];

    // Lista de años (desde el año actual hasta 5 años atrás)
    const currentYear = new Date().getFullYear();
    this.anios = Array.from({ length: 6 }, (_, i) => currentYear - i);
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.cargarForm.patchValue({ archivoAS400: this.selectedFile });
    }
  }

  onSubmit(): void {
    if (this.cargarForm.invalid || !this.selectedFile) {
      this.error = 'Por favor, complete todos los campos y seleccione un archivo.';
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    const formData = new FormData();
    formData.append('file', this.selectedFile);
    formData.append('anio', this.cargarForm.get('anio')?.value);
    formData.append('mes', this.cargarForm.get('mes')?.value);

    this.conciliacionesService.uploadArchivoAS400(formData)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (response) => {
          this.success = 'Archivo procesado correctamente.';
          // Esperar 2 segundos y cerrar el modal
          setTimeout(() => {
            this.closeDialog(true);
          }, 2000);
        },
        error: (err) => {
          console.error('Error al procesar el archivo', err);
          this.error = 'Error al procesar el archivo. Por favor, verifique el formato e intente nuevamente.';
        }
      });
  }

  closeDialog(result: boolean): void {
    this.dialogRef.close(result);
  }
}
