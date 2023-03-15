package org.csu.tvds.io;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.csu.tvds.service.impl.FileListenService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Component
public class CompositeDirListener extends FileAlterationListenerAdaptor {
    @Resource
    private FileListenService fileListenService;

    @Override
    public void onFileCreate(File file) {
        System.out.println("检测到`合成图像`有新文件增加：" + file.getAbsolutePath());
        fileListenService.handleCompositeCreate(file);
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("检测到`合成图像`有文件被删除：" + file.getAbsolutePath());
        fileListenService.handleCompositeDelete(file);
    }
}
