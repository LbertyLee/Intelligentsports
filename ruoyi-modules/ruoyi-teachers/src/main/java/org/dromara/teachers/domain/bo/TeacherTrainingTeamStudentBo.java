package org.dromara.teachers.domain.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.ArrayList;

/**
 * 训练队学生表(TeacherTrainingTeamStudent)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-12 14:51:05
 */
@Data
public class TeacherTrainingTeamStudentBo {
    //唯一标识
    private Long id;
    //学生ID

    private ArrayList<Long> studentId;
    //训练队ID
    private Long trainingTeamId;

}

