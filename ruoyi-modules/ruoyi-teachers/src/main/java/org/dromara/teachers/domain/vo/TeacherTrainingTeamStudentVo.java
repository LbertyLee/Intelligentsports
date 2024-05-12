package org.dromara.teachers.domain.vo;

import lombok.Data;

@Data
public class TeacherTrainingTeamStudentVo {
    //唯一标识
    private Long id;
    //学生ID
    private Long studentId;
    //训练队ID
    private Long trainingTeamId;
}
