package org.dromara.teachers.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;


import java.util.List;

@Data
@Accessors(chain = true)
public class LineDataVo {

    /**当日血氧数据*/
    private List<LineBloodOxygenVo> BloodOxygenDataToDay;

    /**当日心率数据*/
    private List<LineHeartRateVo> HeartRateDataToDay;

}
