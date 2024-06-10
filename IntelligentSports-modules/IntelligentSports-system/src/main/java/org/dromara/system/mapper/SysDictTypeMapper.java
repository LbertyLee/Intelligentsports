package org.dromara.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.system.domain.SysDictType;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.vo.SysDictTypeVo;

/**
 * 字典表 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysDictTypeMapper extends BaseMapperPlus<SysDictType, SysDictTypeVo> {

}
