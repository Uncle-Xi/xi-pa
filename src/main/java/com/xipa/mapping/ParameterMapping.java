package com.xipa.mapping;

import com.xipa.session.Configuration;

/**
 * @description: ParameterMapping
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ParameterMapping {

    private Configuration configuration;
    private String name;
    private Class<?> type;

    public ParameterMapping(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Class<?> getType() {
        return type;
    }
    public void setType(Class<?> type) {
        this.type = type;
    }


}
