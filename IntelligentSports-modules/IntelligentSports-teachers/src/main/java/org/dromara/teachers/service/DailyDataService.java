package org.dromara.teachers.service;

import org.dromara.teachers.domain.bo.DailyBaseDataBo;
import org.dromara.teachers.domain.vo.DailyBaseDataVo;
import org.dromara.teachers.domain.vo.LineDataVo;

public interface DailyDataService {

    DailyBaseDataVo getDailyBaseData(DailyBaseDataBo dailyDataBo);

    LineDataVo getLineData(DailyBaseDataBo dailyDataBo);

}
