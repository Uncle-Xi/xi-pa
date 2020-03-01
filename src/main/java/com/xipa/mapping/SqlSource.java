package com.xipa.mapping;

public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject);
}
