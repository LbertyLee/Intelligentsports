package org.dromara.teachers.service;


import org.dromara.teachers.domain.bo.DailyHealthMetricsBo;
import org.dromara.teachers.domain.vo.DailyHealthMetricsVo;

import java.util.List;

/**
 * 日常健康指标表(TeacherDailyHealthMetrics)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-23 16:36:40
 */
public interface DailyHealthMetricsService  {

    int insert(DailyHealthMetricsBo dailyHealthMetricsBo);

    List<DailyHealthMetricsVo> selectListByBraceletsIdList(List<String> braceletsIdList);

}

