package com.xipa.executor;

import com.xipa.executor.statement.StatementHandler;
import com.xipa.mapping.BoundSql;
import com.xipa.mapping.MappedStatement;
import com.xipa.session.Configuration;
import com.xipa.session.ResultHandler;
import com.xipa.session.RowBounds;
import com.xipa.transaction.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor extends BaseExecutor {

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
//        Statement stmt = null;
//        try {
//            Configuration configuration = ms.getConfiguration();
//            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null, null);
//            stmt = prepareStatement(handler, ms.getStatementLog());
//            return handler.update(stmt);
//        } finally {
//            closeStatement(stmt);
//        }
        StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, null, null, null);
        return handler.update(ms);
    }

    @Override
    public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
//        Statement stmt = null;
//        try {
//            Configuration configuration = ms.getConfiguration();
//            StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter, rowBounds, resultHandler, boundSql);
//            stmt = prepareStatement(handler, ms.getStatementLog());
//            return handler.query(stmt, resultHandler);
//        } finally {
//            closeStatement(stmt);
//        }
        StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, rowBounds, resultHandler, boundSql);
        return handler.query(ms, resultHandler);
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
