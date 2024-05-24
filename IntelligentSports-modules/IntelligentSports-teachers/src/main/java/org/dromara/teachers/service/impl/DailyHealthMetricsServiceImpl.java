package org.dromara.teachers.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.teachers.domain.bo.DailyHealthMetricsBo;
import org.dromara.teachers.domain.entity.DailyHealthMetrics;
import org.dromara.teachers.domain.vo.DailyHealthMetricsVo;
import org.dromara.teachers.mapper.DailyHealthMetricsMapper;
import org.dromara.teachers.service.DailyHealthMetricsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日常健康指标表(TeacherDailyHealthMetrics)表服务实现类
 *
 * @author LbertyLee
 * @since 2024-05-23 16:36:40
 */
@Slf4j
@RequiredArgsConstructor
@Service("teacherDailyHealthMetricsService")
public class DailyHealthMetricsServiceImpl  implements DailyHealthMetricsService {

    private final DailyHealthMetricsMapper teacherDailyHealthMetricsMapper;

    @Override
    public int insert(DailyHealthMetricsBo dailyHealthMetricsBo) {
        if(log.isInfoEnabled()){
            log.info("DailyHealthMetricsServiceImpl.insert.dailyHealthMetricsBo={}", dailyHealthMetricsBo);
        }
        DailyHealthMetrics dailyHealthMetrics = BeanUtil.copyProperties(dailyHealthMetricsBo, DailyHealthMetrics.class);
        return teacherDailyHealthMetricsMapper.insert(dailyHealthMetrics);
    }

    @Override
    public List<DailyHealthMetricsVo> selectListByBraceletsIdList(List<String> braceletsIdList) {
        return teacherDailyHealthMetricsMapper.selectBatchBraceletsIdList(braceletsIdList);
    }
}

