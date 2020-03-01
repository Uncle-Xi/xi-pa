package com.xipa.executor;

import com.xipa.mapping.BoundSql;
import com.xipa.mapping.MappedStatement;
import com.xipa.session.Configuration;
import com.xipa.session.ResultHandler;
import com.xipa.session.RowBounds;
import com.xipa.transaction.Transaction;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: ReuseExecutor
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ReuseExecutor extends BaseExecutor {


    private final Map<String, Statement> statementMap = new HashMap<>();

    public ReuseExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
        return 0;
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        return null;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        transaction.commit();
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        transaction.rollback();
    }

    @Override
    public void clearLocalCache() {

    }

    @Override
    public void close(boolean forceRollback) {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void setExecutorWrapper(Executor executor) {

    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
