package com.hardziyevich.app.dao;

import com.hardziyevich.app.controller.Request;
import com.hardziyevich.app.entity.Capacitors;

import java.util.List;

public interface CapacitorDao {
    boolean create(Request request);
    boolean delete(long id);
    boolean update(Request request);
    List<Capacitors> search(Request request);
}
