package org.dromara.teachers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.TeacherExerciseTypeBo;
import org.dromara.teachers.domain.entity.TeacherExerciseType;
import org.dromara.teachers.domain.vo.TeacherExerciseTypeVo;

import java.util.List;

/**
 * 运动类型表(TeacherExerciseType)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-14 14:27:41
 */
public interface TeacherExerciseTypeService extends IService<TeacherExerciseType> {

    int save(TeacherExerciseTypeBo teacherExerciseType);

    int updateById(TeacherExerciseTypeBo teacherExerciseTypeBo);


    TableDataInfo<TeacherExerciseTypeVo> pageList(TeacherExerciseTypeBo teacherExerciseTypeBo, PageQuery pageQuery);

    List<TeacherExerciseTypeVo> selectList(TeacherExerciseTypeBo teacherExerciseTypeBo);

}

