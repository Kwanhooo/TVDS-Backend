package org.csu.tvds.cron;

import org.csu.tvds.cron.task.CompositeTimerTask;

import java.util.Timer;

public class CompositeTimer {
    public static void start() {
        Timer timer = new Timer();
        timer.schedule(new CompositeTimerTask(), 0, 1000);
    }
}
