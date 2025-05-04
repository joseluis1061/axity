import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IConciliacion } from '../../interfaces/conciliaciones.interface';

@Injectable({
  providedIn: 'root'
})
export class ConciliacionesService {
  private readonly URL = environment.API_URL;
  private http = inject(HttpClient);

  getAll(): Observable<IConciliacion[]> {
    return this.http.get<IConciliacion[]>(`${this.URL}/conciliaciones`);
  }

  getById(id: number): Observable<IConciliacion> {
    return this.http.get<IConciliacion>(`${this.URL}/conciliaciones/${id}`);
  }

  getByDate(date: string): Observable<IConciliacion[]> {
    return this.http.get<IConciliacion[]>(`${this.URL}/conciliaciones/fecha/${date}`);
  }

  getBySucursal(sucursal: string): Observable<IConciliacion[]> {
    return this.http.get<IConciliacion[]>(`${this.URL}/conciliaciones/sucursal/${sucursal}`);
  }

  getByCodigoProducto(codigoProducto: string): Observable<IConciliacion[]> {
    return this.http.get<IConciliacion[]>(`${this.URL}/conciliaciones/producto/${codigoProducto}`);
  }

  getByCodigoEstado(codigoEstado: string): Observable<IConciliacion[]> {
    return this.http.get<IConciliacion[]>(`${this.URL}/conciliaciones/estado/${codigoEstado}`);
  }

  getByFechaYEstado(fecha: string, codigoEstado: string): Observable<IConciliacion[]> {
    return this.http.get<IConciliacion[]>(
      `${this.URL}/conciliaciones/fecha/${fecha}/estado/${codigoEstado}`
    );
  }

  getDescuadradasPorFecha(fecha: string): Observable<IConciliacion[]> {
    return this.http.get<IConciliacion[]>(
      `${this.URL}/conciliaciones/descuadradas/fecha/${fecha}`
    );
  }

  getByFechaAndSucursalProducto(fecha: string, codigoSucursal: string,
                               codigoProducto: string, codigoDocumento: string): Observable<IConciliacion> {
    return this.http.get<IConciliacion>(
      `${this.URL}/conciliaciones/fecha/${fecha}/sucursal/${codigoSucursal}/producto/${codigoProducto}/documento/${codigoDocumento}`
    );
  }

  create(conciliacion: Partial<IConciliacion>): Observable<IConciliacion> {
    return this.http.post<IConciliacion>(`${this.URL}/conciliaciones`, conciliacion);
  }

  update(id: number, conciliacion: Partial<IConciliacion>): Observable<IConciliacion> {
    return this.http.put<IConciliacion>(`${this.URL}/conciliaciones/${id}`, conciliacion);
  }

  delete(conciliacionId: number): Observable<void> {
    return this.http.delete<void>(`${this.URL}/conciliaciones/${conciliacionId}`);
  }

  procesarConciliaciones(fecha: string): Observable<string> {
    return this.http.post<string>(`${this.URL}/conciliaciones/procesar/${fecha}`, {});
  }

  // Método para cargar archivo AS400
  uploadArchivoAS400(formData: FormData): Observable<any> {
    return this.http.post<any>(`${this.URL}/conciliaciones/upload`, formData);
  }
}
