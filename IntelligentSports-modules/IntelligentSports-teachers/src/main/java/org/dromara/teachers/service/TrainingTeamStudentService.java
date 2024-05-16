package org.dromara.teachers.service;

import org.dromara.teachers.domain.bo.TrainingTeamStudentBo;
import org.dromara.teachers.domain.entity.TrainingTeamStudent;
import org.dromara.teachers.domain.vo.TrainingTeamStudentVo;

import java.util.List;

/**
 * 训练队学生表(TeacherTrainingTeamStudent)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-12 14:51:05
 */
public interface TrainingTeamStudentService {

    boolean insertBatch(List<TrainingTeamStudent> teacherTrainingTeamStudentList);

    boolean checkIsBindStudent(Long id);

    List<TrainingTeamStudentVo> selectList(TrainingTeamStudentBo trainingTeamStudentBo);

}

