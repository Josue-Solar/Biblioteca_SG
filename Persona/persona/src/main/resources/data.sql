-- 1. Insertar el Sexo (ID 1)
INSERT INTO sexo (id, nombre) VALUES (1, 'FEMENINO');

-- 2. Insertar el Rol (ID 1)
INSERT INTO rol (id, nombre) VALUES (1, 'USUARIO');

-- 3. Insertar la Persona (Dejamos que el ID se genere solo)
INSERT INTO persona (pnombre, snombre, apellido_paterno, apellido_materno, num_run, dv_run, correo, comuna_id, sexo_id, rol_id) 
VALUES ('Ada', 'Augusta', 'Lovelace', 'Byron', '11222333', '4', 'ada@biblioteca.com', 1, 1, 1);