package com.xipa.datasource.jndi;

import com.xipa.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @description: JndiDataSourceFactory
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class JndiDataSourceFactory implements DataSourceFactory {

    private DataSource dataSource;

    @Override
    public void setProperties(Properties props) {

    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
