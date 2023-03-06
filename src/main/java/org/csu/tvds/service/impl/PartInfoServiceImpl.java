package org.csu.tvds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.entity.mysql.TemplatesLib;
import org.csu.tvds.models.dto.PartRetrieveConditions;
import org.csu.tvds.models.structure.Node;
import org.csu.tvds.models.vo.CatalogTreeVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.persistence.mysql.PartInfoMapper;
import org.csu.tvds.persistence.mysql.TemplatesLibMapper;
import org.csu.tvds.service.PartInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
        result.setPage(records);
        return result;
    }

    @Override
    public CatalogTreeVO buildCatalogTree() {
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
                        partNameNode.getChildren().add(inspectionSeqNode);
                    }
                }
            }
        });
        result.setTree(tree);
        return result;
    }

    private String getPartNameWithTemplateId(String id) {
        // x70_wheel_v1
        String[] split = id.split("_");
        return split[1];
    }
}




