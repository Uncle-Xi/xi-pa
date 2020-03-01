package com.xipa.scripting.xmltags;

//import com.alibaba.fastjson.JSON;
import ognl.Ognl;
import ognl.OgnlContext;

import java.math.BigDecimal;

public class OgnlUtils {

	public static Object getValue(String expression, Object paramObject) {
		try {
			OgnlContext context = new OgnlContext();
			if (paramObject.getClass().isArray()){
				paramObject = ((Object[]) paramObject)[0];
				//System.out.println("[OgnlUtils] [ognlExpression] - [" + expression);
				//System.out.println("[OgnlUtils] [context] - [" + context.toString());
				//System.out.println("[OgnlUtils] [paramObject] - [" + paramObject.toString());
			}
			context.setRoot(paramObject);
			Object ognlExpression = Ognl.parseExpression(expression);
			//System.out.println("[OgnlUtils] [ognlExpression] - [" + JSON.toJSONString(ognlExpression));
			//System.out.println("[OgnlUtils] [context] - [" + JSON.toJSONString(context));
			//System.out.println("[OgnlUtils] [paramObject] - [" + JSON.toJSONString(paramObject));
			Object value = Ognl.getValue(ognlExpression, context, context.getRoot());
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean evaluateBoolean(String expression, Object parameterObject) {
		Object value = OgnlUtils.getValue(expression, parameterObject);
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof Number) {
			return new BigDecimal(String.valueOf(value)).compareTo(BigDecimal.ZERO) != 0;
		}
		return value != null;
	}

	public static void main(String[] args) {
		String test = "test";
		//System.out.println(JSON.toJSONString(test));
	}
}
