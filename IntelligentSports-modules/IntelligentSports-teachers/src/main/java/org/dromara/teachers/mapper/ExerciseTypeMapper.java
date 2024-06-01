package org.dromara.teachers.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.teachers.domain.entity.ExerciseType;
import org.dromara.teachers.domain.vo.ExerciseTypeVo;

/**
 * 运动类型表(TeacherExerciseType)表数据库访问层
 *
 * @author LbertyLee
 * @since 2024-05-14 14:27:41
 */
public interface ExerciseTypeMapper extends BaseMapperPlus<ExerciseType, ExerciseTypeVo> {

    @DataPermission({
//        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<ExerciseTypeVo> selectExerciseTypeVoPage(@Param("page") Page<ExerciseType> page,
                                                  @Param(Constants.WRAPPER) Wrapper<ExerciseType> queryWrapper);
}

