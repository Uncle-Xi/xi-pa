package com.xipa.builder.xml;

import com.xipa.builder.BaseBuilder;
import com.xipa.builder.MapperBuilderAssistant;
import com.xipa.io.Resources;
import com.xipa.parsing.XNode;
import com.xipa.parsing.XPathParser;
import com.xipa.session.Configuration;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @description: XMLMapperBuilder
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XMLMapperBuilder extends BaseBuilder {

    private final MapperBuilderAssistant builderAssistant;
    private final XPathParser parser;
    private final Map<String, XNode> sqlFragments;
    private final String resource;

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource, Map<String, XNode> sqlFragments) {
        this(new XPathParser(inputStream, true, null, new XMLMapperEntityResolver()),
                configuration, resource, sqlFragments);
    }

    public XMLMapperBuilder(XPathParser parser, Configuration configuration, String resource, Map<String, XNode> sqlFragments) {
        super(configuration);
        this.parser = parser;
        this.sqlFragments = sqlFragments;
        this.resource = resource;
        this.builderAssistant = new MapperBuilderAssistant(configuration);
    }

    public void parse() {
        if (!configuration.isResourceLoaded(resource)) {
            configurationElement(parser.evalNode("/mapper"));
            configuration.addLoadedResource(resource);
            bindMapperForNamespace();
        }
    }

    private void configurationElement(XNode context) {
        try {
            String namespace = context.getStringAttribute("namespace");
            if (namespace == null || namespace.equals("")) {
                throw new RuntimeException("Mapper's namespace cannot be empty");
            }
            setCurrentNamespace(namespace);
            // cacheElement(context.evalNode("cache"));
            // parameterMapElement(context.evalNodes("/mapper/parameterMap"));
            // resultMapElements(context.evalNodes("/mapper/resultMap"));
            // sqlElement(context.evalNodes("/mapper/sql"));
            buildStatementFromContext(context.evalNodes("select|insert|update|delete"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Mapper XML. The XML location is '" + resource + "'. Cause: " + e, e);
        }
    }

    /**
     * TODO 略去了数据库 ID
     * @param list
     */
    private void buildStatementFromContext(List<XNode> list) {
        buildStatementFromContext(list, null);
    }

    private void buildStatementFromContext(List<XNode> list, String requiredDatabaseId) {
        for (XNode context : list) {
            final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration,
                    builderAssistant, context, requiredDatabaseId);
            try {
                statementParser.parseStatementNode();
            } catch (Exception e) {
                // TODO Collection<XMLStatementBuilder> incompleteStatements
                System.out.println("buildStatementFromContext Exception" + e);
            }
        }
    }

    private void setCurrentNamespace(String namespace) {
        builderAssistant.setCurrentNamespace(namespace);
    }

    private void bindMapperForNamespace() {
        String namespace = "";
        if (namespace != null) {
            Class<?> boundType = null;
            try {
                boundType = Resources.classForName(namespace);
            } catch (ClassNotFoundException e) {}
            if (boundType != null) {
                if (!configuration.hasMapper(boundType)) {
                    configuration.addLoadedResource("namespace:" + namespace);
                    configuration.addMapper(boundType);
                }
            }
        }
    }
}
