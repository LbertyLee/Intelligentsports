package org.dromara.teachers.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.teachers.domain.bo.TaskHealthMetricsBo;
import org.dromara.teachers.domain.entity.TaskHealthMetrics;
import org.dromara.teachers.domain.vo.TaskHealthMetricsVo;
import org.dromara.teachers.mapper.TaskHealthMetricsMapper;
import org.dromara.teachers.service.TaskHealthMetricsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 训练健康指数(TeacherTaskHealthMetrics)表服务实现类
 *
 * @author LbertyLee
 * @since 2024-05-18 15:21:44
 */
@Slf4j
@Service("teacherTaskHealthMetricsService")
@RequiredArgsConstructor
public class TeacherTaskHealthMetricsServiceImpl implements TaskHealthMetricsService {

    private final TaskHealthMetricsMapper taskHealthMetricsMapper;

    /**
     * 插入训练任务实时数据
     *
     * @param taskHealthMetricsBos
     * @author LbertyLee
     * @return int
     */
    @Override
    public void insertList(List<TaskHealthMetricsBo> taskHealthMetricsBos) {
        if (log.isInfoEnabled()) {
            log.info("TeacherTaskHealthMetricsServiceImpl.insertList.taskHealthMetricsBos{}", taskHealthMetricsBos);
        }
        List<TaskHealthMetrics> taskHealthMetricsList = MapstructUtils.convert(taskHealthMetricsBos, TaskHealthMetrics.class);
        taskHealthMetricsMapper.insertBatch(taskHealthMetricsList);
    }

    /**
     * 根据任务id重置任务健康指数
     *
     * @param taskId 任务id
     * @return
     */
    @Override
    public int resetTaskHealthMetrics(Long taskId) {
        if (log.isInfoEnabled()) {
            log.info("TeacherTaskHealthMetricsServiceImpl.resetTaskHealthMetrics.taskId:{}", taskId);
        }
        return taskHealthMetricsMapper
            .delete(new LambdaQueryWrapper<TaskHealthMetrics>().eq(TaskHealthMetrics::getTaskId, taskId));
    }

    @Override
    public List<TaskHealthMetricsVo> selectTaskHealthMetricsList(Long taskId, String braceletId) {
        if(log.isInfoEnabled()){
            log.info("TeacherTaskHealthMetricsServiceImpl.selectTaskHealthMetricsList.taskId:{},braceletId:{}", taskId,braceletId);
        }
        return taskHealthMetricsMapper.selectTaskHealthMetricsVoList(taskId,braceletId);
    }
}

