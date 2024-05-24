package org.dromara.teachers.domain.vo;


import lombok.Data;
import org.dromara.common.tenant.core.TenantEntity;

/**
 * 日常健康指标表(TeacherDailyHealthMetrics)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-23 16:36:39
 */
@SuppressWarnings("serial")
@Data
public class DailyHealthMetricsVo extends TenantEntity {
    //唯一标识
    private Long id;
    //手环ID
    private String braceletId;
    //统计时间
    private Long statisticalTime;
    //最高血氧
    private Double maxBloodOxygen;
    //平均血氧
    private Double avgBloodOxygen;
    //最低血氧
    private Double minBloodOxygen;
    //最高心率
    private Double maxHeartRate;
    //平均心率
    private Double avgHeartRate;
    //最低心率
    private Double minHeartRate;
    //训练队ID
    private Long teamId;

}

