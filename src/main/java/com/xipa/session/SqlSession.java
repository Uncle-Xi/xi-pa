package com.xipa.session;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface SqlSession extends Closeable {

    <T> T selectOne(String statement, Object parameter) throws SQLException;

    <E> List<E> selectList(String statement, Object parameter) throws SQLException;

    int insert(String statement, Object parameter) throws SQLException;

    int update(String statement, Object parameter) throws SQLException;

    int delete(String statement, Object parameter) throws SQLException;

    void commit(boolean force);

    void rollback(boolean force);

    @Override
    void close();

    void clearCache();

    Configuration getConfiguration();

    <T> T getMapper(Class<T> type);

    Connection getConnection();
}
