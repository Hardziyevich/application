package com.hardziyevich.app.dao.impl;

import com.hardziyevich.app.dao.CaseDao;
import com.hardziyevich.app.entity.CaseSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.function.Predicate;

import static com.hardziyevich.app.db.ConnectionPool.*;

public class CaseDaoImpl implements CaseDao {

    private static final Logger log = LoggerFactory.getLogger(CaseDaoImpl.class);

    private static final String INSERT_CASE = "INSERT INTO electronics.case_size(name_inch, length_mm, width_mm) VALUES (?,?,?)";
    private static final String DELETE_CASE = "DELETE from electronics.case_size WHERE id=?";


    @Override
    public long create(CaseSize caseSize) {
        long result = 0;
        try (Connection connection = INSTANCE.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CASE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, caseSize.nameInch());
            preparedStatement.setObject(2, caseSize.lengthMm());
            preparedStatement.setObject(3, caseSize.widthMm());
            int i = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet != null && resultSet.next()) {
                result = resultSet.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public CaseSize update(CaseSize caseSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(CaseSize caseSize) {
        try (Connection connection = INSTANCE.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CASE)) {
            preparedStatement.setLong(1,caseSize.id());
            int number = preparedStatement.executeUpdate();
            System.out.println(number);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CaseSize findById(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<CaseSize> find(Predicate<CaseSize> tPredicate) {
        throw new UnsupportedOperationException();
    }
}
