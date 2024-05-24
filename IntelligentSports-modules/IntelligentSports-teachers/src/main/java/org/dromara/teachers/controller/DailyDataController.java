package org.dromara.teachers.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.domain.bo.DailyBaseDataBo;
import org.dromara.teachers.domain.vo.DailyBaseDataVo;
import org.dromara.teachers.domain.vo.LineDataVo;
import org.dromara.teachers.service.DailyDataService;
import org.springframework.web.bind.annotation.*;

/**
 * 日常训练数据Controller
 * @author LbertyLee
 * @date 2024/5/21 16:51
 */
@Slf4j
@RestController
@RequestMapping("/teacher/dailyData")
@RequiredArgsConstructor
public class DailyDataController extends BaseController {

    private final DailyDataService dailyDataService;

    /**
     * 处理每日基础数据信息，并返回处理结果。
     *
     * @param dailyDataBo 包含每日基础数据信息的请求体对象。
     * @return 返回一个结果对象，其中包含处理成功或失败的信息以及每日基础数据的视图对象。
     */
    @PostMapping("/base")
    public R<DailyBaseDataVo> getDailyBaseData(@RequestBody DailyBaseDataBo dailyDataBo) {
        if(log.isInfoEnabled()){
            log.info("DailyDataController.getDailyBaseData.dailyDataBo:{}", dailyDataBo);
        }
        return R.ok(dailyDataService.getDailyBaseData(dailyDataBo));
    }

    /**
     * 获取折线图数据
     */
    @PostMapping("/line")
    public R<LineDataVo> getLineData(@RequestBody DailyBaseDataBo dailyDataBo) {
        return R.ok(dailyDataService.getLineData(dailyDataBo));
    }

}
