import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteConciliacionesSucursalComponent } from './reporte-conciliaciones-sucursal.component';

describe('ReporteConciliacionesSucursalComponent', () => {
  let component: ReporteConciliacionesSucursalComponent;
  let fixture: ComponentFixture<ReporteConciliacionesSucursalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteConciliacionesSucursalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReporteConciliacionesSucursalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
