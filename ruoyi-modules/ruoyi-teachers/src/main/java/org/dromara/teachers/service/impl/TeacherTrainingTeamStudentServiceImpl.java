package org.dromara.teachers.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.teachers.domain.entity.TeacherTrainingTeamStudent;
import org.dromara.teachers.mapper.TeacherTrainingTeamStudentMapper;
import org.dromara.teachers.service.TeacherTrainingTeamStudentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 训练队学生表(TeacherTrainingTeamStudent)表服务实现类
 *
 * @author LbertyLee
 * @since 2024-05-12 14:51:05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherTrainingTeamStudentServiceImpl  implements TeacherTrainingTeamStudentService {


    private final TeacherTrainingTeamStudentMapper teacherTrainingTeamStudentMapper;

    /**
     * 批量插入教师培训团队学生信息。
     *
     * @param teacherTrainingTeamStudentList 要插入的教师培训团队学生列表，不应为空。
     * @return 返回插入成功的记录数。
     */
    @Override
    public boolean insertBatch(List<TeacherTrainingTeamStudent> teacherTrainingTeamStudentList) {
        // 调用mapper层方法，执行批量插入操作
        return teacherTrainingTeamStudentMapper.insertBatch(teacherTrainingTeamStudentList);
    }

    /**
     * 检查学生是否绑定到了教师培训团队
     *
     * @param id 学生的唯一标识符
     * @return boolean 返回值为true表示学生未绑定到任何教师培训团队，false则表示已绑定
     */
    @Override
    public boolean checkIsBindStudent(Long id) {
        // 通过学生ID查询绑定到教师培训团队的学生信息
        List<TeacherTrainingTeamStudent> teacherTrainingTeamStudents = teacherTrainingTeamStudentMapper.selectList(
            new LambdaQueryWrapper<TeacherTrainingTeamStudent>().eq(TeacherTrainingTeamStudent::getTrainingTeamId, id)
        );
        // 判断查询结果是否为空，若为空则表示学生未绑定，返回true；否则返回false
        return ObjectUtil.isEmpty(teacherTrainingTeamStudents);
    }
}

