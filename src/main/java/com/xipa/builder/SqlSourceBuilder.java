package com.xipa.builder;

import com.xipa.mapping.ParameterMapping;
import com.xipa.mapping.SqlSource;
import com.xipa.parsing.GenericTokenParser;
import com.xipa.parsing.TokenHandler;
import com.xipa.reflection.MetaObject;
import com.xipa.session.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: SqlSourceBuilder
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class SqlSourceBuilder extends BaseBuilder {

    private static final String PARAMETER_PROPERTIES = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler(configuration, parameterType, additionalParameters);
        GenericTokenParser tokenParser = new GenericTokenParser("#{", "}", tokenHandler);
        originalSql = tokenParser.parse(originalSql);
        return new StaticSqlSource(configuration, originalSql, tokenHandler.getParameterMappings());
    }

    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

        private List<ParameterMapping> parameterMappings = new ArrayList<>();
        private Class<?> parameterType;
        private MetaObject metaParameters;

        public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType, Map<String, Object> additionalParameters) {
            super(configuration);
            this.parameterType = parameterType;
            this.metaParameters = configuration.newMetaObject(additionalParameters);
        }

        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }

        @Override
        public String handleToken(String content) {
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        // TODO
        private ParameterMapping buildParameterMapping(String content) {
            return new ParameterMapping(content);
        }
    }

}
