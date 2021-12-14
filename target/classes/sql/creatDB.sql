CREATE SCHEMA component;

CREATE TABLE IF NOT EXISTS operating_temperature
(
    id        SERIAL PRIMARY KEY,
    low_temp  VARCHAR(128) NOT NULL,
    high_temp VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS case_size
(
    id        SERIAL PRIMARY KEY,
    name_inch VARCHAR(128)  NOT NULL,
    length_mm NUMERIC(3, 1) NOT NULL,
    width_mm  NUMERIC(3, 1) NOT NULL
);

CREATE TABLE IF NOT EXISTS resistors
(
    id               SERIAL PRIMARY KEY,
    value            INTEGER      NOT NULL,
    init_measurement VARCHAR(128) NOT NULL CHECK ( init_measurement = 'Ohm' OR init_measurement = 'kOhm'),
    power            VARCHAR(128) NOT NULL,
    case_resistor    INTEGER REFERENCES case_size (id) ON DELETE CASCADE,
    temperature      INTEGER REFERENCES operating_temperature (id)
);

CREATE TABLE IF NOT EXISTS capacitors
(
    id               SERIAL PRIMARY KEY,
    value            INTEGER      NOT NULL,
    init_measurement VARCHAR(128) NOT NULL CHECK ( init_measurement = 'uF' OR init_measurement = 'pF'),
    voltage_rated    varchar(128) NOT NULL,
    case_capacitor   INTEGER REFERENCES case_size (id),
    temperature      INTEGER REFERENCES operating_temperature (id)
);

DROP TABLE resistors;
DROP TABLE capacitors;
DROP TABLE case_size;
DROP TABLE operating_temperature;
