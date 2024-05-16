package org.dromara.teachers.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户对象导出VO
 *
 * @author Lion Li
 */

@Data
@NoArgsConstructor
@AutoMapper(target = StudentInfoVo.class, convertGenerate = false)
public class StudentInfoExportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /*** 唯一标识符。用于标识对象的唯一ID。*/
    @ExcelProperty(value = "学生序号")
    private Long id;

    /*** 名称。代表对象的名称属性，通常用于显示或识别对象。*/
    @ExcelProperty(value = "学生姓名")
    private String name;

    /*** 手环id*/
    @ExcelProperty(value = "学生手环id")
    private String uuid;

    /*** 学号*/
    @ExcelProperty(value = "学生学号")
    private String studentNumber;


}
