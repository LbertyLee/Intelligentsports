package org.dromara.teachers.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HeartRateDetails {

    /**组数*/
    @ExcelProperty(value = "组数")
    private Integer number;
    /**平均心率*/
    @ExcelProperty(value = "平均心率")
    private double averageHeartRate;
    /**最高心率*/
    @ExcelProperty(value = "最高心率")
    private double maxHeartRate;
    /**最低心率*/
    @ExcelProperty(value = "最低心率")
    private double minHeartRate;
}
