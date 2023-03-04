package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.models.dto.PartRetrieveConditions;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.csu.tvds.service.PartInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kwanho
 */
@Service
public class PartInfoServiceImpl extends ServiceImpl<PartInfoMapper, PartInfo>
        implements PartInfoService {

    @Override
    public PaginationVO<List<PartInfo>> getOverviews(PartRetrieveConditions conditions, long currentPage, long pageSize) {
        // init
        PaginationVO<List<PartInfo>> result = new PaginationVO<>();
        QueryWrapper<PartInfo> queryWrapper = null;
        // resolve conditions
        if (conditions != null) {
            queryWrapper = new QueryWrapper<>();
            String startDate = conditions.getStartDate();
            String endDate = conditions.getEndDate();
            String model = conditions.getModel();
            String partName = conditions.getPartName();
            String inspectionSeq = conditions.getInspectionSeq();

            if (StringUtils.isAnyBlank(startDate, endDate))
                queryWrapper.between("createTime", startDate, endDate);
            if (StringUtils.isNotBlank(model))
                queryWrapper.eq("model", model);
            if (StringUtils.isNotBlank(partName))
                queryWrapper.eq("partName", partName);
            if (StringUtils.isNotBlank(inspectionSeq))
                queryWrapper.eq("inspectionSeq", inspectionSeq);
        }
        // do search
        Page<PartInfo> page = new Page<>(currentPage, pageSize);
        IPage<PartInfo> iPage = this.page(page, queryWrapper);
        List<PartInfo> records = iPage.getRecords();
        // pagination info
        result.setCurrentPage(currentPage);
        result.setPageSize(pageSize);
        result.setTotalPage(iPage.getPages());
        result.setContent(records);
        return result;
    }
}




