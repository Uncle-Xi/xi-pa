package com.xipa.session;

public interface ResultHandler<T> {

    void handleResult(ResultContext<? extends T> resultContext);

}
