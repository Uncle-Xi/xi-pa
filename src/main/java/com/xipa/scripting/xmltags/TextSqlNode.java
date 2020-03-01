package com.xipa.scripting.xmltags;

import java.util.regex.Pattern;

/**
 * @description: TextSqlNode
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class TextSqlNode implements SqlNode {
    private final String text;
    private final Pattern injectionFilter;

    public TextSqlNode(String text) {
        this(text, null);
        //System.out.printf("TextSqlNode text: %s \n", text);
    }

    public TextSqlNode(String text, Pattern injectionFilter) {
        this.text = text;
        this.injectionFilter = injectionFilter;
    }

    public boolean isDynamic() {
        if (text.indexOf("${") > -1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean apply(DynamicContext context) {
        return false;
    }
}
