package org.dromara.teachers.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LineHeartRateRealTimeVo {

    /*** 最高心率*/
    private Double maxHeartRate;

    /*** 平均心率*/
    private Double avgHeartRate;

        /*** 最低心率*/
    private Double minHeartRate;

    /*** 统计时间*/
    private String statisticalTime;
}
