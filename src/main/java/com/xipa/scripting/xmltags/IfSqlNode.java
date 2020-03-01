package com.xipa.scripting.xmltags;

import com.xipa.builder.xml.XMLConfigBuilder;
import jdk.nashorn.internal.ir.CallNode;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @description: IfSqlNode
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class IfSqlNode implements SqlNode {

    private static final Logger log = Logger.getLogger(IfSqlNode.class.toString());
    private final String test;
    private final SqlNode contents;

    public IfSqlNode(SqlNode contents, String test) {
        this.test = test;
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        log.info("[IF标签解析] [参数] - [" + context.getBindings().get("_parameter").toString() + "] [表达式]：" + test);
        boolean testValue = OgnlUtils.evaluateBoolean(test, context.getBindings().get("_parameter"));
        if (testValue) {
            log.info("[IF标签] [SQL片段] - [" + context.getSql());
            contents.apply(context);
        }
        return false;
    }

    // boolean testValue = evaluateBoolean(test, context.getBindings().get("_parameter"));
    private static boolean evaluateBoolean(String test, Object object) {
        return false;
    }

    public static void main(String[] args) {
        String str = "(a or b) and c";
        str = str.replaceAll("or", "||");
        str = str.replaceAll("and", "&&");
        System.out.println(str);
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("a", true);
        engine.put("b", false);
        engine.put("c", true);
        Object result = null;
        try {
            result = engine.eval(str);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:" + result);

    }

}
