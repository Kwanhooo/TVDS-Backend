package org.csu.tvds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.entity.mysql.DefectInfo;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.models.dto.DefectRetrieveConditions;
import org.csu.tvds.models.vo.CatalogTreeVO;
import org.csu.tvds.models.vo.PaginationVO;

import java.util.List;

/**
 * @author kwanho
 */
public interface DefectInfoService extends IService<DefectInfo> {

    PaginationVO<List<DefectInfo>> getOverviews(String uid, DefectRetrieveConditions conditions, long currentPage, long pageSize);

    CatalogTreeVO buildCatalogTree();

    void newDetection(PartInfo partInfo);

    CompositeAlignedImage trackParentCarriage(String partId);
}
