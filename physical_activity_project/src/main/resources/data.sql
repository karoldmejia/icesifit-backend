-- ROLES
INSERT INTO ROL (name, description) VALUES ('Admin', 'Administrador de la plataforma');
INSERT INTO ROL (name, description) VALUES ('Trainer', 'Entrenador certificado');
INSERT INTO ROL (name, description) VALUES ('User', 'Usuario regular');

-- PERMISSIONS
INSERT INTO PERMISO (name, description) VALUES ('CREAR_RUTINA', 'Permite crear rutinas personalizadas');
INSERT INTO PERMISO (name, description) VALUES ('EDITAR_RUTINA', 'Permite modificar rutinas propias');
INSERT INTO PERMISO (name, description) VALUES ('REGISTRAR_PROGRESO', 'Permite registrar avances de entrenamiento');
INSERT INTO PERMISO (name, description) VALUES ('CONSULTAR_EVENTOS', 'Permite consultar los eventos y talleres disponibles');
INSERT INTO PERMISO (name, description) VALUES ('ENVIAR_MENSAJE', 'Permite enviar mensajes a usuarios asignados');
INSERT INTO PERMISO (name, description) VALUES ('CREAR_RUTINA_PREDEFINIDA', 'Permite subir rutinas prediseñadas');
INSERT INTO PERMISO (name, description) VALUES ('GESTIONAR_ENTRENADORES', 'Permite administrar entrenadores');
INSERT INTO PERMISO (name, description) VALUES ('GESTIONAR_EVENTOS', 'Permite administrar eventos y talleres');
INSERT INTO PERMISO (name, description) VALUES ('GESTIONAR_EJERCICIOS', 'Permite administrar la base de datos de ejercicios');

-- ROLE_PERMISSION

-- Permisos del User
INSERT INTO role_permission (role_id, permission_id) VALUES (3, 1); -- CREAR_RUTINA
INSERT INTO role_permission (role_id, permission_id) VALUES (3, 2); -- EDITAR_RUTINA
INSERT INTO role_permission (role_id, permission_id) VALUES (3, 3); -- REGISTRAR_PROGRESO
INSERT INTO role_permission (role_id, permission_id) VALUES (3, 4); -- CONSULTAR_EVENTOS

-- Permisos del Trainer
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 4); -- CONSULTAR_EVENTOS
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 5); -- ENVIAR_MENSAJE
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 6); -- CREAR_RUTINA_PREDEFINIDA

-- Permisos del Admin
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 7); -- GESTIONAR_ENTRENADORES
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 8); -- GESTIONAR_EVENTOS
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 9); -- GESTIONAR_EJERCICIOS
-- darle todos los permisos al Admin
INSERT INTO role_permission (role_id, permission_id)
SELECT 1, id FROM PERMISO;

-- ADMIN
INSERT INTO USUARIO (name, institutional_email, password, role_id)
VALUES ('Laura Gómez', 'laura.gomez@icesi.edu.co', '$2a$10$9QuciCPq5653XQ6UPRXZg.2JJNcnlT0xf77qJElXvxs/CJ4vn.QbC', 1); -- admin123

-- TRAINERS
INSERT INTO USUARIO (name, institutional_email, password, role_id)
VALUES ('Carlos Pérez', 'carlos.perez@icesi.edu.co', '$2a$10$vCio5BX5mhoOZ95P3L7KFOGql6Jxk02681SzucsJPHfhx/VgAnjci', 2); -- trainer123

INSERT INTO USUARIO (name, institutional_email, password, role_id)
VALUES ('María Torres', 'maria.torres@icesi.edu.co', '$2a$10$MkPykRXbi1wSnB8o9srD7u2x/aFiVdzk1YpVcer0B.Vp9f4r7R1P2', 2); -- trainer456

-- STUDENTS
INSERT INTO USUARIO (name, institutional_email, password, role_id)
VALUES ('Andrés Ríos', 'andres.rios@icesi.edu.co', '$2a$10$isF/7qudQBv.TrVsuype/.AcuW6Ixb1QMCbRyLKbeHbjlE26OCl1y', 3); -- user123

INSERT INTO USUARIO (name, institutional_email, password, role_id)
VALUES ('Camila Díaz', 'camila.diaz@icesi.edu.co', '$2a$10$L5lkZ7188Z4Hv.yVPxMIOeZy0iLtWrgYABWz3uBII11.yfKakNvzS', 3); -- user456

INSERT INTO USUARIO (name, institutional_email, password, role_id)
VALUES ('Juan Martínez', 'juan.martinez@icesi.edu.co', '$2a$10$cq0QO4gQ8wL0hIWTEKPjGuG3Wy1180IaSlIS0SU0QFLWQOBrUO/Hm', 3); --user789


