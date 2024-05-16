package org.dromara.teachers.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.teachers.domain.entity.TrainingTask;
import org.dromara.teachers.domain.vo.TrainingTaskVo;

/**
 * 训练任务表(TeacherTrainingTask)表数据库访问层
 *
 * @author LbertyLee
 * @since 2024-05-15 11:18:55
 */
@Mapper
public interface TrainingTaskMapper extends BaseMapperPlus<TrainingTask, TrainingTaskVo> {


    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<TrainingTaskVo> selectPageTrainingTaskList(@Param("page") Page<Object> page,
                                                    @Param(Constants.WRAPPER) Wrapper<TrainingTask> trainingTaskWrapper);

}

