package org.shenjia.sqlpaging;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

/**
 * SQLServer 2012 or later
 * 
 * @author jason
 *
 */
public class SQLServerPagingDecorator implements
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
        StringBuilder sqlBuf = new StringBuilder(oldSql.length() + 74);
        sqlBuf.append(oldSql);
        sqlBuf.append(" OFFSET #{parameters.offset} ROWS FETCH NEXT #{parameters.limit} ROWS ONLY");
        String newSql = sqlBuf.toString();

        return new PagingSelectStatementProvider(parameters, newSql);
    }

}
