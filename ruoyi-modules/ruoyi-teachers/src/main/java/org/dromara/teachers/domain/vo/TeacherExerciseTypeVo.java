package org.dromara.teachers.domain.vo;


import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;
import org.dromara.system.domain.vo.SysTenantVo;

/**
 * 运动类型表(TeacherExerciseType)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-14 14:27:41
 */
@Data
//@AutoMapper(target = TeacherExerciseTypeVo.class)
public class TeacherExerciseTypeVo{
    //唯一标识
    private Long id;
    //运动名称
    private String exerciseName;
    //组数
    private Long number;
    //备注
    private String remarks;


}

