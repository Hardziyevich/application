package com.hardziyevich.app.dao;

import com.hardziyevich.app.entity.Capacitors;
import com.hardziyevich.app.service.dto.CreateCapacitorDto;
import com.hardziyevich.app.service.dto.UpdateCapacitorDto;

import java.util.List;

public interface CapacitorDao {

    boolean create(CreateCapacitorDto request);

    boolean delete(long id);

    boolean update(UpdateCapacitorDto request);

    List<Capacitors> search(JdbcSpecification jdbcSpecification);

}
