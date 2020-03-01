
package com.xipa.cursor;

import java.io.Closeable;

public interface Cursor<T> extends Closeable, Iterable<T> {

    boolean isOpen();

    boolean isConsumed();

    int getCurrentIndex();
}
