package com.hardziyevich.app.dao;

import com.hardziyevich.app.entity.CaseSize;

import java.util.List;
import java.util.function.Predicate;

public interface CaseDao {
    long create(CaseSize caseSize);
    CaseSize update(CaseSize caseSize);
    void delete(CaseSize caseSize);
    CaseSize findById(int id);
    List<CaseSize> find(Predicate<CaseSize> tPredicate);
}
