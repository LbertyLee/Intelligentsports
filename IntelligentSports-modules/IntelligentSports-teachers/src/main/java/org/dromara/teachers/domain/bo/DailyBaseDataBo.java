package org.dromara.teachers.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

@Data
@EqualsAndHashCode(callSuper=false)
public class DailyBaseDataBo extends BaseEntity {

    /**训练队ID*/
    private Long trainingTeamId;

    /**开始时间*/
    private Long startTime;

    /**结束时间*/
    private Long endTime;


}
