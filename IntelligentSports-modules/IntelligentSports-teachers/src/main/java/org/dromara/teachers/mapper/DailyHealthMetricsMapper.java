package org.dromara.teachers.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.teachers.domain.entity.DailyHealthMetrics;
import org.dromara.teachers.domain.vo.DailyHealthMetricsVo;

import java.util.List;

/**
 * 日常健康指标表(TeacherDailyHealthMetrics)表数据库访问层
 *
 * @author LbertyLee
 * @since 2024-05-23 16:36:38
 */
@Mapper
public interface DailyHealthMetricsMapper extends BaseMapperPlus<DailyHealthMetrics, DailyHealthMetricsVo> {

    List<DailyHealthMetricsVo> selectBatchBraceletsIdList(@Param("braceletsIdList") List<String> braceletsIdList);

}

