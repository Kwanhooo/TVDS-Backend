package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListService {
    @Resource
    private PartInfoMapper partInfoMapper;

    public List<PartInfo> listCarriageParts(Long id) {
        QueryWrapper<PartInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("compositeId", id);
        List<PartInfo> originPartInfos = partInfoMapper.selectList(queryWrapper);
        List<PartInfo> result = new ArrayList<>(originPartInfos);
        return result;
    }
}
