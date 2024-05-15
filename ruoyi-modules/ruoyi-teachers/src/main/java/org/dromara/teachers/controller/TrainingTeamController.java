package org.dromara.teachers.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.domain.bo.TrainingTeamBo;
import org.dromara.teachers.domain.bo.TrainingTeamStudentBo;
import org.dromara.teachers.domain.vo.TrainingTeamVo;
import org.dromara.teachers.service.TrainingTeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/teacher/trainingTeam")
public class TrainingTeamController extends BaseController {

    private final TrainingTeamService teacherTrainingTeamService;


    /**
     * 查看训练队详情
     */
    @GetMapping("/{id}")
    public R<TrainingTeamVo> get(@PathVariable Long id)
    {
        if(log.isInfoEnabled()){
            log.info("TeacherTrainingTeamController.get.id={}", id);
        }
        // 调用服务层方法，根据ID查询训练队信息
        return R.ok(teacherTrainingTeamService.selectTeacherTrainingTeamById(id));
    }
    /**
     * 查询训练队分页列表
     *
     * @param teacherTrainingTeamBo 教师培训团队业务对象，用于传递查询条件
     * @param pageQuery 分页查询参数，用于指定页码和每页记录数
     * @return TableDataInfo<TeacherTrainingTeamVo> 返回教师培训团队的分页数据信息，包括总数和数据列表
     */
    @GetMapping("/page")
    public TableDataInfo<TrainingTeamVo> list(TrainingTeamBo teacherTrainingTeamBo,
                                              PageQuery pageQuery)
    {
        // 调用服务层方法，查询并返回教师培训团队的分页列表信息
        return teacherTrainingTeamService.selectPageTeacherTrainingTeamList(teacherTrainingTeamBo, pageQuery);
    }
    @GetMapping("/list")
    public R<List<TrainingTeamVo>> list(TrainingTeamBo teacherTrainingTeamBo){
        return R.ok(teacherTrainingTeamService.list(teacherTrainingTeamBo));
    }
    /**
     * 添加训练队信息。
     *
     * @param teacherTrainingTeamBo 培训团队信息对象，包含需要添加的团队的详细信息。
     * @return 返回操作结果，成功返回R<Void>，其中isSuccess为true，失败则为false。
     */
    @PostMapping("/add")
    private R<Void> addTrainingTeam(@RequestBody TrainingTeamBo teacherTrainingTeamBo) {
        // 调用服务层方法，添加训练队信息，并将操作结果转换为Ajax响应格式返回
        return toAjax(teacherTrainingTeamService.addTrainingTeam(teacherTrainingTeamBo));
    }
    /**
     * 更新训练队伍信息。
     *
     * @param teacherTrainingTeamBo 训练队信息对象，包含需要更新的所有信息。
     * @return 返回操作结果，如果操作成功，返回码为200；否则返回相应错误代码。
     */
    @PostMapping("/update")
    private R<Void> updateTrainingTeam(@RequestBody TrainingTeamBo teacherTrainingTeamBo) {
        // 调用服务层方法，更新训练队信息，并将操作结果转换为Ajax响应格式返回
        return toAjax(teacherTrainingTeamService.updateTrainingTeam(teacherTrainingTeamBo));
    }
    /**
     * 删除训练队伍信息。
     *
     * @param id 训练队伍ID。
     * @return 返回操作结果，如果操作成功，返回码为200；否则返回相应错误代码。
     */
    @DeleteMapping("/delete/{id}")
    private R<Void> deleteTrainingTeam(@PathVariable Long id) {
        // 调用服务层方法，删除训练队信息，并将操作结果转换为Ajax响应格式返回
        return toAjax(teacherTrainingTeamService.deleteTrainingTeam(id));
    }

    /**
     * 训练队绑定学生
     */
    @PostMapping("/bindStudent")
    private R<Void> bindStudent(@RequestBody TrainingTeamStudentBo teacherTrainingTeamBindStudentBo) {
        return toAjax(teacherTrainingTeamService.bindStudent(teacherTrainingTeamBindStudentBo));
    }

}
