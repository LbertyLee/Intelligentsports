package org.dromara.teachers.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.domain.bo.TrainingTaskBo;
import org.dromara.teachers.domain.vo.TrainingTaskVo;
import org.dromara.teachers.service.TrainingTaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 训练任务表(TeacherTrainingTask)表控制层
 *
 * @author LbertyLee
 * @since 2024-05-15 11:18:55
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/teacher/trainingTask")
public class TrainingTaskController extends BaseController {

    private final TrainingTaskService trainingTaskService;


    /**
     * 根据训练任务id查询训练任务基础数据
     */
    @GetMapping("/taskBaseInfo/{taskId}")
    public R<TrainingTaskVo>  selectTrainingTaskById( @PathVariable Long taskId) {
        if(log.isInfoEnabled()){
            log.info("TrainingTaskController.selectTrainingTaskById.taskId:{}",taskId);
        }
        return R.ok(trainingTaskService.selectTaskBaseInfoByTaskId(taskId));
    }

    /**
     * 分页查询训练任务
     */
    @GetMapping("/page")
    public TableDataInfo<TrainingTaskVo>  selectPageTrainingTask(TrainingTaskBo trainingTaskBo,
                                                                 PageQuery pageQuery) {
        if(log.isInfoEnabled()){
            log.info("TrainingTaskController.selectPageTrainingTask.trainingTaskBo:{}",trainingTaskBo);
            log.info("TrainingTaskController.selectPageTrainingTask.pageQuery:{}",pageQuery);
        }
        return trainingTaskService.selectPageTrainingTask(trainingTaskBo, pageQuery);
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public R<TrainingTaskVo> selectOne(@PathVariable Long id) {
        if (log.isInfoEnabled()){
            log.info("TrainingTaskController.selectOne.id={}",id);
        }
        return R.ok(trainingTaskService.selectOne(id));
    }

    /**
     * 新增数据
     *
     * @param trainingTaskBo 实体对象
     * @return 新增结果
     */
    @PostMapping("/add")
    public R<TrainingTaskVo> insert(@RequestBody TrainingTaskBo trainingTaskBo) {
        if (log.isInfoEnabled()){
            log.info("TrainingTaskController.insert.trainingTaskBo={}",trainingTaskBo);
        }
        return R.ok(this.trainingTaskService.save(trainingTaskBo));
    }

    /**
     * 修改数据
     *
     * @param trainingTaskBo 实体对象
     * @return 修改结果
     */
    @PutMapping("/update")
    public R<Void> update(@RequestBody TrainingTaskBo trainingTaskBo) {
        if (log.isInfoEnabled()){
            log.info("TrainingTaskController.update.trainingTaskBo={}",trainingTaskBo);
        }
        return toAjax(this.trainingTaskService.updateById(trainingTaskBo));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("idList") List<Long> idList) {
        if (log.isInfoEnabled()){
            log.info("TrainingTaskController.delete.idList={}",idList);
        }
        return toAjax(this.trainingTaskService.removeByIds(idList));
    }
}
