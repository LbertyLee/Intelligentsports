package org.dromara.teachers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.ExerciseTypeBo;
import org.dromara.teachers.domain.entity.ExerciseType;
import org.dromara.teachers.domain.vo.ExerciseTypeVo;

import java.util.List;

/**
 * 运动类型表(TeacherExerciseType)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-14 14:27:41
 */
public interface ExerciseTypeService extends IService<ExerciseType> {

    int save(ExerciseTypeBo teacherExerciseType);

    int updateById(ExerciseTypeBo teacherExerciseTypeBo);


    TableDataInfo<ExerciseTypeVo> pageList(ExerciseTypeBo teacherExerciseTypeBo, PageQuery pageQuery);

    List<ExerciseTypeVo> selectList(ExerciseTypeBo teacherExerciseTypeBo);

    ExerciseTypeVo selectById(Long exerciseTypeId);

}

