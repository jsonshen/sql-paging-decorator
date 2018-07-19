package org.shenjia.sqlpaging;

import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

public interface PagingDecorator {

    SelectStatementProvider decorate(SelectStatementProvider delegate,
        int limit,
        int offset);
}
