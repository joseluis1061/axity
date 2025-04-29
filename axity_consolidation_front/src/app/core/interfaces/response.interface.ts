// GET
export interface IResponseStandarArray<T> {
  error: string;
  data: T[];
  success: boolean;
  messages: null | string;
}

// Create, Clone, Update, retorna el id del elemento Delete success en true
export interface IResponseStandar<T> {
  error: string;
  data: T;
  success: boolean;
  messages: null | string;
}
