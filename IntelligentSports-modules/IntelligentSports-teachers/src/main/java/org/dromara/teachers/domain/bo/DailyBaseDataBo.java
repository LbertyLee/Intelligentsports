package org.dromara.teachers.domain.bo;

import lombok.Data;
import org.dromara.common.mybatis.core.domain.BaseEntity;

@Data
public class DailyBaseDataBo extends BaseEntity {

    /**训练队ID*/
    private Long trainingTeamId;

}
