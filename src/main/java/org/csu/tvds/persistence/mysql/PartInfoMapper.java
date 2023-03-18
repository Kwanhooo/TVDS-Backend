package org.csu.tvds.persistence.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.tvds.entity.mysql.PartInfo;

/**
 * @author kwanho
 */
public interface PartInfoMapper extends BaseMapper<PartInfo> {

    int selectDistinctDefectCount();

    void deleteAll();
}




