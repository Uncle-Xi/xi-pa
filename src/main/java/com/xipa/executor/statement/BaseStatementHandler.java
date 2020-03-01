package com.xipa.executor.statement;

import com.xipa.executor.Executor;
import com.xipa.executor.parameter.ParameterHandler;
import com.xipa.executor.resultset.ResultSetHandler;
import com.xipa.mapping.BoundSql;
import com.xipa.mapping.MappedStatement;
import com.xipa.session.Configuration;
import com.xipa.session.ResultHandler;
import com.xipa.session.RowBounds;

public abstract class BaseStatementHandler implements StatementHandler {

    protected final Configuration configuration;
    protected final ResultSetHandler resultSetHandler;
    protected final ParameterHandler parameterHandler;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;
    protected final BoundSql boundSql;
    protected final RowBounds rowBounds;


    protected BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        if (boundSql == null) { // issue #435, get the key before calculating the statement
            boundSql = mappedStatement.getBoundSql(parameterObject);
        }
        this.boundSql = boundSql;
        this.parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, rowBounds, parameterHandler, resultHandler, boundSql);
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }
}
