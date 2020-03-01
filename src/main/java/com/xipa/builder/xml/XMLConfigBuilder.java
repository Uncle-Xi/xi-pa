package com.xipa.builder.xml;

import com.xipa.builder.BaseBuilder;
import com.xipa.builder.BuilderException;
import com.xipa.datasource.DataSourceFactory;
import com.xipa.io.Resources;
import com.xipa.mapping.Environment;
import com.xipa.parsing.XNode;
import com.xipa.parsing.XPathParser;
import com.xipa.plugin.Interceptor;
import com.xipa.session.Configuration;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @description: XMLConfigBuilder
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XMLConfigBuilder extends BaseBuilder {

    private static final Logger log = Logger.getLogger(XMLConfigBuilder.class.toString());

    private boolean parsed;
    private final XPathParser parser;
    private String environment;

    public XMLConfigBuilder(InputStream inputStream, String environment, Properties props) {
        this(new XPathParser(inputStream, true, props, new XMLMapperEntityResolver()), environment, props);
    }

    private XMLConfigBuilder(XPathParser parser, String environment, Properties props) {
        super(new Configuration());
        this.parsed = false;
        this.environment = environment;
        this.parser = parser;
    }

    /**
     * 解析XML配置文件
     * @return
     */
    public Configuration parse() {
        if (parsed) {
            throw new BuilderException("Each XMLConfigBuilder can only be used once.");
        }
        parsed = true;
        parseConfiguration(parser.evalNode("/configuration"));
        return configuration;
    }

    private void parseConfiguration(XNode root) {
        log.info("parseConfiguration start...");
        try {
            // 解析数据源
            environmentsElement(root.evalNode("environments"));
            // 解析 mapper 映射文件
            mapperElement(root.evalNode("mappers"));
            // 解析插件
            pluginElement(root.evalNode("plugins"));
            // TODO 其他解析暂时省略
        } catch (Exception e) {
            throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
    }

    /**
     * 数据源信息
     * @param context
     * @throws Exception
     */
    private void environmentsElement(XNode context) throws Exception {
        if (context != null) {
            if (environment == null) {
                environment = context.getStringAttribute("default");
            }
            for (XNode child : context.getChildren()) {
                String id = child.getStringAttribute("id");
                if (isSpecifiedEnvironment(id)){
                    log.info("environmentsElement start...");
                    DataSourceFactory dsFactory = dataSourceElement(child.evalNode("dataSource"));
                    DataSource dataSource = dsFactory.getDataSource();
                    Environment.Builder environmentBuilder = new Environment.Builder(id)
                            .transactionFactory(null)
                            .dataSource(dataSource);
                    configuration.setEnvironment(environmentBuilder.build());
                }
            }
        }
    }

    /**
     * 数据源工厂
     * @param context
     * @return
     * @throws Exception
     */
    private DataSourceFactory dataSourceElement(XNode context) throws Exception {
        if (context != null) {
            String type = context.getStringAttribute("type");
            Properties props = context.getChildrenAsProperties();
            DataSourceFactory factory = (DataSourceFactory) resolveClass(type).newInstance();
            factory.setProperties(props);
            return factory;
        }
        throw new BuilderException("Environment declaration requires a DataSourceFactory.");
    }

    /**
     * mapper 映射文件
     * @param parent
     * @throws Exception
     */
    private void mapperElement(XNode parent) throws Exception {
        if (parent != null) {
            for (XNode child : parent.getChildren()) {
                if ("package".equals(child.getName())) {
                    // TODO
                    String mapperPackage = child.getStringAttribute("name");
                    configuration.addMappers(mapperPackage);
                } else {
                    String resource = child.getStringAttribute("resource");
                    // String url = child.getStringAttribute("url");
                    // String mapperClass = child.getStringAttribute("class");
                    log.info("mapperElement resource[" + resource + "] parse start...");
                    InputStream inputStream = Resources.getResourceAsStream(resource);
                    log.info("Resources.getResourceAsStream(resource) start.");
                    XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream,
                            configuration, resource,
                            null);
                    log.info("mapperElement resource[" + resource + "] new XMLMapperBuilder...");
                    mapperParser.parse();
                    log.info("完成一个node节点解析：" + child.getName());
                }
            }
        }
    }

    /**
     * 插件解析
     * @param parent
     * @throws Exception
     */
    private void pluginElement(XNode parent) throws Exception {
        if (parent != null) {
            for (XNode child : parent.getChildren()) {
                String interceptor = child.getStringAttribute("interceptor");
                Properties properties = child.getChildrenAsProperties();
                Interceptor interceptorInstance = (Interceptor) resolveClass(interceptor).newInstance();
                interceptorInstance.setProperties(properties);
                configuration.addInterceptor(interceptorInstance);
            }
        }
    }

    /**
     * 是否为默认
     * @param id
     * @return
     */
    private boolean isSpecifiedEnvironment(String id) {
        if (environment == null) {
            throw new BuilderException("No environment specified.");
        } else if (id == null) {
            throw new BuilderException("Environment requires an id attribute.");
        } else if (environment.equals(id)) {
            return true;
        }
        return false;
    }
}
