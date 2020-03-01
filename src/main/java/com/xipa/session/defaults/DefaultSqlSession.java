package com.xipa.session.defaults;

import com.xipa.cursor.Cursor;
import com.xipa.executor.CachingExecutor;
import com.xipa.executor.Executor;
import com.xipa.executor.SimpleExecutor;
import com.xipa.mapping.MappedStatement;
import com.xipa.session.Configuration;
import com.xipa.session.ResultHandler;
import com.xipa.session.RowBounds;
import com.xipa.session.SqlSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;
    private final Executor executor;

    private final boolean autoCommit;
    private boolean dirty;
    private List<Cursor<?>> cursorList;

    public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
        this.configuration = configuration;
        this.executor = executor;
        this.dirty = false;
        this.autoCommit = autoCommit;
        //System.out.println("executor >>> " + executor);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) throws SQLException {
        List<T> result = selectList(statement, parameter);
        if (result != null && result.size() > 1) {
            throw new RuntimeException("select one but select more...");
        }
        return result == null? null : result.isEmpty() == true? null : result.get(0);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) throws SQLException {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return executor.query(mappedStatement, parameter, null, null, null, null);
    }

    @Override
    public int insert(String statement, Object parameter) throws SQLException {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return executor.update(mappedStatement, parameter);
    }

    @Override
    public int update(String statement, Object parameter) throws SQLException {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return executor.update(mappedStatement, parameter);
    }

    @Override
    public int delete(String statement, Object parameter) throws SQLException {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return executor.update(mappedStatement, parameter);
    }

    @Override
    public void commit(boolean force) {

    }

    @Override
    public void rollback(boolean force) {

    }

    @Override
    public void close() {

    }

    @Override
    public void clearCache() {

    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
