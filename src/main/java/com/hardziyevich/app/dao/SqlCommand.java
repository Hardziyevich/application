package com.hardziyevich.app.dao;

import static com.hardziyevich.app.dao.SqlCommand.Tables.*;

public final class SqlCommand {

    public final static class Delete {
        public static final String DELETE_BY_ID = "DELETE from %s WHERE id=?";
    }

    public final static class Insert {
        public static final String INSERT = """
                INSERT INTO %s(value, unit_measurement, %s, case_value, temperature)
                VALUES (?,?,?,
                        (SELECT id FROM %s WHERE name = ?),
                        (SELECT id FROM %s WHERE high_temp =? AND low_temp =?));""";
    }

    public final static class Tables {
        public static final String TABLE_CAPACITORS = "electronics.capacitors";
        public static final String TABLE_CASE = "electronics.case_size";
        public static final String TABLE_TEMPERATURE = "electronics.operating_temperature";

        public static final String SETTING_CAPACITORS = "voltage_rated";
    }


}
