package org.dromara.teachers.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 全部明细报告
 */
@Data
@Accessors(chain = true)
public class FullDetailsVo {

    /**训练队名称*/
    @ExcelProperty(value = "训练队名称")
    private String trainingName;

    /**训练类型*/
    @ExcelProperty(value = "训练类型")
    private String trainingType;

    /**授课教师*/
    @ExcelProperty(value = "授课教师")
    private String teacherName;

    /**训练日期*/
    @ExcelProperty(value = "训练日期")
    private Date trainingDate;

    /**人数*/
    @ExcelProperty(value = "人数")
    private Integer  personNum;

   private List<FullDetailsInfoVo> fullDetailsReportVoList;

}
