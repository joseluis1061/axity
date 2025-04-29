import { TestBed } from '@angular/core/testing';

import { ConciliacionesService } from './conciliaciones.service';

describe('ConciliacionesService', () => {
  let service: ConciliacionesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConciliacionesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
