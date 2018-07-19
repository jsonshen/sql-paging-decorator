package org.shenjia.sqlpaging;

import java.util.Map;

import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

public class PagingSelectStatementProvider implements
        SelectStatementProvider {

    private Map<String, Object> parameters;
    private String selectStatement;

    public PagingSelectStatementProvider(Map<String, Object> parameters,
            String selectStatement) {
        this.parameters = parameters;
        this.selectStatement = selectStatement;
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public String getSelectStatement() {
        return selectStatement;
    }

}
