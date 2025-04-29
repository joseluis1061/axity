import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteConciliacionesDescuadradasComponent } from './reporte-conciliaciones-descuadradas.component';

describe('ReporteConciliacionesDescuadradasComponent', () => {
  let component: ReporteConciliacionesDescuadradasComponent;
  let fixture: ComponentFixture<ReporteConciliacionesDescuadradasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteConciliacionesDescuadradasComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReporteConciliacionesDescuadradasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
