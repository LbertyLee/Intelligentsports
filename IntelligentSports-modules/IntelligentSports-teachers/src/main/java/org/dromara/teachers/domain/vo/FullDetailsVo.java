package org.dromara.teachers.domain.vo;

import lombok.Data;

@Data
public class FullDetailsVo {

    /**学生姓名*/
    private String  studentName;
    /** 平均心率*/
    private double averageHeartRate;
    /**平均配速*/
    private double averagePace;
    /**平均血氧*/
    private double averageBloodOxygen;

    /**最高心率*/
    private double maxHeartRate;
    /**最高配速*/
    private double maxPace;
    /**最高血氧*/
    private double maxBloodOxygen;

}
