INSERT INTO operating_temperature(low_temp, high_temp)
VALUES ('-65°C','+175°C'),
       ('-55°C','+105°C'),
       ('-55°C','+125°C'),
       ('-55°C','+175°C'),
       ('-25°C','+85°C'),
       ('-25°C','+125°C');

INSERT INTO case_size(name_inch, length_mm, width_mm)
VALUES ('0402',0.4,0.2),
       ('0603',0.6,0.3),
       ('0805',0.8,0.5),
       ('1210',1.2,1.0);

INSERT INTO resistors(value, init_measurement, power, case_resistor, temperature)
VALUES (50,'Ohm','0.1W',2,2),
       (1000,'Ohm','0.1W',2,2),
       (120,'Ohm','0.25W',3,3),
       (1,'kOhm','0.1W',3,4),
       (10,'kOhm','0.1W',2,2);

INSERT INTO capacitors(value, init_measurement, voltage_rated, case_capacitor, temperature)
VALUES (10,'pF','25V',1,1),
       (22,'pF','50V',1,4),
       (1,'uF','25V',1,1),
       (100,'pF','50V',2,1),
       (120,'pF','25V',2,1),
       (10,'uF','25V',1,1);