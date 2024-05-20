package org.dromara.teachers.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
public class StudentTrainingTaskInfoVo {

    private Long id;
    /**训练任务ID*/
    private Long taskId;
    /**手环ID*/
    private String braceletId;

    /**实时血氧*/
    private Integer realTimeBloodOxygen;
    /**实时心率 */
    private Integer realTimeHeartRate;
    /**实时配速*/
    private Integer realTimePace;

    /**平均血氧*/
    private Integer averageBloodOxygen;
    /**平均心率*/
    private Integer averageHeartRate;
    /**平均配速*/
    private Integer averagePace;

    /**最高血氧*/
    private Integer maxBloodOxygen;
    /**最高心率*/
    private Integer maxHeartRate;
    /**最高配速*/
    private Integer maxPace;

    /**单次步数*/
    private Integer realTimeStepNumber;
    /**单次燃烧卡路里*/
    private Integer burningCalories;

    private List<StudentTrainingTaskInfoVo> list;


}
