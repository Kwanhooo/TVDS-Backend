package org.csu.tvds.io;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.csu.tvds.service.impl.FileListenService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Component
public class OriginDirListener extends FileAlterationListenerAdaptor {
    @Resource
    private FileListenService fileListenService;

    @Override
    public void onFileCreate(File file) {
        System.out.println("检测到`原始图像`有新文件增加：" + file.getAbsolutePath());
        fileListenService.handleOriginCreate(file);
    }
}
