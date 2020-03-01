package com.xipa.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description: Resources
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class Resources {

    public static InputStream getResourceAsStream(String resource) throws IOException {
        return getResourceAsStream(null, resource);
    }

    public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
        InputStream in = null;
        if (loader == null) {
            in = Resources.class.getClassLoader().getResourceAsStream(resource);;
        } else {
            in = /*classLoaderWrapper.getResourceAsStream(resource, loader)*/
                    loader.getResourceAsStream(resource);;
        }
        if (in == null) {
            throw new IOException("Could not find resource " + resource);
        }
        return in;
    }

    public static Class<?> classForName(String name) throws ClassNotFoundException {
        try {
            Class<?> c = Class.forName(name);
            if (null != c) {
                return c;
            }
        } catch (ClassNotFoundException e) {
        }
        throw new ClassNotFoundException("Cannot find class: " + name);
    }
}
