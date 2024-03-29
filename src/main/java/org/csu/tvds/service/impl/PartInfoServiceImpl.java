package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.entity.mysql.TemplatesLib;
import org.csu.tvds.models.dto.PartRetrieveConditions;
import org.csu.tvds.models.structure.Node;
import org.csu.tvds.models.vo.CatalogTreeVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.models.vo.PartOverviewVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.csu.tvds.persistence.mysql.TemplatesLibMapper;
import org.csu.tvds.service.PartInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kwanho
 */
@Service
public class PartInfoServiceImpl extends ServiceImpl<PartInfoMapper, PartInfo>
        implements PartInfoService {

    @Resource
    private TemplatesLibMapper templatesLibMapper;
    @Resource
    private PartInfoMapper partInfoMapper;
    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;

    @Override
    public PaginationVO<List<PartInfo>> getOverviews(PartRetrieveConditions conditions, long currentPage, long pageSize) {
        LambdaQueryWrapper<PartInfo> wrapper = new LambdaQueryWrapper<>();
        if (conditions != null) {
            Integer status = conditions.getStatus();
            if (status != null && status != -1) {
                wrapper.eq(PartInfo::getStatus, status);
            }
            String dateBegin = conditions.getDateBegin();
            String dateEnd = conditions.getDateEnd();
            if (!StringUtils.isAnyBlank(dateBegin, dateEnd)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime dateTimeBegin = LocalDateTime.parse(dateBegin, formatter);
                LocalDateTime dateTimeEnd = LocalDateTime.parse(dateEnd, formatter);
                wrapper.between(PartInfo::getCreateTime, dateTimeBegin, dateTimeEnd);
            }
            List<String> treeList = conditions.getTreeList();
            if (treeList != null && treeList.size() > 0) {
                wrapper.and(w -> {
                    for (String s : treeList) {
                        String[] split = s.split("_");
                        String model = split[0];
                        String partName = split[1];
                        String inspectionSeq = split[2];
                        w.or(qw -> qw
                                .eq(PartInfo::getModel, model)
                                .eq(PartInfo::getPartName, partName)
                                .eq(PartInfo::getInspectionSeq, inspectionSeq)
                        );
                    }
                });
            }
        }

        // do search
        PaginationVO<List<PartInfo>> result = new PaginationVO<>();
        Page<PartInfo> page = new Page<>(currentPage, pageSize);
        IPage<PartInfo> iPage = this.page(page, wrapper);
        List<PartInfo> records = iPage.getRecords();
        List<PartInfo> voList = new ArrayList<>();
        // convert vo
        for (PartInfo record : records) {
            PartOverviewVO vo = new PartOverviewVO();
            BeanUtils.copyProperties(record, vo);
            String compositeId = record.getCompositeId();
            CompositeAlignedImage compositeAlignedImage = compositeAlignedImageMapper.selectById(compositeId);
            vo.setCameraNo(compositeAlignedImage.getCameraNumber());
            vo.setCarriageId(compositeAlignedImage.getCarriageId());
            voList.add(vo);
        }
        // pagination info
        result.setCurrentPage(currentPage);
        result.setPageSize(pageSize);
        result.setTotalPage(iPage.getPages());
        result.setPage(voList);
        return result;
    }

    @Override
    public CatalogTreeVO buildCatalogTree() {
        AtomicInteger id = new AtomicInteger();
        CatalogTreeVO result = new CatalogTreeVO();
        List<Node> tree = new ArrayList<>();
        List<TemplatesLib> templates = templatesLibMapper.selectList(null);
        // model-partName
        for (TemplatesLib template : templates) {
            String model = template.getModel();
            String partName = getPartNameWithTemplateId(template.getId());
            // model
            Node modelNode = tree.stream()
                    .filter(node -> node.getLabel().equals(model))
                    .findFirst()
                    .orElse(null);
            if (modelNode == null) {
                modelNode = new Node();
                modelNode.setLabel(model);
                modelNode.setId(model);
                tree.add(modelNode);
            }
            // partName
            Node partNameNode = modelNode.getChildren().stream()
                    .filter(node -> node.getLabel().equals(partName))
                    .findFirst()
                    .orElse(null);
            if (partNameNode == null) {
                partNameNode = new Node();
                partNameNode.setLabel(partName);
                partNameNode.setId(partName);
                modelNode.getChildren().add(partNameNode);
            }
        }

        // inspectionSeq
        List<PartInfo> parts = partInfoMapper.selectList(null);
        parts.forEach(part -> {
            String model = part.getModel();
            String partName = part.getPartName();
            String inspectionSeq = String.valueOf(part.getInspectionSeq());
            // 在tree中找到对应的model-partName，然后添加inspectionSeq，如果相同的inspectionSeq已经存在，则不添加
            Node modelNode = tree.stream()
                    .filter(node -> node.getLabel().equals(model))
                    .findFirst()
                    .orElse(null);
            if (modelNode != null) {
                Node partNameNode = modelNode.getChildren().stream()
                        .filter(node -> node.getLabel().equals(partName))
                        .findFirst()
                        .orElse(null);
                if (partNameNode != null) {
                    Node inspectionSeqNode = partNameNode.getChildren().stream()
                            .filter(node -> node.getLabel().equals(inspectionSeq))
                            .findFirst()
                            .orElse(null);
                    if (inspectionSeqNode == null) {
                        inspectionSeqNode = new Node();
                        inspectionSeqNode.setLabel(inspectionSeq);
                        inspectionSeqNode.setId(part.getModel() + "_" + part.getPartName() + "_" + part.getInspectionSeq());
                        partNameNode.getChildren().add(inspectionSeqNode);
                    }
                }
            }
        });
        // 遍历tree，将中文转换为英文
        tree.forEach(modelNode -> {
            modelNode.getChildren().forEach(partNameNode -> {
                partNameNode.setLabel(mapPartNameE2C(partNameNode.getLabel()));
                partNameNode.getChildren().forEach(inspectionSeqNode -> {
                    inspectionSeqNode.setLabel(mapPartNameE2C(inspectionSeqNode.getLabel()));
                });
            });
        });
        result.setTree(tree);
        return result;
    }

    @Override
    public CompositeAlignedImage trackParentCarriage(String partId) {
        QueryWrapper<PartInfo> queryWrapper =
                new QueryWrapper<PartInfo>()
                        .eq("dbId", Long.parseLong(partId));
        PartInfo partInfo = this.getOne(queryWrapper);
        Long parentCarriageId = Long.parseLong(partInfo.getCompositeId());
        return compositeAlignedImageMapper.selectById(parentCarriageId);
    }

    private String getPartNameWithTemplateId(String id) {
        // x70_xxx_xxx
//        String[] split = id.split("_");
//        return split[1];

        int indexOfUnderscore = id.indexOf('_');
        return id.substring(indexOfUnderscore + 1);
    }

    private String mapPartNameE2C(String partName) {
        switch (partName) {
            case "wheel":
                return "车轮";
            case "bearing":
                return "轴承";
            case "spring":
                return "弹簧";
            default:
                return partName;
        }
    }

    public String mapPartNameC2E(String partName) {
        switch (partName) {
            case "车轮":
                return "wheel";
            case "轴承":
                return "bearing";
            case "弹簧":
                return "spring";
            default:
                return partName;
        }
    }
}



