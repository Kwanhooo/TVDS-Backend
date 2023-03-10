package org.csu.tvds.controller;

import org.csu.tvds.common.CommonResponse;
import org.csu.tvds.entity.mysql.OriginImage;
import org.csu.tvds.entity.mysql.PartInfo;
import org.csu.tvds.models.dto.CarriageRetrieveConditions;
import org.csu.tvds.models.dto.OriginRetrieveConditions;
import org.csu.tvds.models.dto.PartRetrieveConditions;
import org.csu.tvds.models.vo.CarriageOverviewVO;
import org.csu.tvds.models.vo.PaginationVO;
import org.csu.tvds.service.CompositeAlignedImageService;
import org.csu.tvds.service.OriginImageService;
import org.csu.tvds.service.PartInfoService;
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
    private OriginImageService originImageService;
    @Resource
    private CompositeAlignedImageService compositeAlignedImageService;
    @Resource
    private PartInfoService partInfoService;

    /**
     * 检索原始图片
     *
     * @param currentPage 页码
     * @param pageSize    每页大小
     * @param conditions  检索条件
     * @return 原始图片检索结果
     */
    @PostMapping("/origin/{currentPage}/{pageSize}")
    public CommonResponse<PaginationVO<?>> retrieveOrigins(
            @PathVariable String currentPage,
            @PathVariable String pageSize,
            @RequestBody(required = false) OriginRetrieveConditions conditions
    ) {
        System.out.println(conditions);
        PaginationVO<List<OriginImage>> result;
        result = originImageService.getOverviews(conditions, Long.parseLong(currentPage), Long.parseLong(pageSize));
        return CommonResponse.createForSuccess(result);
    }

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

    /**
     * 检索零件
     *
     * @param currentPage 页码
     * @param pageSize    每页大小
     * @param conditions  检索条件
     * @return 检索结果
     */
    @PostMapping("/part/{currentPage}/{pageSize}")
    public CommonResponse<PaginationVO<?>> retrieveParts(
            @PathVariable String currentPage,
            @PathVariable String pageSize,
            @RequestBody(required = false) PartRetrieveConditions conditions
    ) {
        System.out.println(conditions);
        PaginationVO<List<PartInfo>> result;
        result = partInfoService.getOverviews(conditions, Long.parseLong(currentPage), Long.parseLong(pageSize));
        return CommonResponse.createForSuccess(result);
    }

}
