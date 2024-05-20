package org.dromara.teachers.domain.vo;


import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;
import org.dromara.teachers.domain.bo.TaskHealthMetricsBo;

/**
 * 训练健康指数(TeacherTaskHealthMetrics)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-18 15:21:43
 */
@Data
@Accessors(chain = true)
@AutoMapper(target = TaskHealthMetricsBo.class)
public class TaskHealthMetricsVo{

    //唯一标识
    private Long id;

    //学生ID
    private Long studentId;

    //学生姓名
    private String studentName;

    //手环ID
    private String braceletId;

    //训练任务ID
    private Long taskId;

    //记录数据的时间戳
    private Long timestamp;

    //总步数
    private Integer totalSteps;

    //总距离
    private Integer totalDistance;

    /**实时配速*/
    private Integer matchingSpeed;

    //总热量消耗
    private Integer totalCalories;

    //心率
    private Integer heartRate;

    //血压
    private Integer bloodPressure;

    //血氧饱和度
    private Integer bloodOxygen;

}

