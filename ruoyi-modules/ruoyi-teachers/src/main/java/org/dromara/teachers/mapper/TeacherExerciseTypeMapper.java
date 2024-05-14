package org.dromara.teachers.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.teachers.domain.entity.TeacherExerciseType;
import org.dromara.teachers.domain.vo.TeacherExerciseTypeVo;

/**
 * 运动类型表(TeacherExerciseType)表数据库访问层
 *
 * @author LbertyLee
 * @since 2024-05-14 14:27:41
 */
public interface TeacherExerciseTypeMapper extends BaseMapperPlus<TeacherExerciseType, TeacherExerciseType> {

    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<TeacherExerciseTypeVo> selectExerciseTypeVoPage(@Param("page") Page<TeacherExerciseType> page,
                                                         @Param(Constants.WRAPPER) Wrapper<TeacherExerciseType> queryWrapper);
}

