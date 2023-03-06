package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.entity.mysql.OriginImage;
import org.csu.tvds.models.dto.OriginRetrieveConditions;
import org.csu.tvds.models.vo.DateTreeVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.persistence.mysql.OriginImageMapper;
import org.csu.tvds.service.OriginImageService;
import org.csu.tvds.util.TreeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author kwanho
 */
@Service
public class OriginImageServiceImpl extends ServiceImpl<OriginImageMapper, OriginImage>
        implements OriginImageService {
    @Resource
    private TreeUtil treeUtil;

    @Override
    public PaginationVO<List<OriginImage>> getOverviews(OriginRetrieveConditions conditions, long currentPage, long pageSize) {
        // init
        PaginationVO<List<OriginImage>> result = new PaginationVO<>();
        QueryWrapper<OriginImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime");
        // resolve conditions
        if (conditions != null) {
            String startDate = conditions.getStartDate();
            String endDate = conditions.getEndDate();
            if (StringUtils.isAnyBlank(startDate, endDate))
                queryWrapper.between("createTime", startDate, endDate);
        }
        // do search
        Page<OriginImage> page = new Page<>(currentPage, pageSize);
        IPage<OriginImage> iPage = this.page(page, queryWrapper);
        List<OriginImage> records = iPage.getRecords();
        // pagination info
        result.setCurrentPage(currentPage);
        result.setPageSize(pageSize);
        result.setTotalPage(iPage.getPages());
        result.setPage(records);
        return result;
    }

    @Override
    public DateTreeVO buildDateTree() {
        List<LocalDate> dates = this.baseMapper.selectDistinctDate();
        DateTreeVO treeVO = new DateTreeVO();
        treeVO.setTree(treeUtil.buildDateTree(dates));
        return treeVO;
    }
}




