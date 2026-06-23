# BFF Service — Libro Digital

Backend for Frontend (BFF) que actúa como punto de entrada único para el frontend React.
Se encarga de agregar datos de los microservicios internos, calcular métricas como
porcentaje de asistencia y promedio de notas, y exponer endpoints optimizados para el cliente.

## Tecnologías

- Java 21
- Spring Boot 3.3
- Spring Security + OAuth2 (Auth0)
- RestTemplate
- Springdoc OpenAPI (Swagger)

## Requisitos previos

- Java 21 instalado
- Maven instalado (o usar el wrapper `./mvnw`)
- Los microservicios `usuarios-service`, `academico-service` y `asistencia-notificacion-service` corriendo

## Instalación y ejecución

```bash
# Clonar el repositorio
git clone https://github.com/metamistyk/libro-digital-bff.git
cd bff-service

# Ejecutar
./mvnw spring-boot:run
```

El servicio quedará disponible en `http://localhost:8080`

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/bff/estudiantes` | Lista todos los estudiantes |
| GET | `/api/v1/bff/estudiantes/{id}/resumen` | Resumen completo de un estudiante |
| GET | `/api/v1/bff/ranking/estudiantes` | Ranking ordenado por promedio de notas |

Todos los endpoints requieren token JWT válido de Auth0 en el header `Authorization`.

## Documentación Swagger

Con el servicio corriendo, accede a: