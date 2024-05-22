package org.dromara.teachers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.teachers.domain.entity.HealthMetrics;
import org.dromara.teachers.domain.vo.HealthMetricsVo;
import org.dromara.teachers.domain.vo.TaskHealthMetricsVo;
import org.dromara.teachers.mapper.HealthMetricsMapper;
import org.dromara.teachers.service.HealthMetricsService;
import org.dromara.teachers.service.TaskHealthMetricsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthMetricsServiceImpl implements HealthMetricsService {

    private final HealthMetricsMapper healthMetricsMapper;

    private final TaskHealthMetricsService taskHealthMetricsService;

    /**
     * 向健康指标数据表中添加一条新记录。
     *
     * @param healthMetrics 健康指标对象，包含各项健康数据。
     * @return 返回添加成功的记录数。
     */
    @Override
    public int add(HealthMetrics healthMetrics) {
        healthMetrics.setTimestamp(System.currentTimeMillis()/1000L);
        return healthMetricsMapper.insert(healthMetrics);
    }

    /**
     * 根据手环ID列表和时间查询健康指标数据列表。
     *
     * @param braceletsIdList 手环ID的列表，用于查询对应的健康指标数据。
     * @param time 时间戳，用于查询指定时间的健康指标数据。
     * @return 返回一个包含多个手环健康指标数据的列表。每个手环的健康指标数据以List<TaskHealthMetricsVo>>的形式返回。
     */
    @Override
    public List<List<TaskHealthMetricsVo>> selectHealthMetricsListByBraceletsIdList(List<String> braceletsIdList, Long time) {
        if(log.isInfoEnabled()){
            log.info("HealthMetricsServiceImpl.selectHealthMetricsListByBraceletsIdList.braceletsIdList:{}",braceletsIdList);
        }
        // 通过流处理每个手环ID，查询其对应的健康指标数据，并收集到一个列表中
        return braceletsIdList.stream()
            .map(bracelet -> healthMetricsMapper.selectHealthMetricsListByBraceletsId(bracelet, time))
            .toList();
    }

    /**
     * 根据手环ID和时间查询健康指标列表
     * @param braceletId 手环的唯一标识符
     * @param time 指定的时间戳
     * @return 返回一个健康指标任务的视图对象列表，这些任务与指定的手环ID和时间相关联
     */
    @Override
    public List<TaskHealthMetricsVo> selectHealthMetricsListByBraceletsId(String braceletId, long time) {
        // 如果日志级别允许信息输出，则记录查询开始的日志信息
        if(log.isInfoEnabled()){
            log.info("HealthMetricsServiceImpl.selectHealthMetricsListByBraceletsId.braceletId:{}",braceletId);
        }
        // 调用mapper层，根据手环ID和时间查询对应的健康指标列表
        return healthMetricsMapper.selectHealthMetricsListByBraceletsId(braceletId,time);
    }

    @Override
    public List<HealthMetricsVo> selectDataWithinTimeRange(long startTime, long endTime, List<String> isOnlineBraceletsIdList) {
        return healthMetricsMapper.selectDataWithinTimeRange(startTime,endTime,isOnlineBraceletsIdList);
    }

}
