package com.hardziyevich.app.dao.impl;

import com.hardziyevich.app.dao.CapacitorDao;
import com.hardziyevich.app.controller.Request;

import java.sql.*;

import static com.hardziyevich.app.dao.SqlCommand.Delete.*;
import static com.hardziyevich.app.dao.SqlCommand.Insert.INSERT;
import static com.hardziyevich.app.dao.SqlCommand.Tables.*;
import static com.hardziyevich.app.db.ConnectionPool.INSTANCE;

public class CapacitorDaoImpl implements CapacitorDao {

    @Override
    public boolean create(Request request) {
        int result = 0;
        String command = INSERT.formatted(TABLE_CAPACITORS, SETTING_CAPACITORS, TABLE_CASE, TABLE_TEMPERATURE);
        try (Connection connection = INSTANCE.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.setObject(1, request.getValueCapacitor());
            preparedStatement.setObject(2, request.getUnit());
            preparedStatement.setObject(3, request.getVoltageRate());
            preparedStatement.setObject(4, request.getCaseSize());
            preparedStatement.setObject(5, request.getTempHigh());
            preparedStatement.setObject(6, request.getTempLow());
            preparedStatement.execute();
            result = preparedStatement.getUpdateCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    @Override
    public boolean delete(long id) {
        int result = 0;
        String delete = DELETE_BY_ID.formatted(TABLE_CAPACITORS);
        try (Connection connection = INSTANCE.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setLong(1, id);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    @Override
    public boolean update(Request build) {

        return false;
    }
}
