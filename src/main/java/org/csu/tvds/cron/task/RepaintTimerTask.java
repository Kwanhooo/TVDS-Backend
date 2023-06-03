package org.csu.tvds.cron.task;

import org.csu.tvds.cache.RepaintTimerCache;
import org.csu.tvds.service.impl.VisionServiceImpl;
import org.csu.tvds.util.SpringContextUtils;

import java.util.Map;
import java.util.TimerTask;

public class RepaintTimerTask extends TimerTask {

    private static final VisionServiceImpl visionService = SpringContextUtils.getBean(VisionServiceImpl.class);

    @Override
    public void run() {
        Map<Long, Long> map = RepaintTimerCache.get();
        map.forEach((k, v) -> {
            if (System.currentTimeMillis() - v > 10000) {
                System.out.println("现调用`重绘图像`接口，dbId => " + k);
                visionService.repaint(String.valueOf(k));
                RepaintTimerCache.consume(k);
            }
        });
    }
}
