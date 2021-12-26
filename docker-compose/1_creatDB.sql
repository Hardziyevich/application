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