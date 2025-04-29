import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteConciliacionesProductosComponent } from './reporte-conciliaciones-productos.component';

describe('ReporteConciliacionesProductosComponent', () => {
  let component: ReporteConciliacionesProductosComponent;
  let fixture: ComponentFixture<ReporteConciliacionesProductosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteConciliacionesProductosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReporteConciliacionesProductosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
