package org.csu.tvds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;
import org.csu.tvds.models.dto.CarriageRetrieveConditions;
import org.csu.tvds.models.vo.CarriageOverviewVO;
import org.csu.tvds.models.vo.DateTreeVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.models.vo.PartCountVO;

import java.util.List;

/**
 * @author kwanho
 */
public interface CompositeAlignedImageService extends IService<CompositeAlignedImage> {

    PaginationVO<List<CarriageOverviewVO>> getOverviews(CarriageRetrieveConditions conditions, long currentPage, long pageSize);

    List<PartCountVO> getPartCount(Long id);

    DateTreeVO buildDateTree();

}
