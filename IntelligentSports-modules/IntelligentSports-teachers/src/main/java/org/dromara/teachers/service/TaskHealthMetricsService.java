package org.dromara.teachers.service;


import org.dromara.teachers.domain.bo.TaskHealthMetricsBo;
import org.dromara.teachers.domain.vo.TaskHealthMetricsVo;

import java.util.List;

/**
 * 训练健康指数(TeacherTaskHealthMetrics)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-18 15:21:44
 */
public interface TaskHealthMetricsService  {

    void insertList(List<TaskHealthMetricsBo> taskHealthMetricsBos);

    int resetTaskHealthMetrics(Long taskId);

    List<TaskHealthMetricsVo> selectTaskHealthMetricsList(Long taskId, String studentId);

}

