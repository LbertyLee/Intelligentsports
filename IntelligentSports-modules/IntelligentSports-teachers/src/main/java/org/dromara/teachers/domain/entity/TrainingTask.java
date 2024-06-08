package org.dromara.teachers.domain.entity;


import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serializable;

/**
 * 训练任务表(TeacherTrainingTask)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-15 11:18:55
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("teacher_training_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class TrainingTask extends TenantEntity {
    //唯一标识
    private Long id;

    //训练队ID
    private Long trainingTeamId;

    //训练队名称
    private String trainingTeamName;

    //训练类型名称
    private String exerciseTypeName;

    //组数
    private Integer number;

    //教师姓名
    private String teacherName;

    //训练时长（单位秒）
    private Long trainingTime;

    /**人数*/
    @ExcelProperty(value = "人数")
    private Integer  personNum;

}

