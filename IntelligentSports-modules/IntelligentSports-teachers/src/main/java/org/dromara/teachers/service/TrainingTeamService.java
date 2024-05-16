package org.dromara.teachers.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.TrainingTeamBo;
import org.dromara.teachers.domain.bo.TrainingTeamStudentBo;
import org.dromara.teachers.domain.vo.TrainingTeamVo;

import java.util.List;

/**
 * 训练队(TeacherTrainingTeam)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-11 23:55:57
 */
public interface TrainingTeamService {

    /**
     * 添加训练队信息。
     *
     * @param teacherTrainingTeamBo 培训团队信息对象，包含团队的详细信息。
     * @return 添加成功返回1，失败返回0。
     */
    int addTrainingTeam(TrainingTeamBo teacherTrainingTeamBo);

    int updateTrainingTeam(TrainingTeamBo teacherTrainingTeamBo);

    int deleteTrainingTeam(Long id);

    TableDataInfo<TrainingTeamVo> selectPageTeacherTrainingTeamList(TrainingTeamBo teacherTrainingTeamBo, PageQuery pageQuery);

    boolean bindStudent(TrainingTeamStudentBo teacherTrainingTeamBindStudentBo);

    TrainingTeamVo selectTeacherTrainingTeamById(Long id);

    List<TrainingTeamVo> list(TrainingTeamBo teacherTrainingTeamBo);

}

