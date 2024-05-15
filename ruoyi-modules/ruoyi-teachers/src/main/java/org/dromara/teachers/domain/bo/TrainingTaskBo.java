package org.dromara.teachers.domain.bo;


import lombok.Data;

/**
 * 训练任务表(TeacherTrainingTask)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-15 11:18:55
 */
@SuppressWarnings("serial")
@Data
public class TrainingTaskBo  {

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

}

