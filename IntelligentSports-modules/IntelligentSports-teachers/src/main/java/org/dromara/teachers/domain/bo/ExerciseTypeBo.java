package org.dromara.teachers.domain.bo;


import lombok.Data;

/**
 * 运动类型表(TeacherExerciseType)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-14 14:27:41
 */
@Data
@SuppressWarnings("serial")
public class ExerciseTypeBo {
    //唯一标识
    private Long id;
    //运动名称
    private String exerciseName;
    //组数
    private Long number;
    //备注
    private String remarks;


}

