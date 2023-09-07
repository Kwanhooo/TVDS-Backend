package org.csu.tvds.cache;

import java.util.Map;

public class CompositeTimerCache {
    static {
        timerMap = new java.util.concurrent.ConcurrentHashMap<>();
    }

    private static final Map<String, Long> timerMap;

    public static void produce(String key) {
        Long value = System.currentTimeMillis();
        if (timerMap.containsKey(key)) {
            timerMap.replace(key, value);
        } else {
            timerMap.put(key, value);
        }
    }

    public static void consume(String key) {
        timerMap.remove(key);
    }

    public static Map<String, Long> get() {
        return timerMap;
    }
}
