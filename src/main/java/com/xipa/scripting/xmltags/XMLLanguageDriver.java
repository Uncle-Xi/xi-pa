package com.xipa.scripting.xmltags;

import com.xipa.executor.parameter.ParameterHandler;
import com.xipa.parsing.XPathParser;
import com.xipa.scripting.LanguageDriver;

import com.xipa.executor.parameter.ParameterHandler;
import com.xipa.mapping.BoundSql;
import com.xipa.mapping.MappedStatement;
import com.xipa.mapping.SqlSource;
import com.xipa.parsing.XNode;
import com.xipa.scripting.defaults.DefaultParameterHandler;
import com.xipa.session.Configuration;

/**
 * @description: XMLLanguageDriver
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XMLLanguageDriver implements LanguageDriver {

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    @Override
    public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }
}
