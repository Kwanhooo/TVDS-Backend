package org.csu.tvds.cache;

import java.util.Map;

public class CompositeTimerCache {
    static {
        timerMap = new java.util.concurrent.ConcurrentHashMap<>();
    }

    private static final Map<Integer, Long> timerMap;

    public static void produce(Integer key) {
        Long value = System.currentTimeMillis();
        if (timerMap.containsKey(key)) {
            timerMap.replace(key, value);
        } else {
            timerMap.put(key, value);
        }
    }

    public static void consume(Integer key) {
        timerMap.remove(key);
    }

    public static Map<Integer, Long> get() {
        return timerMap;
    }
}
