import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeComponent } from './home.component';

import { By } from '@angular/platform-browser';
import { BtnCampusPeriodosComponent } from '../../../../core/shared/components/btn-campus-periodos/btn-campus-periodos.component';


describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería renderizar el título principal', () => {
    const title = fixture.debugElement.query(By.css('h1')).nativeElement;
    expect(title.textContent).toContain('Portal de administración');
  });

  it('debería renderizar el subtítulo', () => {
    const subtitle = fixture.debugElement.query(By.css('h2')).nativeElement;
    expect(subtitle.textContent).toContain('Bienvenido a SOFIA');
  });

  it('debería renderizar el botón de campus y periodos', () => {
    const btnCampusPeriodos = fixture.debugElement.query(By.directive(BtnCampusPeriodosComponent));
    expect(btnCampusPeriodos).toBeTruthy();
  });
});
