package org.dromara.teachers.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.teachers.domain.vo.HealthMetricsVo;

/**
 * 健康指标数据类，用于存储个人健康相关的指标信息。
 */
@Data
@TableName("teacher_health_metrics")
@AutoMapper(target = HealthMetricsVo.class)
public class HealthMetrics {

    /*** 主键ID*/
    private Long id;
    /*** 记录数据的时间戳*/
    private Long timestamp;
    /*** 总步数*/
    private Long totalSteps;
    /*** 总热量消耗*/
    private Long totalCalories;
    /*** 总距离单位（米）*/
    private Long totalDistance;
    /*** 总睡眠时间（分钟）*/
    private Long totalSleepMinutes;
    /*** 深度睡眠时间*/
    private Long deepSleep;
    /*** 轻度睡眠时间*/
    private Long lightSleep;
    /*** 心率*/
    private Long heartRate;
    /*** 血压*/
    private Long bloodPressure;
    /*** 血氧饱和度*/
    private Long bloodOxygen;
    /*** 压力等级*/
    private Long stressLevel;
    /*** 代谢年龄*/
    private Long metabolicAge;
    /*** 身体年龄指数*/
    private Long mai;
    /*** 体温*/
    private Long bodyTemperature;
    /*** 血糖*/
    private Long bloodSugar;
    /*** 手环唯一标识符*/
    private String uuid;

}
