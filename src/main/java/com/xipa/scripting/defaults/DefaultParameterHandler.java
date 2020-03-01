package com.xipa.scripting.defaults;

import com.xipa.executor.parameter.ParameterHandler;
import com.xipa.mapping.BoundSql;
import com.xipa.mapping.MappedStatement;
import com.xipa.mapping.ParameterMapping;
import com.xipa.session.Configuration;
import com.xipa.type.TypeHandlerRegistry;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @description: DefaultParameterHandler
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class DefaultParameterHandler implements ParameterHandler {

    private final TypeHandlerRegistry typeHandlerRegistry;
    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private final BoundSql boundSql;
    private final Configuration configuration;

    public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
    }

    @Override
    public Object getParameterObject() {
        return null;
    }

    @Override
    public void setParameters(PreparedStatement ps) throws SQLException, NoSuchFieldException, IllegalAccessException {
        // 先判断入参类型
        Class<?> parameterTypeClass = mappedStatement.getParameterType();
        if (parameterTypeClass == Integer.class) {
            ps.setObject(1, Integer.parseInt(parameterObject.toString()));
        } else if (parameterTypeClass == String.class) {
            ps.setObject(1, parameterObject.toString());
        } else if (parameterTypeClass == Map.class) {
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for(int i = 0; i < parameterMappings.size(); i++){
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String name = parameterMapping.getName();
                Map<String, Object> paramMap = (Map<String, Object>) parameterObject;
                ps.setObject(i + 1, paramMap.get(name));
            }
        } else {// 自定义对象类型
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                // 获取 #{} 中的属性名称
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String name = parameterMapping.getName();
                // 根据属性名称，获取入参对象中对应的属性的值
                // 要求 #{} 中的属性名称和入参对象中的属性名称一致
                Field field = parameterTypeClass.getDeclaredField(name);
                field.setAccessible(true);
                if (parameterObject instanceof Object[]) {
                    Object[] args = (Object[])parameterObject;
                    Object value = field.get(args[0]);
                    ps.setObject(i + 1, value);
                } else {
                    Object value = field.get(parameterObject);
                    ps.setObject(i + 1, value);
                }

            }
        }
    }
}
