-- Verificar si el usuario admin ya existe
SELECT * FROM users WHERE username = 'admin';

-- Si no existe, crearlo
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy', 'ROLE_ADMIN');

-- Verificar que se creó correctamente
SELECT id, username, role FROM users WHERE username = 'admin';