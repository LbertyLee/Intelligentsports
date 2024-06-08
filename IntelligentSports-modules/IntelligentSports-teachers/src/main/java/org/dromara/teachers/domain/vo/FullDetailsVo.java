package org.dromara.teachers.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 全部明细报告
 */
@Data
public class FullDetailsVo {

    /**学生姓名*/
    @ExcelProperty(value = "学生姓名")
    private String  studentName;
    /** 平均心率*/
    @ExcelProperty(value = "平均心率")
    private double averageHeartRate;
    /**平均配速*/
    @ExcelProperty(value = "平均配速")
    private double averagePace;
    /**平均血氧*/
    @ExcelProperty(value = "平均血氧")
    private double averageBloodOxygen;

    /**最高心率*/
    @ExcelProperty(value = "最高心率")
    private double maxHeartRate;
    /**最高配速*/
    @ExcelProperty(value = "最高配速")
    private double maxPace;
    /**最高血氧*/
    @ExcelProperty(value = "最高血氧")
    private double maxBloodOxygen;

    /**最低心率*/
    @ExcelProperty(value = "最低心率")
    private double minHeartRate;

}
