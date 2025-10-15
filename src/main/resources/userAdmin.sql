-- Verificar si el usuario admin ya existe
SELECT * FROM users WHERE username = 'admin';

-- Si no existe, crearlo
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy', 'ROLE_ADMIN');

-- Verificar que se creó correctamente
SELECT id, username, role FROM users WHERE username = 'admin';

-- =============================================
-- 🗄️ GUÍA RÁPIDA MYSQL - COMANDOS ESENCIALES
-- =============================================

-- 1. CONECTARSE A MYSQL
-- Desde línea de comandos:
-- mysql -u root -p

-- 2. CREAR UNA BASE DE DATOS
CREATE DATABASE virtualpet_db;
-- Crear base de datos para la aplicación de mascotas virtuales

-- 3. USAR UNA BASE DE DATOS
USE virtualpet_db;
-- Selecciona la base de datos para trabajar

-- 4. VER TODAS LAS TABLAS DE LA BD
SHOW TABLES;
-- Muestra todas las tablas en la base de datos actual

-- 5. VER EL CONTENIDO DE UNA TABLA
-- Ver todos los registros de la tabla users
SELECT * FROM users;

-- Ver columnas específicas
SELECT id, username, role FROM users;

-- Ver con filtro (usuarios admin)
SELECT * FROM users WHERE role = 'ROLE_ADMIN';

-- 6. VER LA ESTRUCTURA DE UNA TABLA
DESCRIBE users;
-- Muestra las columnas, tipos de datos y propiedades de la tabla

-- 7. CAMBIAR EL ROLE DE UN USUARIO
-- Hacer admin a un usuario
UPDATE users SET role = 'ROLE_ADMIN' WHERE username = 'jose';

-- Volver a usuario normal
UPDATE users SET role = 'ROLE_USER' WHERE username = 'jose';

-- 8. BORRAR UN REGISTRO (FILA)
-- ⚠️ SIEMPRE usar WHERE para no borrar toda la tabla
DELETE FROM users WHERE id = 5;
-- Borra el usuario con ID 5

DELETE FROM users WHERE username = 'ana';
-- Borra el usuario llamado 'ana'

-- 9. CONSULTAS ÚTILES ADICIONALES

-- Ver cantidad de registros
SELECT COUNT(*) FROM users;

-- Ver usuarios ordenados por nombre
SELECT * FROM users ORDER BY username ASC;

-- Ver solo los primeros 5 registros
SELECT * FROM users LIMIT 5;

-- 10. COMANDOS PELIGROSOS - USAR CON CUIDADO
-- ❌ BORRA TODOS LOS REGISTROS
-- DELETE FROM users;

-- ❌ BORRA LA TABLA COMPLETA
-- DROP TABLE users;

-- =============================================
-- 🎯 EJEMPLOS PRÁCTICOS CON TU PROYECTO
-- =============================================

-- Ver todos los usuarios y sus roles
SELECT id, username, role FROM users ORDER BY id;

-- Cambiar a jose a admin
UPDATE users SET role = 'ROLE_ADMIN' WHERE username = 'jose';

-- Borrar usuario ana
DELETE FROM users WHERE username = 'ana';

-- Verificar cambios
SELECT * FROM users;

-- =============================================
-- 📊 CONSULTAS PARA ESTADÍSTICAS
-- =============================================

-- Contar usuarios por rol
SELECT role, COUNT(*) as total
FROM users
GROUP BY role;

-- Ver usuarios creados recientemente
SELECT username, created_at
FROM users
ORDER BY created_at DESC;

-- =============================================
-- 💾 PARA GUARDAR ESTE SCRIPT
-- =============================================
-- 1. Copia este contenido en un archivo .sql
-- 2. Guárdalo como 'guia_mysql_comandos.sql'
-- 3. Para ejecutar: mysql -u root -p < guia_mysql_comandos.sql
-- 4. O pégalo directamente en la consola MySQL