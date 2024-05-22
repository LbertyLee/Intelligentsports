package org.dromara.teachers.domain.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

/**
 * 运动类型表(TeacherExerciseType)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-14 14:27:41
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ExerciseTypeVo extends TenantEntity {
    //唯一标识
    private Long id;
    //运动名称
    private String exerciseName;
    //组数
    private Long number;
    //备注
    private String remarks;


}

