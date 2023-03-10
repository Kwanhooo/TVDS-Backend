package org.csu.tvds.service.impl;

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
        boolean dateFilterNeeded = false;
        // init
        PaginationVO<List<PartInfo>> result = new PaginationVO<>();
        QueryWrapper<PartInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime");
        // resolve conditions
        if (conditions != null) {
            queryWrapper = new QueryWrapper<>();
            List<String> treeList = conditions.getTreeList();

            if (treeList != null && treeList.size() > 0) {
                for (String s : treeList) {
                    String[] split = s.split("_");
                    String model = split[0];
                    String partName = split[1];
                    String inspectionSeq = split[2];
                    queryWrapper.or(qw -> qw
                            .eq("model", model)
                            .eq("partName", partName)
                            .eq("inspectionSeq", inspectionSeq)
                    );
                }
            }
        }
        // do search
        Page<PartInfo> page = new Page<>(currentPage, pageSize);
        IPage<PartInfo> iPage = this.page(page, queryWrapper);
        List<PartInfo> records = iPage.getRecords();
        List<PartInfo> voList = new ArrayList<>();
        // convert vo
        for (PartInfo record : records) {
            if (conditions != null && !StringUtils.isAnyBlank(conditions.getDateBegin(), conditions.getDateEnd())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime dateTimeBegin = LocalDateTime.parse(conditions.getDateBegin(), formatter);
                LocalDateTime dateTimeEnd = LocalDateTime.parse(conditions.getDateEnd(), formatter);
                if (record.getCreateTime().isBefore(dateTimeBegin) || record.getCreateTime().isAfter(dateTimeEnd)) {
                    continue;
                }
            }
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
            // ???tree??????????????????model-partName???????????????inspectionSeq??????????????????inspectionSeq???????????????????????????
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
        // ??????tree???????????????????????????
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

    private String getPartNameWithTemplateId(String id) {
        // x70_wheel_v1
        String[] split = id.split("_");
        return split[1];
    }

    private String mapPartNameE2C(String partName) {
        switch (partName) {
            case "wheel":
                return "??????";
            case "bearing":
                return "??????";
            case "spring":
                return "??????";
            default:
                return partName;
        }
    }

    public String mapPartNameC2E(String partName) {
        switch (partName) {
            case "??????":
                return "wheel";
            case "??????":
                return "bearing";
            case "??????":
                return "spring";
            default:
                return partName;
        }
    }
}




