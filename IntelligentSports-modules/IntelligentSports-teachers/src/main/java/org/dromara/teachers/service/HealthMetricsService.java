package org.dromara.teachers.service;

import org.dromara.common.core.domain.R;
import org.dromara.teachers.domain.entity.HealthMetrics;
import org.dromara.teachers.domain.vo.HealthMetricsVo;
import org.dromara.teachers.domain.vo.TaskHealthMetricsVo;

import java.util.List;
import java.util.Map;

public interface HealthMetricsService {

    int add(HealthMetrics healthMetrics);

    List<List<TaskHealthMetricsVo>>selectHealthMetricsListByBraceletsIdList(List<String> braceletsIdList,Long time);

    List<TaskHealthMetricsVo> selectHealthMetricsListByBraceletsId(String braceletId, long time);

    List<HealthMetricsVo> selectDataWithinTimeRange(long startTime, long endTime, List<String> isOnlineBraceletsIdList);

    void insert(String msg);

    Map<String, List<TaskHealthMetricsVo>> selectHealthMetricsMapByBraceletsIdList(List<String> braceletsTotalNum, long time);
}
