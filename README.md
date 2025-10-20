# 🐾 Virtual Pet API & Dashboard

[![Java](https://img.shields.io/badge/Java-21-red)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/Frontend-React%20%2B%20Vite-blue)](https://vitejs.dev/)
[![JWT](https://img.shields.io/badge/Security-JWT-orange)](https://jwt.io/)
[![Cache](https://img.shields.io/badge/Performance-Spring%20Cache-yellow)](https://spring.io/guides/gs/caching/)
![License](https://img.shields.io/badge/License-None-lightgrey)

---

## 📌 Descripción del Proyecto

**Virtual Pet API & Dashboard** es una aplicación *full-stack* que simula una mascota virtual.

- **Backend (Spring Boot):** lógica de negocio, persistencia y autenticación.
- **Frontend (React + Vite):** interfaz interactiva para visualizar y gestionar la mascota.

---

## 🛠️ Stack Tecnológico

| Componente | Capa | Tecnología / Versión | Notas |
|------------|------|---------------------|-------|
| Backend | API | Java 21, Spring Boot 3.5.5 | Seguridad JWT, Spring Cache |
| Frontend | UI | React, Vite | Desarrollo moderno y rápido |
| Persistencia | API | Spring Data JPA / Hibernate | Manejo de la base de datos |
| Seguridad | API | Spring Security + JWT | Autenticación y Autorización por tokens |
| Logging | API | SLF4J / Logback | Sistema de registro configurable |

---

## 🚀 Estructura del Proyecto

El proyecto tiene dos componentes principales:

- **Backend:** dentro de `src/main`
- **Frontend:** carpeta raíz `frontend/` (o la que definas)

### 🌳 Estructura del Backend (`src/main/java/com/virtualpet/api`)

rc/main/java/com/virtualpet/api
├── config // Configuración general (OpenAPI)
├── controller // Endpoints de la API (Auth, Pet, Admin, User)
├── dto // Request / Response
├── model // Entidades (User, Pet, Role)
├── repository // Repositorios JPA
├── security // JWT, Filtros, Configuración de Seguridad
└── service // Lógica de negocio


---

## 💻 Frontend (React + Vite)

### ✨ Características

- **PetDashboard:** ver, alimentar y jugar con la mascota.
- **Axios Interceptors:** inyección automática del token JWT.
- **Hot Reload con Vite:** arranque rapidísimo ⚡

### 📦 Dependencias Clave

react, react-dom
vite
axios
react-router-dom


---

## ⚙️ Configuración y Ejecución

### ✅ Prerrequisitos

- **Backend:** JDK 21
- **Frontend:** Node.js 18+ con `npm` o `yarn`
- **Base de datos:** configurar en `application.properties`

---

### ▶️ Paso 1: Iniciar Backend

```bash
./mvnw spring-boot:run

URL por defecto: http://localhost:8080

cd frontend/
npm install     # o yarn install
npm run dev     # o yarn dev
URL por defecto: http://localhost:5173