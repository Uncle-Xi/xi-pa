package com.xipa.transaction.jdbc;

import com.sun.org.apache.regexp.internal.RE;
import com.xipa.session.Configuration;
import com.xipa.session.TransactionIsolationLevel;
import com.xipa.transaction.Transaction;
import sun.rmi.runtime.Log;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @description: JdbcTransaction
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class JdbcTransaction implements Transaction {

    private static final Logger log = Logger.getLogger(JdbcTransaction.class.toString());

    protected Connection connection;
    protected DataSource dataSource;
    protected TransactionIsolationLevel level;
    protected boolean autoCommit;
    private static final String OPEN_TRANSACTION_KEY = "OPEN_TRANSACTION_KEY";
    private ThreadLocal<Map<String, Boolean>> openTransactionMap = new ThreadLocal<>();

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    public JdbcTransaction(DataSource ds, TransactionIsolationLevel desiredLevel, boolean desiredAutoCommit) {
        dataSource = ds;
        level = desiredLevel;
        autoCommit = desiredAutoCommit;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                if (isOpenTransaction()) {
                    connection.setAutoCommit(false);
                }
            } else {
                log.warning("connection != null");
            }
        }
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        if (isOpenTransaction()) {
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (isOpenTransaction()) {
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException { }

    @Override
    public Integer getTimeout() throws SQLException {
        return null;
    }

    private boolean isOpenTransaction(){
        Map<String, Boolean> autoCommit = openTransactionMap.get();
        if (autoCommit == null) {
            return false;
        }
        Boolean isOpen = autoCommit.get(OPEN_TRANSACTION_KEY);
        if (isOpen == null || !isOpen) {
            return false;
        }
        return true;
    }
}
