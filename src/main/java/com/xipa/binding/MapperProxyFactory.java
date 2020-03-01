package com.xipa.binding;

import com.xipa.session.SqlSession;
import com.xipa.session.defaults.DefaultSqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: MapperProxyFactory
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class MapperProxyFactory<T> {

    private Class object;
    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface, Class object) {
        this.mapperInterface = mapperInterface;
        this.object = object;
    }

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public T newInstance(SqlSession sqlSession) {
        return (T) Proxy.newProxyInstance(
                mapperInterface.getClassLoader(),
                new Class[] { mapperInterface },
                new MapperProxy(sqlSession, object)
        );
    }

    protected T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
    }
}
