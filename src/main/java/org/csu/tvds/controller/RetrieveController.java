package org.csu.tvds.controller;

import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.models.dto.CarriageRetrieveConditions;
import org.csu.tvds.models.vo.CarriageOverviewVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.service.CompositeAlignedImageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 几个表格的（按条件）检索
 */
@RestController
@RequestMapping("/retrieve")
public class RetrieveController {
    @Resource
    private CompositeAlignedImageService compositeAlignedImageService;

    /**
     * 检索车厢
     *
     * @param currentPage 页码
     * @param pageSize    每页大小
     * @param conditions  检索条件
     * @return 检索结果
     */
    @PostMapping("/carriage/{currentPage}/{pageSize}")
    public CommonResponse<PaginationVO<?>> retrieveCarriages(
            @PathVariable String currentPage,
            @PathVariable String pageSize,
            @RequestBody(required = false) CarriageRetrieveConditions conditions
    ) {
        PaginationVO<List<CarriageOverviewVO>> result;
        result = compositeAlignedImageService.getOverviews(conditions, Long.parseLong(currentPage), Long.parseLong(pageSize));
        return CommonResponse.createForSuccess(result);
    }

    @PostMapping("/carriage/byIds/{currentPage}/{pageSize}")
    public CommonResponse<PaginationVO<?>> retrieveCarriagesByIds(
            @PathVariable String currentPage,
            @PathVariable String pageSize,
            @RequestBody(required = false) List<String> ids
    ) {
        PaginationVO<List<CarriageOverviewVO>> result;
        result = compositeAlignedImageService.getOverviewsByIds(ids, Long.parseLong(currentPage), Long.parseLong(pageSize));
        return CommonResponse.createForSuccess(result);
    }

}
