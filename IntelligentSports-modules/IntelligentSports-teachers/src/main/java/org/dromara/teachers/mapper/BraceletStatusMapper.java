package org.dromara.teachers.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.teachers.domain.entity.BraceletStatus;
import org.dromara.teachers.domain.vo.BraceletStatusVo;

@Mapper
public interface BraceletStatusMapper extends BaseMapperPlus<BraceletStatus, BraceletStatusVo> {
}
