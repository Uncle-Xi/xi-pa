package com.xipa.mapping;

import com.xipa.cache.Cache;
import com.xipa.session.Configuration;
import sun.rmi.runtime.Log;

import javax.crypto.KeyGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: MappedStatement
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public final class MappedStatement {

    private Configuration configuration;
    private String id; // sql Id
    private Class<?> parameterType;
    private Class<?> resultType;
    private SqlSource sqlSource;
    private String sqlCommandType;  // TODO select|insert|update|delete
    private boolean useCache;
    private String statementType;

    public Configuration getConfiguration() {
        return configuration;
    }

    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlSource sqlSource, String sqlCommandType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlSource = sqlSource;
            mappedStatement.sqlCommandType = sqlCommandType;
        }
        public Builder parameterType(Class<?> parameterType) {
            mappedStatement.parameterType = parameterType;
            return this;
        }
        public String id() {
            return mappedStatement.id;
        }

        public Builder resultType(Class<?> resultType) {
            mappedStatement.resultType = resultType;
            return this;
        }

        public Builder statementType(String statementType) {
            mappedStatement.statementType = statementType;
            return this;
        }

        public Builder useCache(boolean useCache) {
            mappedStatement.useCache = useCache;
            return this;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            assert mappedStatement.sqlSource != null;
            return mappedStatement;
        }
    }

    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public void setSqlSource(SqlSource sqlSource) {
        this.sqlSource = sqlSource;
    }

    public String getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(String sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    @Override
    public String toString() {
        return "MappedStatement{" +
                "configuration=" + configuration +
                ", id='" + id + '\'' +
                ", parameterType=" + parameterType +
                ", resultType=" + resultType +
                ", sqlSource=" + sqlSource +
                ", sqlCommandType='" + sqlCommandType + '\'' +
                ", useCache=" + useCache +
                ", statementType='" + statementType + '\'' +
                '}';
    }
}
