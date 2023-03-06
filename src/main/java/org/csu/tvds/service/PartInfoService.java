package org.csu.tvds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.models.dto.PartRetrieveConditions;
import org.csu.tvds.models.vo.CatalogTreeVO;
import org.csu.tvds.models.vo.PaginationVO;

import java.util.List;

/**
 * @author kwanho
 */
public interface PartInfoService extends IService<PartInfo> {

    PaginationVO<List<PartInfo>> getOverviews(PartRetrieveConditions conditions, long currentPage, long pageSize);

    CatalogTreeVO buildCatalogTree();
}
