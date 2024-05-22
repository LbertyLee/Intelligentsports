package org.dromara.teachers.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serializable;

/**
 * 训练健康指数(TeacherTaskHealthMetrics)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-18 15:21:43
 */
@Data
@AllArgsConstructor
@TableName("teacher_task_health_metrics")
@EqualsAndHashCode(callSuper=false)
public class TaskHealthMetrics extends TenantEntity {
    //唯一标识
    private Long id;
    //手环ID
    private String braceletId;
    //训练任务ID
    private Long taskId;
    //记录数据的时间戳
    private Long timestamp;
    //总步数
    private Integer totalSteps;

    //实时配速
    private Integer matchingSpeed;

    //总距离
    private Integer totalDistance;
    //总热量消耗
    private Integer totalCalories;
    //心率
    private Integer heartRate;
    //血压
    private Integer bloodPressure;
    //血氧饱和度
    private Integer bloodOxygen;

}

