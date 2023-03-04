package org.csu.tvds.models.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;

@EqualsAndHashCode(callSuper = true)
@Data
public class CarriageOverviewVO extends CompositeAlignedImage {
    private String url;
}
