package com.xipa.builder;

import com.xipa.exceptions.PersistenceException;

/**
 * @description: BuilderException
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class BuilderException extends PersistenceException {

    private static final long serialVersionUID = -3885164021020443281L;

    public BuilderException() {
        super();
    }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderException(Throwable cause) {
        super(cause);
    }
}
