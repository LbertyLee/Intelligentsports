package org.dromara.teachers.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 健康指标数据类，用于存储个人健康相关的指标信息。
 */
@Data
@TableName("teacher_health_metrics")
public class HealthMetrics {
    /**记录数据的时间戳*/
    private String timestamp;
    /**总步数*/
    private Long totalSteps;
    /**总热量消耗*/
    private Long totalCalories;
    /**总距离*/
    private Long totalDistance;
    /**总睡眠时间（分钟）*/
    private Long totalSleepMinutes;
     /**深度睡眠时间*/
    private Long deepSleep;
    /**轻度睡眠时间*/
    private Long lightSleep;
    /**心率*/
    private Long heartRate;
    /** 血压*/
    private Long bloodPressure;
    /**血氧饱和度*/
    private Long bloodOxygen;
    /**压力等级*/
    private Long stressLevel;
    /**代谢年龄*/
    private Long metabolicAge;
    /**身体年龄指数*/
    private Long mai;
    /**体温*/
    private Long bodyTemperature;
    /**血糖*/
    private Long bloodSugar;
    /**手环唯一标识符*/
    private String uuid;

}

