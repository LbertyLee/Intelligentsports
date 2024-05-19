package org.dromara.teachers.service;

import org.dromara.common.core.domain.R;
import org.dromara.teachers.domain.entity.HealthMetrics;
import org.dromara.teachers.domain.vo.HealthMetricsVo;
import org.dromara.teachers.domain.vo.TaskHealthMetricsVo;

import java.util.List;

public interface HealthMetricsService {

    int add(HealthMetrics healthMetrics);

    List<List<TaskHealthMetricsVo>>selectHealthMetricsListByBraceletsIdList(List<String> braceletsIdList,Long time);

}
