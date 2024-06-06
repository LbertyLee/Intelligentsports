package org.dromara.teachers.job;


import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.teachers.service.DailyHealthMetricsService;
import org.dromara.teachers.service.HealthMetricsService;
import org.dromara.teachers.service.TaskHealthMetricsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CleanUpExpiredDataJob {

    private final HealthMetricsService healthMetricsService;
    private final DailyHealthMetricsService dailyHealthMetricsService;
    private final TaskHealthMetricsService taskHealthMetricsService;


    public void cleanUpExpiredData() {
        // 清理健康数据
//        healthMetricsService.cleanUpExpiredData();
        // 清理每日健康数据
//        dailyHealthMetricsService.cleanUpExpiredData();

    }
}
