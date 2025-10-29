-- ==========================================
-- ROLES
-- ==========================================
INSERT INTO role (name, description) VALUES ('Admin', 'Administrador de la plataforma');
INSERT INTO role (name, description) VALUES ('Trainer', 'Entrenador certificado');
INSERT INTO role (name, description) VALUES ('User', 'Usuario regular');

-- ==========================================
-- PERMISSIONS
-- ==========================================
INSERT INTO permission (name, description) VALUES ('CREAR_RUTINA', 'Permite crear rutinas personalizadas');
INSERT INTO permission (name, description) VALUES ('EDITAR_RUTINA', 'Permite modificar rutinas propias');
INSERT INTO permission (name, description) VALUES ('REGISTRAR_PROGRESO', 'Permite registrar avances de entrenamiento');
INSERT INTO permission (name, description) VALUES ('CONSULTAR_EVENTOS', 'Permite consultar los eventos y talleres disponibles');
INSERT INTO permission (name, description) VALUES ('ENVIAR_MENSAJE', 'Permite enviar mensajes a usuarios asignados');
INSERT INTO permission (name, description) VALUES ('CREAR_RUTINA_PREDEFINIDA', 'Permite subir rutinas prediseñadas');
INSERT INTO permission (name, description) VALUES ('GESTIONAR_ENTRENADORES', 'Permite administrar entrenadores');
INSERT INTO permission (name, description) VALUES ('GESTIONAR_EVENTOS', 'Permite administrar eventos y talleres');
INSERT INTO permission (name, description) VALUES ('GESTIONAR_EJERCICIOS', 'Permite administrar la base de datos de ejercicios');

-- ==========================================
-- ROLE_PERMISSION
-- ==========================================
-- Permisos del User
INSERT INTO role_permission (role_id, permission_id) VALUES (3, 1);
INSERT INTO role_permission (role_id, permission_id) VALUES (3, 2);
INSERT INTO role_permission (role_id, permission_id) VALUES (3, 3);
INSERT INTO role_permission (role_id, permission_id) VALUES (3, 4);

-- Permisos del Trainer
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 4);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 5);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 6);

-- Permisos del Admin
INSERT INTO role_permission (role_id, permission_id)
SELECT 1, id FROM permission;

-- ==========================================
-- USERS
-- ==========================================
INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('Laura Gómez', 'laura.gomez@icesi.edu.co', '$2a$10$9QuciCPq5653XQ6UPRXZg.2JJNcnlT0xf77qJElXvxs/CJ4vn.QbC', 1); -- admin123

INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('Carlos Pérez', 'carlos.perez@icesi.edu.co', '$2a$10$vCio5BX5mhoOZ95P3L7KFOGql6Jxk02681SzucsJPHfhx/VgAnjci', 2); -- trainer123

INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('María Torres', 'maria.torres@icesi.edu.co', '$2a$10$MkPykRXbi1wSnB8o9srD7u2x/aFiVdzk1YpVcer0B.Vp9f4r7R1P2', 2); -- trainer456

INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('Andrés Ríos', 'andres.rios@icesi.edu.co', '$2a$10$isF/7qudQBv.TrVsuype/.AcuW6Ixb1QMCbRyLKbeHbjlE26OCl1y', 3); -- user123

INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('Camila Díaz', 'camila.diaz@icesi.edu.co', '$2a$10$L5lkZ7188Z4Hv.yVPxMIOeZy0iLtWrgYABWz3uBII11.yfKakNvzS', 3); -- user456

INSERT INTO users (name, institutional_email, password, role_id)
VALUES ('Juan Martínez', 'juan.martinez@icesi.edu.co', '$2a$10$cq0QO4gQ8wL0hIWTEKPjGuG3Wy1180IaSlIS0SU0QFLWQOBrUO/Hm', 3); -- user789

-- ==========================================
-- ROUTINES
-- ==========================================
INSERT INTO routine (name, creation_date, certified)
VALUES ('Rutina de Fuerza Básica', CURRENT_TIMESTAMP, true);

INSERT INTO routine (name, creation_date, certified)
VALUES ('Rutina de Cardio Ligero', CURRENT_TIMESTAMP, false);

-- ==========================================
-- EXERCISES
-- ==========================================
INSERT INTO exercise (name, type, description, duration, difficulty, video_url)
VALUES ('Sentadillas', 'Fuerza', 'Ejercicio para piernas y glúteos', 10.0, 'Media', 'https://videos.ejemplo.com/sentadillas.mp4');

INSERT INTO exercise (name, type, description, duration, difficulty, video_url)
VALUES ('Plancha', 'Resistencia', 'Fortalece el core y los brazos', 5.0, 'Alta', 'https://videos.ejemplo.com/plancha.mp4');

INSERT INTO exercise (name, type, description, duration, difficulty, video_url)
VALUES ('Caminata rápida', 'Cardio', 'Ejercicio aeróbico suave', 20.0, 'Baja', 'https://videos.ejemplo.com/caminata.mp4');

-- ==========================================
-- ROUTINE_EXERCISES
-- ==========================================
INSERT INTO routine_exercise (sets, reps, time, exercise_id, routine_id)
VALUES (3, 15, NULL, 1, 1);

INSERT INTO routine_exercise (sets, reps, time, exercise_id, routine_id)
VALUES (3, NULL, 30, 2, 1);

INSERT INTO routine_exercise (sets, reps, time, exercise_id, routine_id)
VALUES (1, NULL, 20, 3, 2);

-- ==========================================
-- USER_TRAINER_ASSIGNMENTS
-- ==========================================
INSERT INTO user_trainer_assignment (assignment_date, status, trainer_id, user_id)
VALUES (CURRENT_TIMESTAMP, 'ACTIVE', 2, 4); -- Carlos -> Andrés

INSERT INTO user_trainer_assignment (assignment_date, status, trainer_id, user_id)
VALUES (CURRENT_TIMESTAMP, 'PENDING', 3, 5); -- María -> Camila

-- ==========================================
-- USER_ROUTINES
-- ==========================================
INSERT INTO user_routine (assignment_date, status, routine_id, user_id)
VALUES (CURRENT_TIMESTAMP, true, 1, 4);

INSERT INTO user_routine (assignment_date, status, routine_id, user_id)
VALUES (CURRENT_TIMESTAMP, false, 2, 5);

-- ==========================================
-- EXERCISE_PROGRESS
-- ==========================================
INSERT INTO exercise_progress (progress_date, sets_completed, reps_completed, time_completed, effort_level, user_id, routine_exercise_id)
VALUES (CURRENT_TIMESTAMP, 3, 15, NULL, 8, 4, 1);

INSERT INTO exercise_progress (progress_date, sets_completed, reps_completed, time_completed, effort_level, user_id, routine_exercise_id)
VALUES (CURRENT_TIMESTAMP, 1, NULL, 20, 5, 5, 3);

-- ==========================================
-- RECOMMENDATIONS
-- ==========================================
INSERT INTO recommendation (recommendation_date, content, status, progress_id, trainer_id)
VALUES (CURRENT_TIMESTAMP, 'Haz 5 repeticiones extra en plancha para mejorar fuerza de core', 'NEW', 1, 2); -- Carlos -> Andrés

INSERT INTO recommendation (recommendation_date, content, status, progress_id, trainer_id)
VALUES (CURRENT_TIMESTAMP, 'Aumenta el tiempo de caminata a 25 minutos', 'NEW', 2, 3); -- María -> Camila

-- ==========================================
-- MESSAGES
-- ==========================================
INSERT INTO message (content, send_date, user_id, trainer_id)
VALUES ('Hola Andrés, recuerda hidratarte antes del entrenamiento', CURRENT_TIMESTAMP, 4, 2); -- Carlos -> Andrés

INSERT INTO message (content, send_date, user_id, trainer_id)
VALUES ('Camila, no olvides hacer los estiramientos al final', CURRENT_TIMESTAMP, 5, 3); -- María -> Camila

INSERT INTO message (content, send_date, user_id, trainer_id)
VALUES ('Andrés, me gustó tu progreso de hoy!', CURRENT_TIMESTAMP, 4, 2); -- Mensaje de usuario a entrenador

-- ==========================================
-- NOTIFICATIONS
-- ==========================================
INSERT INTO notification (origin_type, origin_id, text, sent_date, read_flag, user_id)
VALUES ('RECOMMENDATION', 1, 'Tu entrenador Carlos ha dejado una nueva recomendación.', CURRENT_TIMESTAMP, FALSE, 4);

INSERT INTO notification (origin_type, origin_id, text, sent_date, read_flag, user_id)
VALUES ('MESSAGE', 1, 'Tienes un nuevo mensaje de tu entrenador Carlos.', CURRENT_TIMESTAMP, FALSE, 4);

INSERT INTO notification (origin_type, origin_id, text, sent_date, read_flag, user_id)
VALUES ('RECOMMENDATION', 2, 'Tu entrenador María ha dejado una nueva recomendación.', CURRENT_TIMESTAMP, FALSE, 5);

INSERT INTO notification (origin_type, origin_id, text, sent_date, read_flag, user_id)
VALUES ('MESSAGE', 2, 'Tienes un nuevo mensaje de tu entrenador María.', CURRENT_TIMESTAMP, FALSE, 5);
