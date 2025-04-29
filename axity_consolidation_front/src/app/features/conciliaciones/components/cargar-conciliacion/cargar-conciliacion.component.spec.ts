import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CargarConciliacionComponent } from './cargar-conciliacion.component';

describe('CargarConciliacionComponent', () => {
  let component: CargarConciliacionComponent;
  let fixture: ComponentFixture<CargarConciliacionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CargarConciliacionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CargarConciliacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
