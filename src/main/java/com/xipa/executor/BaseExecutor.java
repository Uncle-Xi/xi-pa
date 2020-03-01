package com.xipa.executor;


import com.xipa.cache.impl.PerpetualCache;
import com.xipa.mapping.BoundSql;
import com.xipa.mapping.MappedStatement;
import com.xipa.session.Configuration;
import com.xipa.session.ResultHandler;
import com.xipa.session.RowBounds;
import com.xipa.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;


public abstract class BaseExecutor implements Executor {

    protected Configuration configuration;
    protected Transaction transaction;
    protected Executor wrapper;
    protected PerpetualCache localCache;

    protected BaseExecutor(Configuration configuration, Transaction transaction) {
        this.transaction = transaction;
        this.configuration = configuration;
        this.wrapper = this;
    }

    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        return this.doUpdate(ms, parameter);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, String cacheKey, BoundSql boundSql) throws SQLException {
        return this.queryFromDatabase(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
    }

    private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler,
                                          String key, BoundSql boundSql) throws SQLException {
        // TODO 一级缓存 localCache.putObject(key, EXECUTION_PLACEHOLDER);
        List<E> list;
        try {
            list = doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
        } finally {
            //localCache.removeObject(key);
        }
        //localCache.putObject(key, list);
        return list;
    }

    protected abstract int doUpdate(MappedStatement ms, Object parameter)
            throws SQLException;

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds,
                                           ResultHandler resultHandler, BoundSql boundSql)
            throws SQLException;

}
