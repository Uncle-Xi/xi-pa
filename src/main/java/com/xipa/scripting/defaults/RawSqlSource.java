package com.xipa.scripting.defaults;

import com.xipa.builder.SqlSourceBuilder;
import com.xipa.mapping.BoundSql;
import com.xipa.mapping.SqlSource;
import com.xipa.scripting.xmltags.DynamicContext;
import com.xipa.scripting.xmltags.SqlNode;
import com.xipa.session.Configuration;

import java.util.HashMap;

/**
 * @description: RawSqlSource
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;

    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration, getSql(configuration, rootSqlNode), parameterType);
    }

    /**
     * 提前完成解析
     * @param configuration
     * @param sql
     * @param parameterType
     */
    public RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> clazz = parameterType == null ? Object.class : parameterType;
        sqlSource = sqlSourceParser.parse(sql, clazz, new HashMap<>());
    }

    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(null);
        rootSqlNode.apply(context);
        return context.getSql();
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

}
