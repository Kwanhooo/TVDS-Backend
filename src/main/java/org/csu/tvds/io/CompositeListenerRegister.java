package org.csu.tvds.io;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.csu.tvds.common.PathConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;

@Component
public class CompositeListenerRegister {
    @Resource
    private CompositeDirListener compositeDirListener;

    private static boolean isRegistered = false;

    @PostConstruct
    public void register() {
        if (isRegistered) {
            System.out.println("此前已注册‵合成图像`监听器！");
            return;
        }
        File directory = new File(PathConfig.COMPOSITE_BASE);

        FileAlterationObserver observer = new FileAlterationObserver(directory);
        FileAlterationListener listener = compositeDirListener;
        observer.addListener(listener);

        FileAlterationMonitor monitor = new FileAlterationMonitor(1000, observer);
        try {
            monitor.start();
        } catch (Exception e) {
            System.out.println("在注册`合成图像`监听器时出错！");
            throw new RuntimeException(e);
        }
        System.out.println("`合成图像`监听器现已成功注册！(" + PathConfig.COMPOSITE_BASE + ")");
        isRegistered = true;
    }

}
