package org.dromara.teachers.service;

import org.dromara.teachers.domain.bo.DailyBaseDataBo;
import org.dromara.teachers.domain.vo.DailyBaseDataVo;

public interface DailyDataService {

    DailyBaseDataVo getDailyBaseData(DailyBaseDataBo dailyDataBo);

}
