package org.dromara.teachers.service.impl;

import cn.hutool.core.util.IdUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.teachers.domain.entity.HealthMetrics;
import org.dromara.teachers.domain.vo.HealthMetricsVo;
import org.dromara.teachers.domain.vo.TaskHealthMetricsVo;
import org.dromara.teachers.mapper.HealthMetricsMapper;
import org.dromara.teachers.service.HealthMetricsService;
import org.dromara.teachers.service.TaskHealthMetricsService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        healthMetrics.setTimestamp(System.currentTimeMillis() / 1000L);
        return healthMetricsMapper.insert(healthMetrics);
    }

    /**
     * 根据手环ID列表和时间查询健康指标数据列表。
     *
     * @param braceletsIdList 手环ID的列表，用于查询对应的健康指标数据。
     * @param time            时间戳，用于查询指定时间的健康指标数据。
     * @return 返回一个包含多个手环健康指标数据的列表。每个手环的健康指标数据以List<TaskHealthMetricsVo>>的形式返回。
     */
    @Override
    public List<List<TaskHealthMetricsVo>> selectHealthMetricsListByBraceletsIdList(List<String> braceletsIdList, Long time) {
        if (log.isInfoEnabled()) {
            log.info("HealthMetricsServiceImpl.selectHealthMetricsListByBraceletsIdList.braceletsIdList:{}", braceletsIdList);
        }

        // 通过流处理每个手环ID，查询其对应的健康指标数据，并收集到一个列表中
        return braceletsIdList.stream()
            .map(bracelet -> healthMetricsMapper.selectHealthMetricsListByBraceletsId(bracelet, time))
            .toList();
    }

    @Override
    public Map<String, List<TaskHealthMetricsVo>> selectHealthMetricsMapByBraceletsIdList(List<String> braceletsTotalNum, long time) {
        if (log.isInfoEnabled()) {
            log.info("HealthMetricsServiceImpl.selectHealthMetricsMapByBraceletsIdList.braceletsIdList:{}", braceletsTotalNum);
        }
        HashMap<String, List<TaskHealthMetricsVo>> stringListHashMap = new HashMap<>();
        for (String braceletId : braceletsTotalNum){
            List<TaskHealthMetricsVo> taskHealthMetricsVos = healthMetricsMapper.selectHealthMetricsListByBraceletsId(braceletId, time);
            stringListHashMap.put(braceletId, taskHealthMetricsVos);
        }
        return stringListHashMap;
    }

    /**
     * 根据手环ID和时间查询健康指标列表
     *
     * @param braceletId 手环的唯一标识符
     * @param time       指定的时间戳
     * @return 返回一个健康指标任务的视图对象列表，这些任务与指定的手环ID和时间相关联
     */
    @Override
    public List<TaskHealthMetricsVo> selectHealthMetricsListByBraceletsId(String braceletId, long time) {
        // 如果日志级别允许信息输出，则记录查询开始的日志信息
        if (log.isInfoEnabled()) {
            log.info("HealthMetricsServiceImpl.selectHealthMetricsListByBraceletsId.braceletId:{}", braceletId);
        }
        // 调用mapper层，根据手环ID和时间查询对应的健康指标列表
        return healthMetricsMapper.selectHealthMetricsListByBraceletsId(braceletId, time);
    }

    @Override
    public List<HealthMetricsVo> selectDataWithinTimeRange(long startTime, long endTime, List<String> isOnlineBraceletsIdList) {
        return healthMetricsMapper.selectDataWithinTimeRange(startTime, endTime, isOnlineBraceletsIdList);
    }

    /**
     * 将传入的JSON字符串转换为HealthMetrics对象数组，并插入到数据库中。
     * 此方法首先使用Gson库将JSON字符串解析为HealthMetrics对象数组，然后为每个对象设置当前时间戳，
     * 最后将这些对象以批量插入的方式存储到数据库中。
     *
     * @param msg 包含HealthMetrics数据的JSON字符串。
     */
    @Override
    public void insert(String msg) {
        // 使用Gson从JSON字符串解析HealthMetrics对象数组
        Gson gson = new Gson();
        HealthMetrics[] healthMetrics = gson.fromJson(msg, HealthMetrics[].class);
        // 将解析出的HealthMetrics对象数组转换为流，为每个对象设置当前时间戳，并转换回List集合
        List<HealthMetrics> list = Arrays.stream(healthMetrics)
            .peek(e -> {
                    e.setTimestamp(System.currentTimeMillis() / 1000L);
                }
            ).toList();
        // 调用healthMetricsMapper的insertBatch方法，将处理后的HealthMetrics对象列表批量插入数据库
        healthMetricsMapper.insertBatch(list);
    }




}
