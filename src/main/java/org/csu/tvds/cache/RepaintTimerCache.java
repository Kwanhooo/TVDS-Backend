package org.csu.tvds.cache;

import java.util.Map;

public class RepaintTimerCache {
    static {
        timerMap = new java.util.concurrent.ConcurrentHashMap<>();
    }

    private static final Map<Long, Long> timerMap;

    public static void produce(Long key) {
        Long value = System.currentTimeMillis();
        if (timerMap.containsKey(key)) {
            timerMap.replace(key, value);
        } else {
            timerMap.put(key, value);
        }
    }

    public static void consume(Long key) {
        timerMap.remove(key);
    }

    public static Map<Long, Long> get() {
        return timerMap;
    }
}
