package org.dromara.teachers.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.domain.bo.DailyBaseDataBo;
import org.dromara.teachers.domain.vo.DailyBaseDataVo;
import org.dromara.teachers.service.DailyDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/teacher/dailyData")
@RequiredArgsConstructor
public class DailyDataController extends BaseController {

    private final DailyDataService dailyDataService;

    /**
     * 获取日常基础数据
     *
     */
    @GetMapping
    public R<DailyBaseDataVo> getDailyBaseData(DailyBaseDataBo dailyDataBo) {
        if(log.isInfoEnabled()){
            log.info("DailyDataController.getDailyBaseData.dailyDataBo:{}", dailyDataBo);
        }
        return R.ok(dailyDataService.getDailyBaseData(dailyDataBo));
    }

}
