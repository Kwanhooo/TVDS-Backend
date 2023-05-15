package org.csu.tvds.persistence.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.tvds.entity.mysql.DefectInfo;

/**
 * @author kwanho
 */
public interface DefectInfoMapper extends BaseMapper<DefectInfo> {

    void deleteAll();
}




