package org.dromara.teachers.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;
import org.dromara.teachers.domain.vo.TrainingTeamStudentVo;

/**
 * 训练队学生表(TeacherTrainingTeamStudent)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-12 14:51:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("teacher_training_team_student")
@AutoMapper(target = TrainingTeamStudentVo.class)
public class TrainingTeamStudent extends TenantEntity {
    //唯一标识
    private Long id;
    //学生ID
    private Long studentId;
    //训练队ID
    private Long trainingTeamId;

}

