package org.dromara.teachers.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.teachers.domain.bo.TrainingTeamBo;
import org.dromara.teachers.domain.bo.TrainingTeamStudentBo;
import org.dromara.teachers.domain.entity.TrainingTeam;
import org.dromara.teachers.domain.entity.TrainingTeamStudent;
import org.dromara.teachers.domain.vo.TrainingTeamStudentVo;
import org.dromara.teachers.mapper.TrainingTeamStudentMapper;
import org.dromara.teachers.service.TrainingTeamStudentService;
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
public class TrainingTeamStudentServiceImpl implements TrainingTeamStudentService {


    private final TrainingTeamStudentMapper teacherTrainingTeamStudentMapper;

    /**
     * 批量插入教师培训团队学生信息。
     *
     * @param teacherTrainingTeamStudentList 要插入的教师培训团队学生列表，不应为空。
     * @return 返回插入成功的记录数。
     */
    @Override
    public boolean insertBatch(List<TrainingTeamStudent> teacherTrainingTeamStudentList) {
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
        List<TrainingTeamStudent> teacherTrainingTeamStudents = teacherTrainingTeamStudentMapper.selectList(
            new LambdaQueryWrapper<TrainingTeamStudent>().eq(TrainingTeamStudent::getTrainingTeamId, id)
        );
        // 判断查询结果是否为空，若为空则表示学生未绑定，返回true；否则返回false
        return ObjectUtil.isEmpty(teacherTrainingTeamStudents);
    }

    /**
     * 查询训练团队学生列表。
     *
     * @param trainingTeamStudentBo 搜索条件对象，用于构建查询条件。
     * @return 返回训练团队学生视图对象的列表。这些视图对象是由实际的训练团队学生对象转换而来的。
     */
    @Override
    public List<TrainingTeamStudentVo> selectList(TrainingTeamStudentBo trainingTeamStudentBo) {
        // 根据搜索条件构建查询Wrapper，并执行查询操作
        List<TrainingTeamStudent> trainingTeamStudents = teacherTrainingTeamStudentMapper.selectList(this.buildQueryWrapper(trainingTeamStudentBo));

        // 使用Mapstruct将查询结果转换为视图对象
        return MapstructUtils.convert(trainingTeamStudents, TrainingTeamStudentVo.class);
    }

    /**
     * 构建查询Wrapper用于条件查询。
     *
     * @param trainingTeamStudentBo 查询条件对象，包含训练团队ID和学生ID。
     * @return 返回构建好的查询Wrapper，用于Hibernate或MyBatis等框架的查询操作。
     */
    private Wrapper<TrainingTeamStudent> buildQueryWrapper(TrainingTeamStudentBo trainingTeamStudentBo) {
        // 创建一个空的查询Wrapper
        QueryWrapper<TrainingTeamStudent> wrapper = Wrappers.query();
        // 根据条件动态构建查询Wrapper，添加团队ID或学生ID的查询条件
        return wrapper
            .eq(ObjectUtil.isNotNull(trainingTeamStudentBo.getTrainingTeamId()), "training_team_id", trainingTeamStudentBo.getTrainingTeamId())
            .eq(ObjectUtil.isNotNull(trainingTeamStudentBo.getStudentId()), "student_id",trainingTeamStudentBo.getStudentId());
    }
}

