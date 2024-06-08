package org.dromara.teachers.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;

/**
 * 心率明细报告
 */
@Data
public class HeartRateDetailsVo {

    /**学生ID*/
    @ExcelProperty(value = "学生ID")
    private Long studentId;
    /**学生姓名*/
    @ExcelProperty(value = "学生姓名")
    private String studentName;
    /**心率明细报告*/
    @ExcelProperty(value = "心率明细报告")
    private List<HeartRateDetails> heartRateDetails;
}

