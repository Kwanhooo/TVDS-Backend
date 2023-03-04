package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.models.dto.CarriageRetrieveConditions;
import org.csu.tvds.models.vo.CarriageOverviewVO;
import org.csu.tvds.models.vo.CarriageTreeNodeVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.service.CompositeAlignedImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kwanho
 */
@Service
public class CompositeAlignedImageServiceImpl extends ServiceImpl<CompositeAlignedImageMapper, CompositeAlignedImage>
        implements CompositeAlignedImageService {

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
        result.setContent(new ArrayList<>());
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
            result.getContent().add(vo);
        });
        return result;
    }

    /**
     * 根据车厢检索条件构建目录树
     *
     * @param conditions 检索条件
     * @return 目录树
     */
    @Override
    public CarriageTreeNodeVO buildTree(CarriageRetrieveConditions conditions) {
        // 1. resolve conditions to build query wrapper
        QueryWrapper<CompositeAlignedImage> queryWrapper = null;
        if (conditions != null) {
            queryWrapper = this.buildQueryWrapperByConditions(conditions);
        }
        List<CompositeAlignedImage> records = this.list(queryWrapper);
        // 2. build tree (model-compositeTime-inspectionSeq-carriageNo)
        CarriageTreeNodeVO root = new CarriageTreeNodeVO();
        root.setLabel("ROOT");
        records.forEach(r -> {
            // model
            String model = r.getModel();
            CarriageTreeNodeVO modelNode = root.getChildren().stream()
                    .filter(c -> c.getLabel().equals(model))
                    .findFirst()
                    .orElse(null);
            if (modelNode == null) {
                modelNode = new CarriageTreeNodeVO();
                modelNode.setLabel(model);
                root.getChildren().add(modelNode);
            }
            // compositeTime
            String compositeTime = String.valueOf(r.getCompositeTime());
            CarriageTreeNodeVO compositeTimeNode = modelNode.getChildren().stream()
                    .filter(c -> c.getLabel().equals(compositeTime))
                    .findFirst()
                    .orElse(null);
            if (compositeTimeNode == null) {
                compositeTimeNode = new CarriageTreeNodeVO();
                compositeTimeNode.setLabel(compositeTime);
                modelNode.getChildren().add(compositeTimeNode);
            }
            // inspectionSeq
            String inspectionSeq = String.valueOf(r.getInspectionSeq());
            CarriageTreeNodeVO inspectionSeqNode = compositeTimeNode.getChildren().stream()
                    .filter(c -> c.getLabel().equals(inspectionSeq))
                    .findFirst()
                    .orElse(null);
            if (inspectionSeqNode == null) {
                inspectionSeqNode = new CarriageTreeNodeVO();
                inspectionSeqNode.setLabel(inspectionSeq);
                compositeTimeNode.getChildren().add(inspectionSeqNode);
            }
            // carriageNo
            String carriageNo = String.valueOf(r.getCarriageNo());
            CarriageTreeNodeVO carriageNoNode = inspectionSeqNode.getChildren().stream()
                    .filter(c -> c.getLabel().equals(carriageNo))
                    .findFirst()
                    .orElse(null);
            if (carriageNoNode == null) {
                carriageNoNode = new CarriageTreeNodeVO();
                carriageNoNode.setLabel(carriageNo);
                inspectionSeqNode.getChildren().add(carriageNoNode);
            }
            // id
            String id = r.getId();
            CarriageTreeNodeVO idNode = carriageNoNode.getChildren().stream()
                    .filter(c -> c.getLabel().equals(id))
                    .findFirst()
                    .orElse(null);
            if (idNode == null) {
                idNode = new CarriageTreeNodeVO();
                idNode.setLabel(id);
                carriageNoNode.getChildren().add(idNode);
            }
        });
        return root;
    }

    /**
     * 根据车厢id列表获取车厢检索结果
     *
     * @param ids         车厢id列表
     * @param currentPage 页码
     * @param pageSize    每页大小
     * @return 检索结果
     */
    @Override
    public PaginationVO<List<CarriageOverviewVO>> getOverviewsByIds(List<String> ids, long currentPage, long pageSize) {
        LambdaQueryChainWrapper<CompositeAlignedImage> queryWrapper = this.lambdaQuery();
        queryWrapper.in(CompositeAlignedImage::getId, ids);
        Page<CompositeAlignedImage> page = new Page<>(currentPage, pageSize);
        IPage<CompositeAlignedImage> iPage = queryWrapper.page(page);
        List<CompositeAlignedImage> records = iPage.getRecords();
        // set pagination info
        PaginationVO<List<CarriageOverviewVO>> result = new PaginationVO<>();
        result.setCurrentPage(currentPage);
        result.setPageSize(pageSize);
        result.setTotalPage(iPage.getPages());
        result.setContent(new ArrayList<>());
        // convert
        records.forEach(r -> {
            CarriageOverviewVO vo = new CarriageOverviewVO();
            BeanUtils.copyProperties(r, vo);
            vo.setUrl(r.getCompositeUrl());
            vo.setCompositeUrl(null);
            result.getContent().add(vo);
        });
        return result;
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
        String inspectionSeq = conditions.getInspectionSeq();
        String carriageNo = conditions.getCarriageNo();
        String id = conditions.getId();
        if (!StringUtils.isAnyBlank(startDate, endDate)) {
            queryWrapper.between("compositeTime", startDate, endDate);
        }
        if (StringUtils.isNotBlank(inspectionSeq)) {
            queryWrapper.like("inspectionSeq", inspectionSeq);
        }
        if (StringUtils.isNotBlank(carriageNo)) {
            queryWrapper.like("carriageNo", carriageNo);
        }
        if (StringUtils.isNotBlank(id)) {
            queryWrapper.like("id", id);
        }
        return queryWrapper;
    }
}




