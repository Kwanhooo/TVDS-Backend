package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.models.dto.CarriageRetrieveConditions;
import org.csu.tvds.models.vo.CarriageOverviewVO;
import org.csu.tvds.models.vo.DateTreeVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.models.vo.PartCountVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
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

    @Resource
    private PartInfoMapper partInfoMapper;


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
        QueryWrapper<CompositeAlignedImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("compositeTime");
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
            // 获得零件计数
            vo.setPartCount(getPartCount(vo.getDbId()));
            result.getPage().add(vo);
        });
        return result;
    }

    private List<PartCountVO> getPartCount(Long id) {
        QueryWrapper<PartInfo> qw = new QueryWrapper<>();
        List<PartCountVO> result = new ArrayList<>();
        // spring
        qw.eq("compositeId", id).eq("partName", "spring");
        Integer springCount = partInfoMapper.selectCount(qw);
        result.add(new PartCountVO("spring", springCount));

        // bearing
        qw.clear();
        qw.eq("compositeId", id).eq("partName", "bearing");
        Integer bearingCount = partInfoMapper.selectCount(qw);
        result.add(new PartCountVO("bearing", bearingCount));

        // wheel
        qw.clear();
        qw.eq("compositeId", id).eq("partName", "wheel");
        Integer wheelCount = partInfoMapper.selectCount(qw);
        result.add(new PartCountVO("wheel", wheelCount));

        return result;
    }

    @Override
    public DateTreeVO buildDateTree() {
        List<LocalDate> dates = this.baseMapper.selectDistinctDate();
        DateTreeVO treeVO = new DateTreeVO();
        System.out.println(dates);
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
        queryWrapper.orderByDesc("compositeTime");

//        List<String> treeList = conditions.getTreeList();
//        if (treeList != null && treeList.size() > 0) {
//            queryWrapper.in("compositeTime", treeList);
//        }
        String dateBegin = conditions.getDateBegin();
        if (StringUtils.isNotBlank(dateBegin)) {
            queryWrapper.ge("compositeTime", dateBegin);
        }
        String dateEnd = conditions.getDateEnd();
        if (StringUtils.isNotBlank(dateEnd)) {
            queryWrapper.le("compositeTime", dateEnd);
        }
        String inspectionSeq = conditions.getInspectionSeq();
        if (StringUtils.isNotBlank(inspectionSeq)) {
            queryWrapper.like("inspectionSeq", inspectionSeq);
        }
        String carriageId = conditions.getCarriageId();
        if (StringUtils.isNotBlank(carriageId)) {
            queryWrapper.like("carriageId", carriageId);
        }
        String imageId = conditions.getImageId();
        if (StringUtils.isNotBlank(imageId)) {
            queryWrapper.like("id", imageId);
        }
        String model = conditions.getModel();
        if (StringUtils.isNotBlank(model)) {
            queryWrapper.eq("model", model.toUpperCase());
        }
        String cameraNumber = conditions.getCameraNumber();
        if (StringUtils.isNotBlank(cameraNumber)) {
            queryWrapper.eq("cameraNumber", cameraNumber);
        }
        System.out.println(queryWrapper.getTargetSql());
        return queryWrapper;
    }
}




