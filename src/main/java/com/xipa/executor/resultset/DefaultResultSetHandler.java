package com.xipa.executor.resultset;

import com.xipa.cursor.Cursor;
import com.xipa.executor.Executor;
import com.xipa.mapping.MappedStatement;
import com.xipa.session.Configuration;
import com.xipa.session.ResultHandler;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DefaultResultSetHandler
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class DefaultResultSetHandler implements ResultSetHandler {

    private final Executor executor;
    private final Configuration configuration;
    private final MappedStatement mappedStatement;
    private final ResultHandler<?> resultHandler;

    public DefaultResultSetHandler(Executor executor, Configuration configuration, MappedStatement mappedStatement, ResultHandler<?> resultHandler) {
        this.executor = executor;
        this.configuration = configuration;
        this.mappedStatement = mappedStatement;
        this.resultHandler = resultHandler;
    }

    @Override
    public <E> List<E> handleResultSets(Statement stmt) {
        List<E> results = new ArrayList<>();
        try {
            Class<?> resultTypeClass = mappedStatement.getResultType();
            ResultSet resultSet = stmt.getResultSet();
            while (resultSet.next()) {
                Object result = resultTypeClass.newInstance();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    Field field = resultTypeClass.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(result, resultSet.getObject(i + 1));
                }
                results.add((E)result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public void handleOutputParameters(CallableStatement cs) throws SQLException {

    }
}
