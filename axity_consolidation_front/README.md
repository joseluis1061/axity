# Sistema de Conciliación Bancaria

Este proyecto es una aplicación de conciliación bancaria que permite gestionar, visualizar y analizar conciliaciones, con enfoque especial en la detección y manejo de descuadres. Está compuesto por un frontend en Angular 17 y un backend en Spring Boot.

## Características principales

- Visualización y filtrado de conciliaciones
- Detección y gestión de conciliaciones descuadradas
- Reportes detallados con agrupaciones por sucursal, producto y mes
- Exportación a Excel de informes
- Carga de archivos AS400 para generación automática de conciliaciones
- Proceso batch para análisis automático de descuadres

## Requisitos previos

- Node.js (v18.x o superior)
- Angular CLI 17.x
- Java JDK 17 o superior
- Spring Framework 3.4.5 o superior
- Gradle 8.x o superior
- PostgreSQL 14 o superior

## Instalación y configuración

### Backend (Spring Boot)

1. Clona el repositorio del backend:
   ```bash
   git clone https://github.com/tu-usuario/axity-consolidation-back.git
   cd axity-consolidation-back
   ```

2. Configura la base de datos PostgreSQL en `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/conciliaciones_db
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_password
   ```

3. Compila e inicia el proyecto usando Gradle:
   ```bash
   ./gradlew clean build
   ./gradlew bootRun
   ```
   
   En Windows, usa:
   ```bash
   gradlew.bat clean build
   gradlew.bat bootRun
   ```

   El servidor backend estará disponible en `http://localhost:8080`

### Frontend (Angular 17)

1. Clona el repositorio del frontend:
   ```bash
   git clone https://github.com/tu-usuario/axity-consolidation-front.git
   cd axity-consolidation-front
   ```

2. Instala las dependencias:
   ```bash
   npm install
   ```

3. Configura el archivo de entorno `src/environments/environment.ts` para que apunte al backend:
   ```typescript
   export const environment = {
     production: false,
     API_URL: 'http://localhost:8080'
   };
   ```

4. Inicia el servidor de desarrollo:
   ```bash
   ng serve
   ```

   La aplicación estará disponible en `http://localhost:4200`

## Estructura del proyecto

### Backend

El proyecto backend está organizado siguiendo una arquitectura en capas:

- **Controller**: Maneja las peticiones HTTP y define los endpoints REST
- **Service**: Contiene la lógica de negocio
- **Repository**: Gestiona la persistencia de datos
- **Model**: Define las entidades del dominio
- **Config**: Configuraciones de la aplicación, seguridad y otros componentes
- **Exception**: Manejo centralizado de excepciones
- **Util**: Clases utilitarias

### Frontend

El proyecto frontend está organizado en módulos y componentes:

- **Core**: 
  - **Services**: Servicios para comunicación con el backend
  - **Interfaces**: Definición de tipos de datos
  - **Shared**: Componentes y directivas compartidas
- **Features**: 
  - **Conciliaciones**: Gestión de conciliaciones
  - **Reportes**: Módulo de reportes y análisis
- **Assets**: Recursos estáticos como imágenes, iconos y estilos globales

## Uso de la aplicación

### Listado de conciliaciones

La pantalla principal muestra todas las conciliaciones registradas, con opciones de filtrado por fecha, sucursal, producto y estado.

### Conciliaciones descuadradas

La vista de conciliaciones descuadradas muestra todas las conciliaciones con estado "D" (Descuadradas) para una fecha específica, agrupadas por sucursal.

### Reportes

El módulo de reportes permite generar:
- Reporte detallado de conciliaciones descuadradas
- Reporte por sucursal
- Reporte por producto

### Carga de archivos AS400

Para cargar conciliaciones desde archivos del sistema AS400:

1. Haz clic en "Nueva conciliación" en la pantalla de listado de conciliaciones
2. En el modal, selecciona el año y mes de la conciliación
3. Haz clic en "Seleccionar archivo" y elige el archivo AS400
4. Haz clic en "Procesar archivo"

## Desarrollo

### Comandos útiles - Backend (Gradle)

```bash
# Ejecutar pruebas
./gradlew test

# Compilar sin pruebas
./gradlew clean build -x test

# Generar documentación Javadoc
./gradlew javadoc

# Analizar dependencias
./gradlew dependencies

# Verificar problemas de estilo de código
./gradlew checkstyleMain
```

### Comandos útiles - Frontend

```bash
# Ejecutar pruebas
ng test

# Generar build de producción
ng build --configuration production

# Generar componente
ng generate component features/nombre-feature/components/nombre-componente

# Ejecutar linting
ng lint

# Verificar vulnerabilidades en dependencias
npm audit
```

## Despliegue en producción

### Backend

1. Crea un archivo `application-prod.properties` con la configuración de producción
2. Genera el jar:
   ```bash
   ./gradlew bootJar -Pprod
   ```
3. Ejecuta el jar:
   ```bash
   java -jar build/libs/axity-consolidation-back-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```

### Frontend

1. Genera la versión de producción:
   ```bash
   ng build --configuration production
   ```
2. Despliega los archivos generados en la carpeta `dist/` en tu servidor web (Apache, Nginx, etc.)

## Solución de problemas comunes

### Backend

- **Error de conexión a la base de datos**: Verifica las credenciales y que el servidor PostgreSQL esté activo
- **Puerto 8080 en uso**: Cambia el puerto en `application.properties` con `server.port=8081`
- **Error de memoria**: Aumenta la memoria de la JVM con `./gradlew bootRun -Dorg.gradle.jvmargs="-Xmx2g -XX:MaxPermSize=512m"`

### Frontend

- **Error "Node modules not found"**: Ejecuta `npm install` para reinstalar las dependencias
- **CORS error**: Verifica la configuración CORS en el backend y asegúrate de que los dominios estén correctamente permitidos
- **Angular CLI no encontrado**: Instala globalmente con `npm install -g @angular/cli`

## Contribución

1. Haz fork del repositorio
2. Crea una rama para tu feature: `git checkout -b feature/nueva-funcionalidad`
3. Haz commit de tus cambios: `git commit -am 'Añadir nueva funcionalidad'`
4. Haz push a la rama: `git push origin feature/nueva-funcionalidad`
5. Crea un pull request

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo LICENSE para más detalles.

## Contacto

Para cualquier consulta o sugerencia, contacta con el equipo de desarrollo en: joseluis836ps@gmail.com
