package org.csu.tvds.persistence.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.tvds.entity.mysql.CompositeAlignedImage;

import java.time.LocalDate;
import java.util.List;

/**
 * @author kwanho
 */
public interface CompositeAlignedImageMapper extends BaseMapper<CompositeAlignedImage> {

    List<LocalDate> selectDistinctDate();

    List<String> selectDistinctInspectionSeq();

    List<String> selectDistinctDetectedInspectionSeq();

    void rollbackStatus();
}




