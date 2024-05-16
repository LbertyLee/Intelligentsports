package org.dromara.teachers.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.teachers.domain.entity.HealthMetrics;
import org.dromara.teachers.domain.vo.HealthMetricsVo;


@Mapper
public interface HealthMetricsMapper  extends BaseMapperPlus<HealthMetrics, HealthMetricsVo> {


}
