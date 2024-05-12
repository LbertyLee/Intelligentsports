package org.dromara.teachers.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.teachers.domain.bo.TeacherTrainingTeamBo;
import org.dromara.teachers.domain.entity.TeacherTrainingTeam;
import org.dromara.teachers.domain.vo.TeacherTrainingTeamVo;

/**
 * 训练队(TeacherTrainingTeam)表数据库访问层
 *
 * @author LbertyLee
 * @since 2024-05-11 23:55:55
 */
public interface TeacherTrainingTeamMapper extends BaseMapperPlus<TeacherTrainingTeam, TeacherTrainingTeamVo> {


    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<TeacherTrainingTeamVo> selectPageTeacherTrainingTeamList(@Param("page") Page<TeacherTrainingTeam> page,
                                                                  @Param(Constants.WRAPPER) Wrapper<TeacherTrainingTeam> teacherTrainingTeamWrapper);
}

