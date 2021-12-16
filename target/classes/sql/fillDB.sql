INSERT INTO operating_temperature(low_temp, high_temp)
VALUES ('-65°C', '+175°C'),
       ('-55°C', '+105°C'),
       ('-55°C', '+125°C'),
       ('-55°C', '+175°C'),
       ('-25°C', '+85°C'),
       ('-25°C', '+125°C');

INSERT INTO case_size(name, length_mm, width_mm)
VALUES ('0402', 0.4, 0.2),
       ('0603', 0.6, 0.3),
       ('0805', 0.8, 0.5),
       ('1210', 1.2, 1.0);

INSERT INTO resistors(value, unit_measurement, power, case_value, temperature)
VALUES (50, 'Ohm', '0.1W', 2, 2),
       (1000, 'Ohm', '0.1W', 2, 2),
       (120, 'Ohm', '0.25W', 3, 3),
       (1, 'kOhm', '0.1W', 3, 4),
       (10, 'kOhm', '0.1W', 2, 2);

INSERT INTO capacitors(value, unit_measurement, voltage_rated, case_value, temperature)
VALUES (10, 'pF', '25V', 1, 1),
       (22, 'pF', '50V', 1, 4),
       (1, 'uF', '25V', 1, 1),
       (100, 'pF', '50V', 2, 1),
       (120, 'pF', '25V', 2, 1),
       (10, 'uF', '25V', 1, 1);

INSERT INTO capacitors(value, unit_measurement, voltage_rated, case_value, temperature)
VALUES (123, 'pF', '25V',
        (SELECT id FROM case_size WHERE name = '0402'),
        (SELECT id FROM operating_temperature WHERE low_temp = '-65°C' AND high_temp = '+175°C'));

UPDATE capacitors
SET value      = 15,
    case_value = (SELECT id FROM case_size WHERE name = '0805')
WHERE id = 6;

SELECT c.id, c.value, c.unit_measurement, c.voltage_rated,cs.id, cs.name, cs.length_mm, cs.width_mm, ot.id, ot.low_temp, ot.high_temp
FROM capacitors c
         JOIN case_size cs on cs.id = c.case_value
         JOIN operating_temperature ot on ot.id = c.temperature
WHERE c.id = 7;