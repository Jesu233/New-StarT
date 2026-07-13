# New-StarT — Sistema de Compra y Administración de Tickets para Eventos



## Descripción del proyecto

**New-StarT** es un sistema de compra y administración de tickets para eventos de cualquier tipo (conciertos, fondas, cumpleaños, y otros), desarrollado bajo una **arquitectura de microservicios**. La plataforma permite gestionar el ciclo completo de un evento: desde la creación del evento, sus artistas y recintos, hasta la reserva de cupos, la emisión de tickets, la aplicación de promociones y el envío de notificaciones a los usuarios.

Cada dominio del negocio (eventos, reservas, tickets, artistas, recintos, localidades, promociones, notificaciones y autenticación) está implementado como un microservicio independiente, registrado en un servidor de descubrimiento (Eureka) y expuesto al exterior a través de un API Gateway reactivo.

## Equipo

- Matías Caro Bustos
- Yuler Campos Farias
- María Silva Riquelme

## Arquitectura

- **Service Discovery:** Eureka Server (Netflix Eureka), donde todos los microservicios se registran como clientes (`Eureka Discovery Client`).
- **API Gateway:** Spring Cloud Gateway en su variante **reactiva**, que enruta las peticiones externas hacia cada microservicio según su registro en Eureka.
- **Persistencia:** cada microservicio administra su propia base de datos (arquitectura *database-per-service*).
- **Documentación:** cada microservicio expone su propia documentación Swagger/OpenAPI (vía springdoc-openapi).
- **Orquestación local:** todos los servicios se levantan mediante `docker-compose.yml`.

## Microservicios implementados

| Microservicio | Carpeta en el repo | Puerto | Descripción |
|---|---|---|---|
| Eureka Server | `Eureka` | `8761` | Servidor de descubrimiento de servicios (Eureka Discovery Server) |
| Gateway | `gateway` | `8090` | API Gateway reactivo, punto de entrada único al sistema |
| Autenticación | `DFullS`* | `8081` | Autenticación y gestión de usuarios/roles (JWT) |
| Artistas | `artistas` | `8083` | Gestión de artistas asociados a los eventos |
| Localidades | `localidades` | `8082` | Gestión de localidades/zonas dentro de un recinto |
| Recintos | `recintos` | `8084` | Gestión de recintos donde se realizan los eventos |
| Eventos | `eventos` | `8085` | Gestión de eventos (creación, fechas, capacidad, lugar) |
| Tickets | `tickets` | `8086` | Emisión y gestión de tickets de los eventos |
| Reservas | `reservas` | `8087` | Gestión de cupos/reservas vendidos por evento |
| Promoción | `promocion` | `8088` | Gestión de promociones y descuentos |
| Notificaciones | `notificaciones` | `8089` | Envío de notificaciones a los usuarios |

\* *El microservicio `DFullS` corresponde al servicio de **autenticación**. El nombre de la carpeta quedó así por un error al momento de nombrar el proyecto; se mantiene sin modificar por motivos de tiempo, pero su función es exclusivamente la autenticación del sistema.*

## Rutas principales del Gateway

El Gateway (puerto `8090`) es un **Spring Cloud Gateway reactivo (WebFlux)** que enruta las peticiones hacia cada microservicio mediante balanceo de carga vía Eureka (`lb://<service-id>`). Las rutas configuradas son:

| ID de ruta | Servicio destino (Eureka) | Path expuesto en el Gateway |
|---|---|---|
| `ms-autenticacion` | `ms-autenticacion` | `http://localhost:8090/api/v1/auth/**` |
| `ms-promociones` | `promocion` | `http://localhost:8090/api/promociones/**` |
| `ms-notificaciones` | `notificaciones` | `http://localhost:8090/api/v1/email/**` |
| `ms-eventos` | `Eventos` | `http://localhost:8090/api/v1/evento/**` |
| `ms-reservas` | `Reservas` | `http://localhost:8090/api/v1/reserva/**` |
| `ms-recintos` | `ms-recintos` | `http://localhost:8090/api/v1/recintos/**` |
| `ms-localidades` | `ms-localidades` | `http://localhost:8090/api/v1/localidades/**` |
| `ms-artistas` | `ms-artistas` | `http://localhost:8090/api/v1/artistas/**` |
| `ms-tickets` | `Tickets` | `http://localhost:8090/api/v1/tickets/**` |

> Nota: los identificadores de servicio en Eureka (`Eventos`, `Reservas`, `Tickets`) usan mayúscula inicial, a diferencia de los demás (`ms-recintos`, `ms-localidades`, `ms-artistas`), que van en minúscula. Esto es consistente con el nombre declarado en el `spring.application.name` de cada microservicio respectivo.

## Documentación Swagger

Cada microservicio expone su documentación OpenAPI/Swagger de forma local en su propio puerto:

| Microservicio | Swagger UI (local) |
|---|---|
| Autenticación | http://localhost:8081/swagger-ui.html |
| Localidades | http://localhost:8082/swagger-ui.html |
| Artistas | http://localhost:8083/swagger-ui.html |
| Recintos | http://localhost:8084/swagger-ui.html |
| Eventos | http://localhost:8085/swagger-ui.html |
| Tickets | http://localhost:8086/swagger-ui.html |
| Reservas | http://localhost:8087/swagger-ui.html |
| Promoción | http://localhost:8088/swagger-ui.html |
| Notificaciones | http://localhost:8089/swagger-ui.html |



## Instrucciones de ejecución local

**Requisitos previos:**
- Docker y Docker Compose instalados
- Java 21
- Maven

**Pasos:**

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Jesu233/New-StarT.git
   cd New-StarT
   ```

2. Compilar cada microservicio (generar el `.jar` en `target/`) desde la raíz de cada carpeta, por ejemplo:
   ```bash
   cd reservas
   mvn clean package -DskipTests
   cd ..
   ```
   Repetir este paso para cada microservicio (`Eureka`, `gateway`, `DFullS`, `artistas`, `localidades`, `recintos`, `eventos`, `tickets`, `reservas`, `promocion`, `notificaciones`).

3. Levantar todo el sistema con Docker Compose desde la raíz del proyecto:
   ```bash
   docker-compose up --build
   ```

4. Verificar que todos los servicios se registraron correctamente en Eureka:
   ```
   http://localhost:8761
   ```

5. Acceder al sistema a través del Gateway:
   ```
   http://localhost:8090
   ```


