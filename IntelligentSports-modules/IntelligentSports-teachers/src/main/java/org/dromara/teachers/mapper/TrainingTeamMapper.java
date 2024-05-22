package org.dromara.teachers.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.teachers.domain.entity.TrainingTeam;
import org.dromara.teachers.domain.vo.TrainingTeamVo;

/**
 * 训练队(TeacherTrainingTeam)表数据库访问层
 *
 * @author LbertyLee
 * @since 2024-05-11 23:55:55
 */
@Mapper
public interface TrainingTeamMapper extends BaseMapperPlus<TrainingTeam, TrainingTeamVo> {


    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<TrainingTeamVo> selectPageTeacherTrainingTeamList(@Param("page") Page<TrainingTeam> page,
                                                           @Param(Constants.WRAPPER) Wrapper<TrainingTeam> teacherTrainingTeamWrapper);
}

