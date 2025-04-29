import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteConciliacionesMensualComponent } from './reporte-conciliaciones-mensual.component';

describe('ReporteConciliacionesMensualComponent', () => {
  let component: ReporteConciliacionesMensualComponent;
  let fixture: ComponentFixture<ReporteConciliacionesMensualComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteConciliacionesMensualComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReporteConciliacionesMensualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
