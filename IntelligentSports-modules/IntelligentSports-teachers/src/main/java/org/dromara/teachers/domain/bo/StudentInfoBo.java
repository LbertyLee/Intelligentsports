package org.dromara.teachers.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentInfoBo extends TenantEntity {

    /*** 唯一标识符。用于标识对象的唯一ID。*/
    private Long id;

    /*** 名称。代表对象的名称属性，通常用于显示或识别对象。*/
    private String name;

    /*** 学生学号*/
    private String studentNumber;

    /*** 手环id*/
    private String uuid;
}
