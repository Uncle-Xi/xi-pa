package com.xipa.plugin;

import java.util.Properties;

public interface Interceptor{

  Object intercept(Invocation invocation) throws Throwable;

  Object plugin(Object target);

  void setProperties(Properties properties);

}