package org.dromara.teachers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.TeacherTrainingTeamBo;
import org.dromara.teachers.domain.bo.TeacherTrainingTeamStudentBo;
import org.dromara.teachers.domain.entity.TeacherTrainingTeam;
import org.dromara.teachers.domain.vo.TeacherTrainingTeamVo;

/**
 * 训练队(TeacherTrainingTeam)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-11 23:55:57
 */
public interface TeacherTrainingTeamService {

    /**
     * 添加训练队信息。
     *
     * @param teacherTrainingTeamBo 培训团队信息对象，包含团队的详细信息。
     * @return 添加成功返回1，失败返回0。
     */
    int addTrainingTeam(TeacherTrainingTeamBo teacherTrainingTeamBo);

    int updateTrainingTeam(TeacherTrainingTeamBo teacherTrainingTeamBo);

    int deleteTrainingTeam(Long id);

    TableDataInfo<TeacherTrainingTeamVo> selectPageTeacherTrainingTeamList(TeacherTrainingTeamBo teacherTrainingTeamBo, PageQuery pageQuery);

    boolean bindStudent(TeacherTrainingTeamStudentBo teacherTrainingTeamBindStudentBo);
}

