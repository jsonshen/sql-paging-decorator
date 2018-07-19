package org.shenjia.sqlpaging;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

public class LimitAndOffsetPagingDecoratorTest {

    @Test
    public void test_decorate() {
        SelectStatementProvider delegate = new SelectStatementProvider() {
            @Override
            public String getSelectStatement() {
                return "SELECT * FROM ACL_USER";
            }

            @Override
            public Map<String, Object> getParameters() {
                return new HashMap<>();
            }
        };
        int limit = 10;
        int offset = 100;
        String newSql = new LimitAndOffsetPagingDecorator().decorate(delegate, limit, offset).getSelectStatement();
        assertEquals(newSql, "SELECT * FROM ACL_USER LIMIT #{parameters.limit} OFFSET #{parameters.offset}");
    }
}
