package org.dromara.teachers.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;


import java.util.List;

@Data
@Accessors(chain = true)
public class LineDataVo {

    /**当日血氧数据*/
    private List<LineBloodOxygenToDayVo> BloodOxygenDataToDay;

    /**当日心率数据*/
    private List<LineHeartRateToDayVo> HeartRateDataToDay;

    /**实时心率数据*/
    private List<LineHeartRateRealTimeVo> HeartRateDataToRealTime;

    /**实时血氧数据*/
    private List<LineBloodOxygenRealTimeVo> BloodOxygenDataToRealTime;

}
