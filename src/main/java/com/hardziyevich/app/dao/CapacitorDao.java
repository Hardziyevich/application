package com.hardziyevich.app.dao;

import com.hardziyevich.app.controller.Request;

public interface CapacitorDao {
    boolean create(Request request);
    boolean delete(long id);
    boolean update(Request build);
}
