package com.xipa.transaction;

import com.xipa.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public interface TransactionFactory {

    
    void setProperties(Properties props);

    
    Transaction newTransaction(Connection conn);

    
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);

}