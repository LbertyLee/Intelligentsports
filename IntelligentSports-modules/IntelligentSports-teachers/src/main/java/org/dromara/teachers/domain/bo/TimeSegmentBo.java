package org.dromara.teachers.domain.bo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class TimeSegmentBo {


    //当前时间
    private long currentTime;
    //一小时前的时间
    private long oneHourAgo;
    //半小时前的时间
    private long halfHourAgo;

    public TimeSegmentBo() {
        LocalDateTime now = LocalDateTime.now();
        this.currentTime = now.atZone(ZoneId.systemDefault()).toEpochSecond();
        this.oneHourAgo = now.minusHours(1).atZone(ZoneId.systemDefault()).toEpochSecond();
        this.halfHourAgo = now.minusMinutes(30).atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
