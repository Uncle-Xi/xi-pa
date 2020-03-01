package com.xipa.binding;

import com.xipa.mapping.MappedStatement;
import com.xipa.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: MapperProxy
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -6424540398559729838L;
    private final SqlSession sqlSession;
    private final Class<T> mapperInterface;
    private final Map<Method, Integer> methodCache = new HashMap<>();

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (methodCache.get(method) == null) {
            this.cachedMapperMethod(method);
        }
        String mapperInterface = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String statementId = mapperInterface + "." + methodName;
        MappedStatement mappedStatement = sqlSession.getConfiguration().getMappedStatement(statementId);
        if (/*sqlSession.getConfiguration().hasStatement(statementId)*/
                mappedStatement != null) {
            if (mappedStatement.getSqlCommandType().equalsIgnoreCase("select")) {
                if (methodCache.get(method) != null && methodCache.get(method) == 1) {
                    return sqlSession.selectOne(statementId, args);
                }
                return sqlSession.selectList(statementId, args);
            } else {
                return sqlSession.update(statementId, args);
            }
        }
        return method.invoke(proxy, args);
    }

    private void cachedMapperMethod(Method method) {
        Class<?> returnTypeClass = method.getReturnType();
//        Type returnType = method.getGenericReturnType();
//        System.out.println("returnType >>>> " + ( Map.class.isAssignableFrom(returnTypeClass)));
//        System.out.println("returnType >>>> " + ( Collection.class.isAssignableFrom(returnTypeClass)));
//        System.out.println("returnType >>>> " + (returnTypeClass.isArray()));
//        System.out.println("returnType >>>> " + (returnType instanceof TypeVariable));
//        System.out.println("returnType >>>> " + (returnType instanceof ParameterizedType));
//        System.out.println("returnType >>>> " + (returnType instanceof GenericArrayType));
//        return methodCache.computeIfAbsent(method, k -> new MapperMethod(mapperInterface, method, sqlSession.getConfiguration()));
        boolean more = Map.class.isAssignableFrom(returnTypeClass) || Collection.class.isAssignableFrom(returnTypeClass) || returnTypeClass.isArray();
        methodCache.put(method, more == true ? Integer.MAX_VALUE : 1);
    }
}
