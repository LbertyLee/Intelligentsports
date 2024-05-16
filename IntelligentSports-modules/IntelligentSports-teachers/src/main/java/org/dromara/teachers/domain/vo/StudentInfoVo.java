package org.dromara.teachers.domain.vo;

import lombok.Data;
import org.dromara.common.tenant.core.TenantEntity;

@Data
public class StudentInfoVo extends TenantEntity {

        /*** 唯一标识符。用于标识对象的唯一ID。*/
        private Long id;

        /*** 名称。代表对象的名称属性，通常用于显示或识别对象。*/
        private String name;

        /*** 手环id*/
        private String uuid;

        /*** 学号*/
        private String studentNumber;

}
