package org.dromara.teachers.domain.vo;



import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.List;


/**
 * 训练任务表(TeacherTrainingTask)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-15 11:18:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TrainingTaskVo extends TenantEntity {

    //唯一标识
    private Long id;

    //训练任务名称
    private String taskName;

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

    //训练任务中所有学生id
    private List<Long> students;

    private List<TaskHealthMetricsVo> studentInfoList;

    /**人数*/
    @ExcelProperty(value = "人数")
    private Integer  personNum;


}

