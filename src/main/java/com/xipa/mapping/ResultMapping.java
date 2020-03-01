package com.xipa.mapping;

import com.xipa.session.Configuration;
import com.xipa.type.JdbcType;

/**
 * @description: ResultMapping
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ResultMapping {
    private Configuration configuration;
    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
}
