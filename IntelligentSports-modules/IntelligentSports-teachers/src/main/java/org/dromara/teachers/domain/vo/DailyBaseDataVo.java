package org.dromara.teachers.domain.vo;

import lombok.Data;
import org.dromara.common.mybatis.core.domain.BaseEntity;

@Data
public class DailyBaseDataVo{

    /**训练队ID*/
    private Long trainingTeamId;

    /**训练队名称*/
    private String trainingTeamName;

    /***训练队人数*/
    private Integer trainingPeopleNumber;

    /**手环在线人数*/
    private Integer braceletOnlineNumber;

    /**平均心率*/
    private Integer averageHeartRate;
    /**平均血氧*/
    private Integer averageBloodOxygen;

    /**最高心率*/
    private Integer maxHeartRate;
    /**最高血氧*/
    private Integer maxBloodOxygen;

    /**最低心率*/
    private Integer minHeartRate;
    /**最低血氧*/
    private Integer minBloodOxygen;

}
