package org.csu.tvds.cron.task;

import org.csu.tvds.cache.CompositeTimerCache;
import org.csu.tvds.core.CompositeModel;

import java.util.Map;
import java.util.TimerTask;

public class CompositeTimerTask extends TimerTask {

    private static final CompositeModel compositeModel = new CompositeModel();

    @Override
    public void run() {
        Map<Integer, Long> map = CompositeTimerCache.get();
        map.forEach((k, v) -> {
            if (System.currentTimeMillis() - v > 5000) {
                System.out.println("现调用`合成图像`接口，inspectionSeq => " + k);
                compositeModel.dispatch();
                CompositeTimerCache.consume(k);
            }
        });
    }
}
