package com.xipa.session;

import com.xipa.builder.xml.XMLConfigBuilder;
import com.xipa.exceptions.ExceptionFactory;
import com.xipa.executor.ErrorContext;
import com.xipa.session.defaults.DefaultSqlSessionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * @description: SqlSessionFactoryBuilder
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream inputStream) {
        return build(inputStream, null, null);
    }

    /**
     * 初始化 XMLConfigBuilder
     * 解析 XML => Config  Mapper
     * @param inputStream
     * @param environment
     * @param properties
     * @return
     */
    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
        try {
            XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
            return build(parser.parse());
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error building SqlSession.", e);
        } finally {
            ErrorContext.instance().reset();
            try {
                inputStream.close();
            } catch (IOException e) {
                // Intentionally ignore. Prefer previous error.
            }
        }
    }

    /**
     * 构建 SqlSessionFactory
     * @param configuration
     * @return
     */
    public SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }
}
