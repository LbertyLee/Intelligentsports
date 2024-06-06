package org.dromara.teachers.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import org.dromara.teachers.domain.entity.StudentTrainingTaskInfo;
import org.dromara.teachers.domain.vo.StudentTrainingTaskInfoVo;

@Mapper
public interface StudentTrainingTaskInfoMapper extends BaseMapperPlus<StudentTrainingTaskInfo, StudentTrainingTaskInfoVo> {
}
