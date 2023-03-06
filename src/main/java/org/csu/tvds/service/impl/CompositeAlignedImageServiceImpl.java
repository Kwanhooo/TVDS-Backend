package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.models.dto.CarriageRetrieveConditions;
import org.csu.tvds.models.vo.CarriageOverviewVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.models.vo.DateTreeVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.service.CompositeAlignedImageService;
import org.csu.tvds.util.TreeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kwanho
 */
@Service
public class CompositeAlignedImageServiceImpl extends ServiceImpl<CompositeAlignedImageMapper, CompositeAlignedImage>
        implements CompositeAlignedImageService {
    @Resource
    private TreeUtil treeUtil;


    /**
     * 获取车厢检索结果
     *
     * @param conditions  检索条件
     * @param currentPage 页码
     * @param pageSize    每页大小
     * @return 检索结果
     */
    @Override
    public PaginationVO<List<CarriageOverviewVO>> getOverviews(CarriageRetrieveConditions conditions, long currentPage, long pageSize) {
        // init
        PaginationVO<List<CarriageOverviewVO>> result = new PaginationVO<>();
        result.setPage(new ArrayList<>());
        QueryWrapper<CompositeAlignedImage> queryWrapper = null;
        // resolve conditions
        if (conditions != null) {
            queryWrapper = this.buildQueryWrapperByConditions(conditions);
        }
        Page<CompositeAlignedImage> page = new Page<>(currentPage, pageSize);
        IPage<CompositeAlignedImage> iPage = this.page(page, queryWrapper);
        List<CompositeAlignedImage> records = iPage.getRecords();
        // set pagination info
        result.setCurrentPage(currentPage);
        result.setPageSize(pageSize);
        result.setTotalPage(iPage.getPages());
        // convert
        records.forEach(r -> {
            CarriageOverviewVO vo = new CarriageOverviewVO();
            BeanUtils.copyProperties(r, vo);
            vo.setUrl(r.getCompositeUrl());
            vo.setCompositeUrl(null);
            result.getPage().add(vo);
        });
        return result;
    }

    @Override
    public DateTreeVO buildDateTree() {
        List<LocalDate> dates = this.baseMapper.selectDistinctDate();
        DateTreeVO treeVO = new DateTreeVO();
        treeVO.setTree(treeUtil.buildDateTree(dates));
        return treeVO;
    }

    /**
     * 根据车厢检索条件构建wrapper
     *
     * @param conditions 车厢检索条件
     * @return wrapper
     */
    private QueryWrapper<CompositeAlignedImage> buildQueryWrapperByConditions(CarriageRetrieveConditions conditions) {
        QueryWrapper<CompositeAlignedImage> queryWrapper = new QueryWrapper<>();
        String startDate = conditions.getStartDate();
        String endDate = conditions.getEndDate();
        if (!StringUtils.isAnyBlank(startDate, endDate)) {
            queryWrapper.between("compositeTime", startDate, endDate);
        }
        return queryWrapper;
    }
}




