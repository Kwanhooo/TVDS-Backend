package org.csu.tvds.models.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CarriageOverviewVO extends CompositeAlignedImage {
    private String url;
    private List<PartCountVO> partCount;
}
