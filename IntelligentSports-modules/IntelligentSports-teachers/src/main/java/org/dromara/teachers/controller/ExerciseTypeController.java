package org.dromara.teachers.controller;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.domain.bo.ExerciseTypeBo;
import org.dromara.teachers.domain.vo.ExerciseTypeVo;
import org.dromara.teachers.service.ExerciseTypeService;
import org.springframework.web.bind.annotation.*;
import org.dromara.common.core.domain.R;

import java.util.List;

/**
 * 运动类型Controller
 *
 * @author LbertyLee
 * @since 2024-05-14 14:27:41
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher/exerciseType")
public class ExerciseTypeController extends BaseController {

    private final ExerciseTypeService teacherExerciseTypeService;


    /**
     * 查询分页数据
     */
    @GetMapping("/page")
    public TableDataInfo<ExerciseTypeVo> pageList(ExerciseTypeBo teacherExerciseTypeBo,
                                                  PageQuery pageQuery) {
        if(log.isInfoEnabled()){
            log.info("TeacherExerciseTypeController.pageList.teacherExerciseTypeBo:{}",teacherExerciseTypeBo);
            log.info("TeacherExerciseTypeController.pageList.pageQuery:{}",pageQuery);
        }
        return teacherExerciseTypeService.pageList(teacherExerciseTypeBo,pageQuery);
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public R<List<ExerciseTypeVo>> list(ExerciseTypeBo teacherExerciseTypeBo) {
        if (log.isInfoEnabled()){
            log.info("TeacherExerciseTypeController.list.teacherExerciseTypeBo:{}",teacherExerciseTypeBo);
        }
        return R.ok(teacherExerciseTypeService.selectList(teacherExerciseTypeBo));
    }


    /**
     * 新增数据
     *
     * @param teacherExerciseTypeBo 实体对象
     * @return 新增结果
     */
    @PostMapping("/add")
    public R<Void> insert(@RequestBody ExerciseTypeBo teacherExerciseTypeBo) {
        if(log.isInfoEnabled()){
            log.info("TeacherExerciseTypeController.insert.teacherExerciseTypeBo:{}",teacherExerciseTypeBo);
        }
        return toAjax(this.teacherExerciseTypeService.save(teacherExerciseTypeBo));
    }

    /**
     * 修改数据
     *
     * @param teacherExerciseTypeBo 实体对象
     * @return 修改结果
     */
    @PutMapping("/update")
    public R<Void> update(@RequestBody ExerciseTypeBo teacherExerciseTypeBo) {
        if(log.isInfoEnabled()){
            log.info("TeacherExerciseTypeController.update.teacherExerciseTypeBo:{}",teacherExerciseTypeBo);
        }
        return toAjax(this.teacherExerciseTypeService.updateById(teacherExerciseTypeBo));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("idList") List<Long> idList) {
        if(log.isInfoEnabled()){
            log.info("TeacherExerciseTypeController.delete.idList:{}",idList);
        }
        return toAjax(this.teacherExerciseTypeService.removeByIds(idList));
    }
}

