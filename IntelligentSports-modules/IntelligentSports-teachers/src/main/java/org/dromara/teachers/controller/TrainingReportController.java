package org.dromara.teachers.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.vo.SysUserExportVo;
import org.dromara.teachers.domain.bo.TrainingTaskBo;
import org.dromara.teachers.domain.vo.FullDetailsVo;
import org.dromara.teachers.domain.vo.TrainingTaskVo;
import org.dromara.teachers.service.TrainingReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teacher/training/report")
@RequiredArgsConstructor
public class TrainingReportController extends BaseController {

    private final TrainingReportService trainingReportService;


    /**
     * 查看训练队列表
     * @param trainingTaskBo
     * @param pageQuery
     * @return
     */
    @GetMapping("/page")
    public TableDataInfo<TrainingTaskVo> selectPageTrainingTask(TrainingTaskBo trainingTaskBo,
                                                                PageQuery pageQuery) {
        if (log.isInfoEnabled()) {
            log.info("TrainingReportController.selectPageTrainingTask.trainingTaskBo:{}", trainingTaskBo);
        }
        return trainingReportService.selectPageTrainingReport(trainingTaskBo, pageQuery);
    }


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
     * 导出全部明细报告
     */
    @PostMapping("/exportFullDetails/{taskId}")
    public void exportFullDetails( HttpServletResponse response, @PathVariable Long taskId){
        List<FullDetailsVo> fullDetails = trainingReportService.getFullDetails(taskId);
        ExcelUtil.exportExcel(fullDetails, "全部明细报告", FullDetailsVo.class, response);
    }


    /**
     *查看不同的明细报告
     **/

    /**
     * 导出报告
     */
}
