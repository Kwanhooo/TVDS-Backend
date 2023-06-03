package org.csu.tvds.cron;

import org.csu.tvds.cron.task.RepaintTimerTask;

import java.util.Timer;

public class RepaintTimer {
    public static void start() {
        Timer timer = new Timer();
        timer.schedule(new RepaintTimerTask(), 0, 1000);
    }
}
