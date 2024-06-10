package org.dromara.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysRoleDept;

/**
 * 角色与部门关联表 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysRoleDeptMapper extends BaseMapperPlus<SysRoleDept, SysRoleDept> {

}
