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

    @Override
    public int add(HealthMetrics healthMetrics) {
        healthMetrics.setTimestamp(System.currentTimeMillis()/1000L);
        return healthMetricsMapper.insert(healthMetrics);
    }

    @Override
    public List<List<TaskHealthMetricsVo>> selectHealthMetricsListByBraceletsIdList(List<String> braceletsIdList,Long time) {
        if(log.isInfoEnabled()){
        log.info("HealthMetricsServiceImpl.selectHealthMetricsListByBraceletsIdList.braceletsIdList:{}",braceletsIdList);
        }
        //获取实时数据
        return braceletsIdList.stream()
            .map(bracelet-> healthMetricsMapper.selectHealthMetricsListByBraceletsId(bracelet,time))
            .toList();
    }

    @Override
    public List<TaskHealthMetricsVo> selectHealthMetricsListByBraceletsId(String braceletId, long time) {
        if(log.isInfoEnabled()){
            log.info("HealthMetricsServiceImpl.selectHealthMetricsListByBraceletsId.braceletId:{}",braceletId);
        }
        return healthMetricsMapper.selectHealthMetricsListByBraceletsId(braceletId,time);
    }

}
