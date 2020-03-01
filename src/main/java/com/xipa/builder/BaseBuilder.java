package com.xipa.builder;

import com.xipa.session.Configuration;
import com.xipa.type.TypeAliasRegistry;
import com.xipa.type.TypeHandlerRegistry;

public abstract class BaseBuilder {

    /** 全局配置 */
    protected final Configuration configuration;
    /** 别名注册器 */
    protected final TypeAliasRegistry typeAliasRegistry;
    /** 类型转换注册器 */
    protected final TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }

    public <T> Class<? extends T> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }

    protected <T> Class<? extends T> resolveClass(String alias) {
        if (alias == null) {
            return null;
        }
        try {
            return resolveAlias(alias);
        } catch (Exception e) {
            throw new BuilderException("Error resolving class. Cause: " + e, e);
        }
    }
}
