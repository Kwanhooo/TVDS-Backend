package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.DefectInfo;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.entity.mysql.TemplatesLib;
import org.csu.tvds.models.dto.DefectRetrieveConditions;
import org.csu.tvds.models.structure.Node;
import org.csu.tvds.models.vo.CatalogTreeVO;
import org.csu.tvds.models.vo.DefectOverviewVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.persistence.mysql.CompositeAlignedImageMapper;
import org.csu.tvds.persistence.mysql.DefectInfoMapper;
import org.csu.tvds.persistence.mysql.TemplatesLibMapper;
import org.csu.tvds.service.DefectInfoService;
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
public class DefectInfoServiceImpl extends ServiceImpl<DefectInfoMapper, DefectInfo>
        implements DefectInfoService {

    @Resource
    private TemplatesLibMapper templatesLibMapper;
    @Resource
    private DefectInfoMapper defectInfoMapper;
    @Resource
    private CompositeAlignedImageMapper compositeAlignedImageMapper;

    @Override
    public PaginationVO<List<DefectInfo>> getOverviews(DefectRetrieveConditions conditions, long currentPage, long pageSize) {
        LambdaQueryWrapper<DefectInfo> wrapper = new LambdaQueryWrapper<>();
        if (conditions != null) {
            String dateBegin = conditions.getDateBegin();
            String dateEnd = conditions.getDateEnd();
            if (!StringUtils.isAnyBlank(dateBegin, dateEnd)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime dateTimeBegin = LocalDateTime.parse(dateBegin, formatter);
                LocalDateTime dateTimeEnd = LocalDateTime.parse(dateEnd, formatter);
                wrapper.between(DefectInfo::getCreateTime, dateTimeBegin, dateTimeEnd);
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
                                .eq(DefectInfo::getModel, model)
                                .eq(DefectInfo::getPartName, partName)
                                .eq(DefectInfo::getInspectionSeq, inspectionSeq)
                        );
                    }
                });
            }
        }

        // do search
        PaginationVO<List<DefectInfo>> result = new PaginationVO<>();
        Page<DefectInfo> page = new Page<>(currentPage, pageSize);
        IPage<DefectInfo> iPage = this.page(page, wrapper);
        List<DefectInfo> records = iPage.getRecords();
        List<DefectInfo> voList = new ArrayList<>();
        // convert vo
        for (DefectInfo record : records) {
            DefectOverviewVO vo = new DefectOverviewVO();
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
        List<DefectInfo> parts = defectInfoMapper.selectList(null);
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
    public void newDetection(PartInfo partInfo) {
        DefectInfo defectInfo = new DefectInfo();
        BeanUtils.copyProperties(partInfo, defectInfo);
        this.save(defectInfo);
    }

    private String getPartNameWithTemplateId(String id) {
        // x70_wheel_v1
        String[] split = id.split("_");
        return split[1];
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



