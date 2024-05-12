package org.dromara.teachers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.teachers.domain.entity.TeacherTrainingTeamStudent;

import java.util.List;

/**
 * 训练队学生表(TeacherTrainingTeamStudent)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-12 14:51:05
 */
public interface TeacherTrainingTeamStudentService {

    boolean insertBatch(List<TeacherTrainingTeamStudent> teacherTrainingTeamStudentList);

    boolean checkIsBindStudent(Long id);

}

