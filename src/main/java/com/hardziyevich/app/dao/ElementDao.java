package com.hardziyevich.app.dao;

import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;

/**
 * Represents Data access object for element table.
 */
import java.util.List;

public interface ElementDao<T> {

    /**
     * Create new string in resistors table.
     * @param request a createDto object which contain fields that need to fill for element table.
     * @return provides whether the request was successful
     */
    boolean create(CreateDto request);

    /**
     * Delete string in element table.
     * @param id a long is id in db, whose line should delete
     * @return provides whether the request was successful
     */
    boolean delete(long id);

    /**
     * Update string in element table.
     * @param request a createDto object which contain fields that need to update for element table.
     * @return provides whether the request was successful
     */
    boolean update(UpdateDto request);

    /**
     * Search strings in element table.
     * @param jdbcSpecification represent criteria for search element
     * @return a list of element which will be found
     */
    List<T> search(JdbcSpecification<T> jdbcSpecification);
}
