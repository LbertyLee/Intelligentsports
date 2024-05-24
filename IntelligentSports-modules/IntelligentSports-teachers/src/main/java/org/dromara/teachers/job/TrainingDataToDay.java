package org.dromara.teachers.job;

import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.teachers.domain.bo.DailyHealthMetricsBo;
import org.dromara.teachers.domain.bo.TimeSegmentBo;
import org.dromara.teachers.domain.vo.HealthMetricsVo;
import org.dromara.teachers.domain.vo.LineBloodOxygenToDayVo;
import org.dromara.teachers.domain.vo.LineHeartRateToDayVo;
import org.dromara.teachers.service.DailyHealthMetricsService;
import org.dromara.teachers.service.HealthMetricsService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TrainingDataToDay {

    @Resource
    private final HealthMetricsService healthMetricsService;

    @Resource
    private final DailyHealthMetricsService dailyHealthMetricsService;


    public TrainingDataToDay(HealthMetricsService healthMetricsService, DailyHealthMetricsService dailyHealthMetricsService) {
        this.healthMetricsService = healthMetricsService;
        this.dailyHealthMetricsService = dailyHealthMetricsService;
    }

    //    @Scheduled(fixedRate = 5000)
//    @Scheduled(cron = "0 0 1 * * ?")
    public void trainingDataToDay() {
        if (log.isInfoEnabled()) {
            log.info("定时处理数据-任务开始");
        }
        TimeSegmentBo timeStamps = new TimeSegmentBo();
        List<HealthMetricsVo> healthMetricsVos = healthMetricsService.selectDataWithinTimeRange(timeStamps.getOneHourAgo(), timeStamps.getCurrentTime(), null);
        if (ObjectUtil.isEmpty(healthMetricsVos)) {
            if (log.isInfoEnabled()) {
                log.info("定时处理数据-任务结束：处理数据[{}]条.", 0);
            }
            return;
        }
        Map<String, List<HealthMetricsVo>> groupedMetrics = healthMetricsVos.stream().collect(Collectors.groupingBy(HealthMetricsVo::getUuid));
        groupedMetrics.forEach((uuid, metrics) -> {
            processMetrics(uuid, metrics, timeStamps.getCurrentTime());
        });
        if (log.isInfoEnabled()) {
            log.info("定时处理数据-任务结束：处理数据[{}]条.", healthMetricsVos.size());
        }
    }

    private void processMetrics(String uuid, List<HealthMetricsVo> metrics, long currentTime) {
        LineBloodOxygenToDayVo lineBloodOxygenVo = calculateBloodOxygenMetrics(metrics);
        lineBloodOxygenVo.setStatisticalTime(String.valueOf(currentTime));
        LineHeartRateToDayVo lineHeartRateVo = calculateHeartRateMetrics(metrics);
        lineHeartRateVo.setStatisticalTime(String.valueOf(currentTime));
        DailyHealthMetricsBo dailyHealthMetricsBo = new DailyHealthMetricsBo()
            .setStatisticalTime(currentTime).setAvgBloodOxygen(lineBloodOxygenVo.getAvgBloodOxygen())
            .setAvgHeartRate(lineHeartRateVo.getAvgHeartRate())
            .setMaxBloodOxygen(lineBloodOxygenVo.getMaxBloodOxygen())
            .setMaxHeartRate(lineHeartRateVo.getMaxHeartRate())
            .setMinBloodOxygen(lineBloodOxygenVo.getMinBloodOxygen())
            .setMinHeartRate(lineHeartRateVo.getMinHeartRate())
            .setBraceletId(uuid);
        dailyHealthMetricsService.insert(dailyHealthMetricsBo);

    }

    private LineBloodOxygenToDayVo calculateBloodOxygenMetrics(List<HealthMetricsVo> metrics) {
        double avg = metrics.stream().mapToDouble(HealthMetricsVo::getBloodOxygen).average().orElse(0);
        double max = metrics.stream().mapToDouble(HealthMetricsVo::getBloodOxygen).max().orElse(0);
        double min = metrics.stream().mapToDouble(HealthMetricsVo::getBloodOxygen).min().orElse(0);
        LineBloodOxygenToDayVo vo = new LineBloodOxygenToDayVo();
        vo.setAvgBloodOxygen(avg);
        vo.setMaxBloodOxygen(max);
        vo.setMinBloodOxygen(min);
        return vo;
    }

    private LineHeartRateToDayVo calculateHeartRateMetrics(List<HealthMetricsVo> metrics) {
        double avg = metrics.stream().mapToDouble(HealthMetricsVo::getHeartRate).average().orElse(0);
        double max = metrics.stream().mapToDouble(HealthMetricsVo::getHeartRate).max().orElse(0);
        double min = metrics.stream().mapToDouble(HealthMetricsVo::getHeartRate).min().orElse(0);
        LineHeartRateToDayVo vo = new LineHeartRateToDayVo();
        vo.setAvgHeartRate(avg);
        vo.setMaxHeartRate(max);
        vo.setMinHeartRate(min);
        return vo;
    }

    private void storeMetrics(String uuid, long currentTime, Object metricsVo, String cacheKey) {
        String key = uuid + "." + currentTime;
        HashMap<String, Object> cacheMap = new HashMap<>();
        cacheMap.put(key, metricsVo);
        RedisUtils.setCacheMap(cacheKey, cacheMap);
        RedisUtils.expire(cacheKey, 86400);
    }
}
