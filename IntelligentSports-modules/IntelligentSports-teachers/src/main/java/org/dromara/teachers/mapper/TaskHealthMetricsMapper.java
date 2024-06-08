package org.dromara.teachers.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.teachers.domain.entity.TaskHealthMetrics;
import org.dromara.teachers.domain.vo.TaskHealthMetricsVo;


import java.util.List;

/**
 * 训练健康指数(TeacherTaskHealthMetrics)表数据库访问层
 *
 * @author LbertyLee
 * @since 2024-05-18 15:21:42
 */
@Mapper
public interface TaskHealthMetricsMapper extends BaseMapperPlus<TaskHealthMetrics, TaskHealthMetricsVo> {


    List<TaskHealthMetricsVo> selectTaskHealthMetricsVoList(@Param("taskId") Long taskId,@Param("braceletId") String braceletId);
}

