package org.shenjia.sqlpaging;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

public class OraclePagingDecorator implements
        PagingDecorator {

    @Override
    public SelectStatementProvider decorate(SelectStatementProvider delegate,
        int limit,
        int offset) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.putAll(delegate.getParameters());
        parameters.put("start_row", offset);
        parameters.put("end_row", offset + limit);

        String oldSql = delegate.getSelectStatement();
        StringBuilder sqlBuf = new StringBuilder(oldSql.length() + 143);
        sqlBuf.append("SELECT * FROM (");
        sqlBuf.append("SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM (");
        sqlBuf.append(oldSql);
        sqlBuf.append(") TMP_PAGE WHERE ROWNUM <= #{parameters.end_row}");
        sqlBuf.append(") WHERE ROW_ID > #{parameters.start_row}");
        String newSql = sqlBuf.toString();

        return new PagingSelectStatementProvider(parameters, newSql);
    }

}
