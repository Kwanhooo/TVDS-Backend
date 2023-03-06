package org.csu.tvds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.tvds.entity.mysql.OriginImage;
import org.csu.tvds.models.dto.OriginRetrieveConditions;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.models.vo.DateTreeVO;

import java.util.List;

/**
 * @author kwanho
 */
public interface OriginImageService extends IService<OriginImage> {

    PaginationVO<List<OriginImage>> getOverviews(OriginRetrieveConditions conditions, long currentPage, long pageSize);

    DateTreeVO buildDateTree();
}
