export interface IConciliacion {
  idConciliacion: number
  fechaConciliacion: string
  codigoSucursal: string
  codigoProducto: string
  codigoDocumento: string
  diferenciaFisica: number
  diferenciaValor: number
  codigoEstado: string
  descripcionEstado: string
  fechaCreacion: string
  sucursalProducto: ISucursalProducto
  estadoConciliacion: IEstadoConciliacion
}

export interface ISucursalProducto {
  codigoSucursal: string
  codigoProducto: string
  codigoDocumento: string
  sucursal: ISucursal
  producto: IProducto
  documento: IDocumento
}

export interface ISucursal {
  codigoSucursal: string
  nombreSucursal: string
}

export interface IProducto {
  codigoProducto: string
  nombreProducto: string
}

export interface IDocumento {
  codigoDocumento: string
  descripcion: string
}

export interface IEstadoConciliacion {
  codigoEstado: string
  descripcion: string
}
