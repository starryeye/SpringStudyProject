package dev.practice.logging;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;

public class MyThreadContext {

    public static boolean isSupported() {
        return ThreadContext.getThreadContextMap() instanceof ObjectThreadContextMap;
    }

    public static Object getValue(String key) {
        return getObjectMap().getValue(key);
    }

    public static void putValue(String key, Object value) {
        getObjectMap().putValue(key, value);
    }

    public static void remove(String key) {
        getObjectMap().remove(key);
    }

    private static ObjectThreadContextMap getObjectMap() {
        if (!isSupported()) { throw new UnsupportedOperationException(); }
        return (ObjectThreadContextMap) ThreadContext.getThreadContextMap();
    }
}
