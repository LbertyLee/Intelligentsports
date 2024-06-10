package org.dromara.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysSocial;
import org.dromara.system.domain.vo.SysSocialVo;

/**
 * 社会化关系Mapper接口
 *
 * @author thiszhc
 */
@Mapper
public interface SysSocialMapper extends BaseMapperPlus<SysSocial, SysSocialVo> {

}
