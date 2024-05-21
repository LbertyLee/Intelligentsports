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

}
