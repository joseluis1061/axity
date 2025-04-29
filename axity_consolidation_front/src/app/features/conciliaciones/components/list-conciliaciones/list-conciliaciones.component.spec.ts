import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListConciliacionesComponent } from './list-conciliaciones.component';

describe('ListConciliacionesComponent', () => {
  let component: ListConciliacionesComponent;
  let fixture: ComponentFixture<ListConciliacionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListConciliacionesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListConciliacionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
