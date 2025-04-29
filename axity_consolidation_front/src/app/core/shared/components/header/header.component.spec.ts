import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { AuthService } from '../../../services/auth/auth.service';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

class MockAuthService {
  logout() {
    return of(true); // Simula un logout exitoso
  }
}

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let authServiceMock: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    // Creamos un mock del AuthService
    authServiceMock = jasmine.createSpyObj('AuthService', ['logout']);

    await TestBed.configureTestingModule({
      imports: [HeaderComponent],  // Importamos el componente standalone aquí
      providers: [
        { provide: AuthService, useValue: authServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should call logout when the button is clicked', () => {
    // Seleccionamos el botón de cerrar sesión con By.css
    const logoutButton = fixture.debugElement.query(By.css('.logout-button')).nativeElement;

    // Disparamos el clic en el botón
    logoutButton.click();

    // Esperamos que el método logout sea llamado
    expect(authServiceMock.logout).toHaveBeenCalled();
  });
});
