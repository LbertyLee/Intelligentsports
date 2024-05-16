package org.dromara.teachers.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingTeamStudentVo {
    //唯一标识
    private Long id;
    //学生ID
    private Long studentId;
    //训练队ID
    private Long trainingTeamId;
}
