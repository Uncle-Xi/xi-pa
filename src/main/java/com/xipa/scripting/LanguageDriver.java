package com.xipa.scripting;

import com.xipa.executor.parameter.ParameterHandler;
import com.xipa.mapping.BoundSql;
import com.xipa.mapping.MappedStatement;
import com.xipa.mapping.SqlSource;
import com.xipa.parsing.XNode;
import com.xipa.session.Configuration;

public interface LanguageDriver {

    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);

    SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType);
}
