package org.dromara.teachers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.teachers.domain.entity.BraceletStatus;
import org.dromara.teachers.domain.vo.BraceletStatusVo;

import java.util.List;

/**
 * 手环状态(TeacherBraceletStatus)表数据库访问层
 *
 * @author LbertyLee
 * @since 2024-05-18 14:49:34
 */
public interface BraceletStatusMapper extends BaseMapperPlus<BraceletStatus, BraceletStatusVo> {


}

