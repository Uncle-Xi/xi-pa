package com.xipa.session;

import com.xipa.binding.MapperProxyFactory;
import com.xipa.binding.MapperRegistry;
import com.xipa.builder.annotation.MapperAnnotationBuilder;
import com.xipa.datasource.pooled.PooledDataSourceFactory;
import com.xipa.executor.*;
import com.xipa.executor.parameter.ParameterHandler;
import com.xipa.executor.resultset.DefaultResultSetHandler;
import com.xipa.executor.resultset.ResultSetHandler;
import com.xipa.executor.statement.SimpleStatementHandler;
import com.xipa.executor.statement.StatementHandler;
import com.xipa.mapping.BoundSql;
import com.xipa.mapping.Environment;
import com.xipa.mapping.MappedStatement;
import com.xipa.parsing.XNode;
import com.xipa.plugin.Interceptor;
import com.xipa.plugin.InterceptorChain;
import com.xipa.reflection.MetaObject;
import com.xipa.scripting.LanguageDriver;
import com.xipa.scripting.LanguageDriverRegistry;
import com.xipa.scripting.defaults.DefaultParameterHandler;
import com.xipa.scripting.xmltags.XMLLanguageDriver;
import com.xipa.transaction.Transaction;
import com.xipa.type.TypeAliasRegistry;
import com.xipa.type.TypeHandlerRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @description: Configuration
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class Configuration {

    protected Environment environment;
    protected final MapperRegistry mapperRegistry = new MapperRegistry(this);
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    protected ExecutorType defaultExecutorType = ExecutorType.SIMPLE;
    protected boolean cacheEnabled = true;
    protected final Set<String> loadedResources = new HashSet<>();
    protected final Map<String, XNode> sqlFragments = new HashMap<>();
    protected final InterceptorChain interceptorChain = new InterceptorChain();
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();
    protected final LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();


    public Configuration() {
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("XML", XMLLanguageDriver.class);
        languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
    }

    public Configuration(Environment environment) {
        this();
        this.environment = environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
        executorType = executorType == null ? defaultExecutorType : executorType;
        executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
        Executor executor;
        if (ExecutorType.BATCH == executorType) {
            // TODO
            executor = new BatchExecutor(this, transaction);
        } else if (ExecutorType.REUSE == executorType) {
            // TODO
            executor = new ReuseExecutor(this, transaction);
        } else {
            executor = new SimpleExecutor(this, transaction);
        }
        if (cacheEnabled) {
            executor = new CachingExecutor(executor);
        }
        executor = (Executor) interceptorChain.pluginAll(executor);
        return executor;
    }

    public LanguageDriver getLanguageDriver(Class<? extends LanguageDriver> langClass) {
        if (langClass == null) {
            return languageRegistry.getDefaultDriver();
        }
        languageRegistry.register(langClass);
        return languageRegistry.getDriver(langClass);
    }

    public void addMappers(String packageName) throws ClassNotFoundException {
        mapperRegistry.addMappers(packageName);
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptorChain.addInterceptor(interceptor);
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public boolean hasStatement(String statementName) {
        return mappedStatements.containsKey(statementName);
    }

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
//        ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
//        parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler);
//        return parameterHandler;
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ParameterHandler parameterHandler,
                                                ResultHandler resultHandler, BoundSql boundSql) {
        ResultSetHandler resultSetHandler = new DefaultResultSetHandler(executor, this, mappedStatement, resultHandler);
        resultSetHandler = (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
        return resultSetHandler;
    }

    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement,
                                                Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
//        StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
//        statementHandler = (StatementHandler) interceptorChain.pluginAll(statementHandler);
//        return statementHandler;
        return new SimpleStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public Environment getEnvironment() {
        return environment;
    }
    public MetaObject newMetaObject(Object object) {
        return null;
    }
    public ExecutorType getDefaultExecutorType() {
        return defaultExecutorType;
    }
    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }
    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }
}
