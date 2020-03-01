package com.xipa.builder.annotation;

import com.xipa.annotation.Delete;
import com.xipa.annotation.Insert;
import com.xipa.annotation.Select;
import com.xipa.annotation.Update;
import com.xipa.session.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: MapperAnnotationBuilder
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class MapperAnnotationBuilder {

    private static final Set<Class<? extends Annotation>> SQL_ANNOTATION_TYPES = new HashSet<>();
    private static final Set<Class<? extends Annotation>> SQL_PROVIDER_ANNOTATION_TYPES = new HashSet<>();

    private final Configuration configuration;
    private final Class<?> type;

    public MapperAnnotationBuilder(Configuration configuration, Class<?> type) {
        String resource = type.getName().replace('.', '/') + ".java (best guess)";
//        this.assistant = new MapperBuilderAssistant(configuration, resource);
        this.configuration = configuration;
        this.type = type;
    }

    /**
     * 静态代码块，初始化mapper接口中的CRUD注解
     */
    static {
        SQL_ANNOTATION_TYPES.add(Select.class);
        SQL_ANNOTATION_TYPES.add(Insert.class);
        SQL_ANNOTATION_TYPES.add(Update.class);
        SQL_ANNOTATION_TYPES.add(Delete.class);

//        SQL_PROVIDER_ANNOTATION_TYPES.add(SelectProvider.class);
//        SQL_PROVIDER_ANNOTATION_TYPES.add(InsertProvider.class);
//        SQL_PROVIDER_ANNOTATION_TYPES.add(UpdateProvider.class);
//        SQL_PROVIDER_ANNOTATION_TYPES.add(DeleteProvider.class);
    }

    public void parse() {
        String resource = type.toString();
        if (!configuration.isResourceLoaded(resource)) {
        }
    }
}
