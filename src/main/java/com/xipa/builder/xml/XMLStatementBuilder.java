package com.xipa.builder.xml;

import com.xipa.builder.BaseBuilder;
import com.xipa.builder.MapperBuilderAssistant;
import com.xipa.mapping.MappedStatement;
import com.xipa.mapping.SqlSource;
import com.xipa.parsing.XNode;
import com.xipa.scripting.LanguageDriver;
import com.xipa.session.Configuration;

import java.util.Locale;

/**
 * @description: XMLStatementBuilder
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XMLStatementBuilder extends BaseBuilder {

    private final XNode context;
    private final String requiredDatabaseId;
    private final MapperBuilderAssistant builderAssistant;

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context) {
        this(configuration, builderAssistant, context, null);
    }

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context, String databaseId) {
        super(configuration);
        this.builderAssistant = builderAssistant;
        this.context = context;
        this.requiredDatabaseId = databaseId;
    }

    public void parseStatementNode() {
        /** TODO 省略了其他丰富属性 */
        String sqlCommandType = context.getNode().getNodeName();
        String id = context.getStringAttribute("id");
        String parameterType = context.getStringAttribute("parameterType");
        Class<?> parameterTypeClass = resolveClass(parameterType);
        String resultType = context.getStringAttribute("resultType");
        Class<?> resultTypeClass = resolveClass(resultType);
        String lang = context.getStringAttribute("lang");
        LanguageDriver langDriver = getLanguageDriver(lang);
        String statementType = context.getStringAttribute("statementType");
        SqlSource sqlSource = langDriver.createSqlSource(configuration, context, parameterTypeClass);
        builderAssistant.addMappedStatement(id, parameterTypeClass, resultTypeClass, sqlSource, sqlCommandType, statementType);
    }

    private LanguageDriver getLanguageDriver(String lang) {
        Class<? extends LanguageDriver> langClass = null;
        if (lang != null) {
            langClass = resolveClass(lang);
        }
        return configuration.getLanguageDriver(langClass);
    }
}