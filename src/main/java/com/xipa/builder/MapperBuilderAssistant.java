package com.xipa.builder;

import com.xipa.mapping.MappedStatement;
import com.xipa.mapping.SqlSource;
import com.xipa.session.Configuration;

/**
 * @description: MapperBuilderAssistant
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class MapperBuilderAssistant extends BaseBuilder {

    private String currentNamespace;

    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }

    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public MapperBuilderAssistant(Configuration configuration) {
        super(configuration);
    }

    public MappedStatement addMappedStatement(
            String id,
            Class<?> parameterType,
            Class<?> resultType,
            SqlSource sqlSource,
            String sqlCommandType,
            String statementType) {
        MappedStatement.Builder statementBuilder = new MappedStatement
                .Builder(configuration, currentNamespace + "." + id, sqlSource, sqlCommandType)
                .parameterType(parameterType)
                .resultType(resultType)
                .statementType(statementType);
        MappedStatement statement = statementBuilder.build();
        configuration.addMappedStatement(statement);
        try {
            configuration.addMappers(currentNamespace);
        } catch (Exception e){
            e.printStackTrace();
        }
        return statement;
    }
}
