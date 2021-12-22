package com.hardziyevich.app.dao.impl;

/**
 * Constant class for Sql request in database.
 */
final class SqlCommand {

    private SqlCommand() {
        throw new UnsupportedOperationException();
    }

    final static class Delete {
        private Delete() {
            throw new UnsupportedOperationException();
        }

        static final String DELETE_BY_ID = "DELETE from %s WHERE id=?";

    }

    final static class Insert {

        private Insert() {
            throw new UnsupportedOperationException();
        }

        static final String INSERT = """
                INSERT INTO %s(value, unit_measurement, %s, case_value, temperature)
                VALUES (?,?,?,
                        (SELECT id FROM %s WHERE name = ?),
                        (SELECT id FROM %s WHERE high_temp =? AND low_temp =?));""";
        static final String INSERT_SETTING_CAPACITORS = "voltage_rated";
        static final String INSERT_SETTING_RESISTORS = "power";

    }

    final static class Update {

        private Update() {
            throw new UnsupportedOperationException();
        }

        static final String UPDATE = """
                UPDATE %s
                SET
                """;
        static final String UPDATE_VALUE = "value=?";
        static final String UPDATE_UNIT = "unit_measurement=?";
        static final String UPDATE_VOLTAGE = "voltage_rated=?";
        static final String UPDATE_POWER = "power=?";
        static final String UPDATE_COMMA = ",";
        static final String UPDATE_WHERE_ID = " WHERE id=?";

    }

    final static class Select {

        private Select() {
            throw new UnsupportedOperationException();
        }

        public static final String SELECT = """
                SELECT e.id, e.value, e.unit_measurement, %s, cs.id , cs.name, cs.length_mm, cs.width_mm, ot.id, ot.low_temp, ot.high_temp
                FROM %s e
                         JOIN electronics.case_size cs on cs.id = e.case_value
                         JOIN electronics.operating_temperature ot on ot.id = e.temperature
                WHERE 
                """;
        static final String SELECT_ID = "e.id=?";
        static final String SELECT_VALUE = "e.value=?";
        static final String SELECT_UNIT = "e.unit_measurement=?";
        static final String SELECT_VOLTAGE_RATED = "e.voltage_rated=?";
        static final String SELECT_POWER = "e.power=?";
        static final String SELECT_CASE_NAME = "cs.name=?";
        static final String SELECT_AND = " AND ";
        static final String SELECT_SETTING_CAPACITORS = "e.voltage_rated";
        static final String SELECT_SETTING_RESISTORS = "e.power";

    }

    final static class Tables {

        private Tables() {
            throw new UnsupportedOperationException();
        }

        static final String TABLE_CAPACITORS = "electronics.capacitors";
        static final String TABLE_RESISTORS = "electronics.resistors";
        static final String TABLE_CASE = "electronics.case_size";
        static final String TABLE_TEMPERATURE = "electronics.operating_temperature";

    }

}
