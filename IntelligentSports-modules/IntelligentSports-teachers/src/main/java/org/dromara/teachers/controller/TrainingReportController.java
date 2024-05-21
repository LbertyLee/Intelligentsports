package org.dromara.teachers.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.teachers.domain.vo.FullDetailsVo;
import org.dromara.teachers.service.TrainingReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/training/report")
@RequiredArgsConstructor
public class TrainingReportController {

    private final TrainingReportService trainingReportService;


    /**
     * 查看训练报告-全部明细
     */
    @GetMapping("/fullDetails/{taskId}")
    public R<List<FullDetailsVo>> getFullDetailsVo(@PathVariable Long taskId) {
        if(log.isInfoEnabled()){
            log.info("TrainingReportController.getFullDetailsVo.taskId={}", taskId);
        }
        return R.ok(trainingReportService.getFullDetails(taskId));
    }

    /**
     *查看不同的明细报告
     **/

    /**
     * 导出报告
     */
}
