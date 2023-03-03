package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.csu.tvds.service.PartInfoService;
import org.springframework.stereotype.Service;

/**
 * @author kwanho
 * @description 针对表【part_info】的数据库操作Service实现
 * @createDate 2023-03-03 23:05:08
 */
@Service
public class PartInfoServiceImpl extends ServiceImpl<PartInfoMapper, PartInfo>
        implements PartInfoService {

}




