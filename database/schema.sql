CREATE DATABASE IF NOT EXISTS reservas_db;
USE reservas_db;

INSERT INTO roles (nombre) VALUES ('ADMIN'), ('CLIENTE');

INSERT INTO servicios (nombre, duracion_minutos, precio) VALUES
('Corte de cabello', 30, 15.00),
('Consulta general', 45, 25.00),
('Manicure', 40, 12.00);

INSERT INTO recursos (nombre, descripcion) VALUES
('Estilista Juan', 'Especialista en cortes'),
('Consultorio 1', 'Sala principal'),
('Estilista Ana', 'Especialista en color');
