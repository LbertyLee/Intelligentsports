package org.dromara.teachers.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;
import org.dromara.teachers.domain.vo.StudentTrainingTaskInfoVo;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
@AutoMapper(target = StudentTrainingTaskInfoVo.class)
@TableName("teacher_student_training_task_info")
public class StudentTrainingTaskInfo extends TenantEntity {

    @TableId
    private Long Id;

    /**训练任务ID*/
    private Long taskId;
    /**手环ID*/
    private String braceletId;

    /**实时血氧*/
    private Integer realTimeBloodOxygen;
    /**实时心率 */
    private Integer realTimeHeartRate;
    /**实时配速*/
    private Integer realTimePace;

    /**平均血氧*/
    private Integer averageBloodOxygen;
    /**平均心率*/
    private Integer averageHeartRate;
    /**平均配速*/
    private Integer averagePace;

    /**最高血氧*/
    private Integer maxBloodOxygen;
    /**最高心率*/
    private Integer maxHeartRate;
    /**最高配速*/
    private Integer maxPace;


}
