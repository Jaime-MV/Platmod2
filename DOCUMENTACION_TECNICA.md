# Documentación Técnica - Platmod (Backend)

## 1. Introducción
**Platmod** es una plataforma educativa (prototipo) desarrollada con una arquitectura en capas basada en **Spring Boot**. El sistema provee un backend robusto para la gestión de usuarios (estudiantes, docentes, administradores), cursos, lecciones, foros de discusión y suscripciones.

## 2. Tecnologías y Herramientas (Stack Tecnológico)
* **Lenguaje:** Java 17
* **Framework Principal:** Spring Boot 3.5.x
* **Acceso a Datos:** Spring Data JPA / Hibernate
* **Base de Datos:** PostgreSQL (Alojada en Render)
* **Seguridad:** Spring Security + JWT (JSON Web Tokens)
* **Construcción y Dependencias:** Maven
* **Utilidades:** Lombok (para reducir código repetitivo como Getters, Setters, y constructores)

## 3. Arquitectura del Proyecto
El proyecto sigue el patrón de diseño clásico de aplicaciones web multicapa (N-Tier Architecture), organizado en los siguientes paquetes principales dentro de `com.prototipo.platmod`:

* **`config/`**: Contiene las clases de configuración global de la aplicación (e.g., `SecurityConfig`, `ApplicationConfig`, `JwtAuthenticationFilter`). Aquí se define cómo se maneja la seguridad y los filtros de autenticación.
* **`controller/`**: Define los endpoints RESTful de la aplicación, exponiendo la funcionalidad al cliente frontend.
* **`dto/` (Data Transfer Objects)**: Objetos planos utilizados para transferir datos entre las capas del sistema y hacia el cliente, aislando los modelos internos de base de datos (`Entity`) de las respuestas API.
* **`entity/`**: Clases del modelo de dominio mapeadas directamente a las tablas de la base de datos a través de JPA (Modelos).
* **`repository/`**: Interfaces que extienden `JpaRepository` para el acceso a la base de datos (CRUD y consultas personalizadas).
* **`service/` & `service/impl/`**: Contiene la lógica de negocio. Las interfaces definen los contratos (`service/`) y las clases concretas (`service/impl/`) implementan dichas reglas de negocio.

## 4. Módulos Principales (Controladores)

El sistema está dividido en varias áreas funcionales principales:

* **Administración (`AdminController`)**: Endpoints para gestión general de la plataforma por parte de los administradores.
* **Autenticación (`AuthController`)**: Endpoints públicos para el inicio de sesión (Login/Registro) y generación de tokens JWT.
* **Gestión de Cursos (`CursoController`)**: Endpoints para crear, editar, listar y matricular estudiantes en cursos, módulos y lecciones.
* **Docentes (`DocenteController`)**: Lógica específica para operaciones orientadas a los profesores (asignación de cursos, gestión de perfiles).
* **Foros (`ForoController`)**: Endpoints para la creación de preguntas, hilos de debate, respuestas y sistema de valoraciones (Likes).
* **Suscripciones (`PlanSuscripcionController`)**: Gestión de los planes de pago y beneficios de los usuarios en la plataforma.
* **Usuarios (`UsuarioController`)**: Operaciones de gestión de cuentas, perfiles y estudiantes.

## 5. Entidades Principales de Base de Datos
El dominio de datos (`entity/`) cuenta con múltiples relaciones que soportan el modelo de negocio:
* **Actores:** `Usuario`, `Estudiante`, `Docente`, `PerfilDetalle`.
* **Educación:** `Curso`, `Modulo`, `Leccion`, `ProgresoEstudiante`, `AsignacionDocente`.
* **Interacción:** `ForoPregunta`, `ForoRespuesta`, `Comentario`, `ForoFavorito`, `ForoPreguntaLike`, `ForoRespuestaLike`, `ChatGrupal`, `MensajePrivado`.
* **Monetización y Logros:** `Certificado`, `Suscripcion`, `PlanSuscripcion`, `PlanBeneficio`.

## 6. Configuración de Base de Datos (application.properties)
El proyecto está configurado para conectarse a un entorno de despliegue en la nube mediante **Render**:
* **Dialecto:** `org.hibernate.dialect.PostgreSQLDialect`
* **Estrategia DDL:** `update` (Actualiza el esquema automáticamente basado en las entidades, adecuado para desarrollo/prototipado).
* Las credenciales apuntan a una instancia remota de PostgreSQL (`dpg-*.render.com`).

## 7. Flujo de Seguridad y Autenticación
1. El cliente envía credenciales vía el `/auth/login`.
2. El `AuthController` valida la solicitud utilizando `SecurityConfig` y genera un token **JWT** firmado usando `jjwt`.
3. Para peticiones subsecuentes, el cliente envía el token en la cabecera `Authorization: Bearer <token>`.
4. El `JwtAuthenticationFilter` intercepta las peticiones aseguradas, valida el token y carga el contexto del usuario en Spring Security.

## 8. Ejecución Local (Desarrollo)
Para ejecutar el backend de forma local:
1. Asegurarse de tener instalado JDK 17 y Maven.
2. Desde una terminal en el directorio `platmod`, ejecutar:
   ```bash
   ./mvnw spring-boot:run
   ```
*(Opcional: Si se requiere correr la base de datos en local, modificar el URI de base de datos en `application.properties` antes de ejecutar).*
