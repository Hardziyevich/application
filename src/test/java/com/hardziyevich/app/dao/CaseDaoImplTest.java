package com.hardziyevich.app.dao;

import com.hardziyevich.app.dao.impl.CaseDaoImpl;
import com.hardziyevich.app.entity.CaseSize;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static com.hardziyevich.app.db.ConnectionPool.INSTANCE;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CaseDaoImplTest {
    private static final CaseDaoImpl caseDao = new CaseDaoImpl();

    @AfterAll
    void close() {
        INSTANCE.destroyPool();
    }

    @Test
    void testInsert(){
        CaseSize caseSize = new CaseSize(0,"test",2.0,2.0);
        assertNotNull(caseDao.create(caseSize));
    }

    @Test
    void testDelete(){
        CaseSize caseSize = new CaseSize(25,"test",2.0,2.0);
        assertDoesNotThrow(()-> caseDao.delete(caseSize));
    }
}
