package com.xipa.scripting.xmltags;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: DynamicContext
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class DynamicContext {

    public static final String PARAMETER_OBJECT_KEY = "_parameter";
    public static final String DATABASE_ID_KEY = "_databaseId";

    private final StringBuilder sqlBuilder = new StringBuilder();

    private Map<String, Object> bindings = new HashMap<String, Object>();

    public DynamicContext(Object parameter){
        this.bindings.put("_parameter", parameter);
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void appendSql(String sql) {
        sqlBuilder.append(sql);
        sqlBuilder.append(" ");
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }
}
