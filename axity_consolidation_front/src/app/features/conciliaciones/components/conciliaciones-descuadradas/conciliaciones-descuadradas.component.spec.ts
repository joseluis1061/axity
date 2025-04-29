import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConciliacionesDescuadradasComponent } from './conciliaciones-descuadradas.component';

describe('ConciliacionesDescuadradasComponent', () => {
  let component: ConciliacionesDescuadradasComponent;
  let fixture: ComponentFixture<ConciliacionesDescuadradasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConciliacionesDescuadradasComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConciliacionesDescuadradasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
