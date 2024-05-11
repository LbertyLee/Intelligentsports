package org.dromara.teachers.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class StudentInfoImportVo implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;
        /*** 唯一标识符。用于标识对象的唯一ID。*/
        @ExcelProperty(value = "学生序号")
        private Long id;

        /**学号*/
        @ExcelProperty(value = "学号")
        private String studentNumber;

        /*** 名称。代表对象的名称属性，通常用于显示或识别对象。*/
        @ExcelProperty(value = "学生姓名")
        private String name;

        /*** 全局唯一标识符。用于在全球范围内唯一标识一个对象。*/
        @ExcelProperty(value = "手环ID")
        private String uuid;



}
