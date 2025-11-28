# Sistema de Gestión de Citas Médicas (SGCM)

### Integrantes:
- **Joseph Alfredo Trujillo Dextre**
- **Abel Angelo Mancha Minaya**

## Descripción

El **Sistema de Gestión de Citas Médicas (SGCM)** es una plataforma web para la administración de citas médicas. Permite a los pacientes gestionar sus citas, desde la creación hasta la cancelación, garantizando una experiencia de usuario eficiente y segura mediante el uso de autenticación con tokens JWT.

---

## Flujo Básico de Eventos

| Paso | Actor    | Acción                                           | Sistema Responde                             |
|------|----------|--------------------------------------------------|----------------------------------------------|
| 1    | Paciente | Abre la aplicación → `http://localhost:8080/login.html` | Muestra formulario de login                 |
| 2    | Paciente | Ingresa usuario y contraseña → presiona “Ingresar” | Envía `POST /api/auth/login`                |
| 3    | Sistema  | Valida credenciales contra la BD → genera JWT   | Devuelve `{ "jwt": "eyJhbGci..." }`         |
| 4    | Frontend | Guarda token en `localStorage` y redirige a `citas.html` | Dashboard cargado                           |
| 5    | Paciente | Selecciona una opción: Crear / Listar / Modificar / Cancelar cita | Llama al endpoint correspondiente con `Authorization: Bearer` |
| 6    | Sistema  | Valida token → ejecuta operación → persiste en MySQL | Devuelve `200 OK` + datos actualizados      |
| 7    | Frontend | Actualiza la interfaz sin recargar página       | Mensaje de éxito/toast                      |
| 8    | Paciente | Pulsa “Cerrar sesión”                           | Elimina token → redirige a login            |

---

## Flujos Alternativos

| ID   | Condición                                    | Desviación del flujo básico              | Resultado                                   |
|------|----------------------------------------------|------------------------------------------|--------------------------------------------|
| A1   | Credenciales incorrectas                     | Paso 3 → devuelve `401 Unauthorized`     | Mensaje: “Usuario o contraseña incorrectos”|
| A2   | Token expirado o inválido                    | Cualquier petición protegida → `403 Forbidden` | Redirecciona automáticamente a login |
| A3   | Horario ya ocupado al crear cita             | Paso 6 → validación de solapamiento de fecha/hora | Mensaje: “El horario ya está ocupado” |
| A4   | Intento de modificar/cancelar cita ajena     | Paso 5 → el paciente solo puede tocar sus propias citas (filtro por ID) | `403 Forbidden` – “No autorizado” |
| A5   | Conexión perdida o error del servidor        | Cualquier petición → catch en fetch      | Toast: “Error de conexión. Intente más tarde” |

---

## Códigos Requeridos

### 1. **Base de Datos (MySQL)**

```sql
CREATE DATABASE sgcm_db;
USE sgcm_db;

-- Consultar todos los usuarios
SELECT * FROM usuarios;

-- Consultar todas las citas
SELECT * FROM citas;

POST (REGISTRAR)
URL: http://localhost:9090/api/auth/register
BODY (JSON):
{
  "username": "admin",
  "password": "123456"
}
TE DA UN TOKEN, ESE ES TU LLAVE DE SEGURIDAD PARA TODO LO DEMÁS.

POST (LOGIN)
URL: http://localhost:9090/api/auth/login
HEADERS:
Content-Type: application/json
BODY (JSON):
{
  "username": "admin",
  "password": "123456"
}

POST (CREAR CITA)
URL: http://localhost:9090/api/citas
HEADERS:
Authorization: Bearer EL_TOKEN_QUE_TE_DAN
Content-Type: application/json
BODY (JSON):
{
  "especialidad": "Dermatología",
  "fecha": "2025-11-22T10:30",
  "motivo": "Consulta de rutina"
}

PUT (MODIFICAR CITA)
URL: http://localhost:9090/api/citas/1
HEADERS:
Authorization: Bearer EL_TOKEN_QUE_TE_DAN
Content-Type: application/json
BODY (JSON):
{
  "especialidad": "Dermatología",
  "fecha": "2025-11-25T10:30",
  "motivo": "Cambio de turno"
}

DELETE (CANCELAR CITA)
URL: http://localhost:9090/api/citas/1![WhatsApp Image 2025-11-27 at 9 48 43 PM](https://github.com/user-attachments/assets/9474dd9e-a8f9-4933-8eac-e2c2c6c56090)

HEADERS:
Authorization: Bearer EL_TOKEN_QUE_TE_DAN
Content-Type: application/json

GET (VER TODAS LAS CITAS)
URL: http://localhost:9090/api/citas/mis

Authorization: Bearer EL_TOKEN_QUE_TE_DAN
Content-Type: application/json
