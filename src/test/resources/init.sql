CREATE SCHEMA electronics;

CREATE TABLE IF NOT EXISTS electronics.operating_temperature
(
    id        SERIAL PRIMARY KEY,
    low_temp  VARCHAR(128) NOT NULL,
    high_temp VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS electronics.case_size
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(128)  NOT NULL,
    length_mm NUMERIC(3, 1) NOT NULL,
    width_mm  NUMERIC(3, 1) NOT NULL
);

CREATE TABLE IF NOT EXISTS electronics.resistors
(
    id               SERIAL PRIMARY KEY,
    value            INTEGER      NOT NULL,
    unit_measurement VARCHAR(128) NOT NULL CHECK ( unit_measurement = 'Ohm' OR unit_measurement = 'kOhm'),
    power            VARCHAR(128) NOT NULL,
    case_value       INTEGER REFERENCES electronics.case_size (id) ON DELETE CASCADE,
    temperature      INTEGER REFERENCES electronics.operating_temperature (id)
);

CREATE TABLE IF NOT EXISTS electronics.capacitors
(
    id               SERIAL PRIMARY KEY,
    value            INTEGER      NOT NULL,
    unit_measurement VARCHAR(128) NOT NULL CHECK ( unit_measurement = 'uF' OR unit_measurement = 'pF'),
    voltage_rated    varchar(128) NOT NULL,
    case_value       INTEGER REFERENCES electronics.case_size (id),
    temperature      INTEGER REFERENCES electronics.operating_temperature (id)
);

INSERT INTO electronics.operating_temperature(low_temp, high_temp)
VALUES ('-65°C', '+175°C'),
       ('-55°C', '+105°C'),
       ('-55°C', '+125°C'),
       ('-55°C', '+175°C'),
       ('-25°C', '+85°C'),
       ('-25°C', '+125°C');

INSERT INTO electronics.case_size(name, length_mm, width_mm)
VALUES ('0402', 0.4, 0.2),
       ('0603', 0.6, 0.3),
       ('0805', 0.8, 0.5),
       ('1210', 1.2, 1.0);

INSERT INTO electronics.resistors(value, unit_measurement, power, case_value, temperature)
VALUES (50, 'Ohm', '0.1W', 1, 2),
       (1000, 'Ohm', '0.1W', 2, 2),
       (120, 'Ohm', '0.25W', 3, 3),
       (1, 'kOhm', '0.1W', 2, 2),
       (10, 'kOhm', '0.1W', 2, 2);

INSERT INTO electronics.capacitors(value, unit_measurement, voltage_rated, case_value, temperature)
VALUES (10, 'pF', '25V', 1, 1),
       (22, 'pF', '50V', 1, 4),
       (1, 'uF', '25V', 1, 1),
       (100, 'pF', '50V', 2, 1),
       (120, 'pF', '25V', 2, 1),
       (10, 'uF', '6.3V', 1, 1),
       (150, 'pF', '100V', 4, 6),
       (200, 'pF', '100V', 4, 6),
       (1200, 'pF', '120V', 3, 6);