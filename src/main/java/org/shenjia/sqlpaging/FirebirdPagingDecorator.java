package org.shenjia.sqlpaging;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

public class FirebirdPagingDecorator implements
        PagingDecorator {

    @Override
    public SelectStatementProvider decorate(SelectStatementProvider delegate,
        int limit,
        int offset) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.putAll(delegate.getParameters());
        parameters.put("limit", limit);
        parameters.put("offset", offset);

        String oldSql = delegate.getSelectStatement();
        StringBuilder sqlBuf = new StringBuilder(oldSql.length() + 77);
        sqlBuf.append("SELECT SKIP #{parameters.offset} FIRST #{parameters.limit} * FROM (");
        sqlBuf.append(oldSql);
        sqlBuf.append(") TMP_PAGE");
        String newSql = sqlBuf.toString();

        return new PagingSelectStatementProvider(parameters, newSql);
    }

}
