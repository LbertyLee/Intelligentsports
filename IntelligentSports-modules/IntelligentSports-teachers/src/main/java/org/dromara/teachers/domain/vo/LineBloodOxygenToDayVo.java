package org.dromara.teachers.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LineBloodOxygenToDayVo {

    /***最高血氧*/
    private Double maxBloodOxygen;

    /***平均血氧*/
    private Double avgBloodOxygen;

    /***最低血氧*/
    private Double minBloodOxygen;

    /*** 统计时间（每小时统计一次）*/
    private String statisticalTime;
}
