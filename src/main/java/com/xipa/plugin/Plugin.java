package com.xipa.plugin;

import com.xipa.annotation.Intercepts;
import com.xipa.reflection.ExceptionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Plugin implements InvocationHandler {

    private final Object target;
    private final Interceptor interceptor;

    private Plugin(Object target, Interceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }

    public static Object wrap(Object target, Interceptor interceptor) {
        Class<?> type = target.getClass();
        Class<?>[] interfaces = interceptor.getClass().getInterfaces();
        if (interfaces.length > 0) {
            return Proxy.newProxyInstance(
                    type.getClassLoader(),
                    type.getInterfaces(),
                    new Plugin(target, interceptor));
        }
        return target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (interceptor.getClass().isAnnotationPresent(Intercepts.class)) {
                // 如果是被拦截的方法，则进入自定义拦截器的逻辑
                String[] annotationName = interceptor.getClass().getAnnotation(Intercepts.class).value();
                if (method.getName().equals(annotationName[0])) {
                    return interceptor.intercept(new Invocation(target, method, args));
                }
            }
            return method.invoke(target, args);
        } catch (Exception e) {
            throw ExceptionUtil.unwrapThrowable(e);
        }
    }


    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set<Class<?>> interfaces = new HashSet<>();
        while (type != null) {
            for (Class<?> c : type.getInterfaces()) {
                if (signatureMap.containsKey(c)) {
                    interfaces.add(c);
                }
            }
            type = type.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }
}