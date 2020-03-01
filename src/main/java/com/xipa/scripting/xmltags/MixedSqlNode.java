package com.xipa.scripting.xmltags;

import java.util.List;

/**
 * @description: MixedSqlNode
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class MixedSqlNode implements SqlNode {
    private final List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        //System.out.printf("MixedSqlNode apply 前： %s, SqlNode.size = %d\n", context.getSql(), contents.size());
        for (SqlNode sqlNode : contents) {
            sqlNode.apply(context);
        }
        //System.out.printf("MixedSqlNode apply 后： %s \n", context.getSql());
        return true;
    }
}
