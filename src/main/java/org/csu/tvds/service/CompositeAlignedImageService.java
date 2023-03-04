package org.csu.tvds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.models.dto.CarriageRetrieveConditions;
import org.csu.tvds.models.vo.CarriageOverviewVO;
import org.csu.tvds.models.vo.CarriageTreeNodeVO;
import org.csu.tvds.models.vo.PaginationVO;

import java.util.List;

/**
 * @author kwanho
 */
public interface CompositeAlignedImageService extends IService<CompositeAlignedImage> {

    PaginationVO<List<CarriageOverviewVO>> getOverviews(CarriageRetrieveConditions conditions, long currentPage, long pageSize);

    CarriageTreeNodeVO buildTree(CarriageRetrieveConditions conditions);

    PaginationVO<List<CarriageOverviewVO>> getOverviewsByIds(List<String> ids, long currentPage, long pageSize);
}
