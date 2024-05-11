package org.dromara.teachers.domain.vo;

import lombok.Data;
import org.dromara.common.tenant.core.TenantEntity;

@Data
public class StudentInfoVo extends TenantEntity {

        /**
         * 唯一标识符。用于标识对象的唯一ID。
         */
        private Long id;

        /**
         * 名称。代表对象的名称属性，通常用于显示或识别对象。
         */
        private String name;

        /**
         * 全局唯一标识符。用于在全球范围内唯一标识一个对象。
         */
        private String uuid;

}
