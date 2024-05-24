package org.dromara.teachers.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.teachers.domain.entity.HealthMetrics;
import org.dromara.teachers.domain.vo.HealthMetricsVo;
import org.dromara.teachers.domain.vo.TaskHealthMetricsVo;

import java.util.List;


@Mapper
public interface HealthMetricsMapper  extends BaseMapperPlus<HealthMetrics, HealthMetricsVo> {


    List<TaskHealthMetricsVo> selectHealthMetricsListByBraceletsId(@Param("uuid") String braceletsId, @Param("time") long time );


    List<HealthMetricsVo> selectDataWithinTimeRange(@Param("startTime") long startTime,
                                                    @Param("endTime") long endTime,
                                                    @Param("uuids") List<String> uuids);
}
