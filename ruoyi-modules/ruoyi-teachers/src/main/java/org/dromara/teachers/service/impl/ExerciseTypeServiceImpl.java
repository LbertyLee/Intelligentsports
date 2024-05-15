package org.dromara.teachers.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.ExerciseTypeBo;
import org.dromara.teachers.domain.entity.ExerciseType;
import org.dromara.teachers.domain.vo.ExerciseTypeVo;
import org.dromara.teachers.mapper.ExerciseTypeMapper;
import org.dromara.teachers.service.ExerciseTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 运动类型表(TeacherExerciseType)表服务实现类
 *
 * @author LbertyLee
 * @since 2024-05-14 14:27:41
 */
@Slf4j
@Service("teacherExerciseTypeService")
@RequiredArgsConstructor
public class ExerciseTypeServiceImpl extends ServiceImpl<ExerciseTypeMapper, ExerciseType> implements ExerciseTypeService {

    private final ExerciseTypeMapper teacherExerciseTypeMapper;
    /**
     * 保存教师作业类型信息
     *
     * @param teacherExerciseTypeBo 教师作业类型实体对象，包含要保存的作业类型信息
     * @return 返回影响的行数，通常为插入的行数
     */
    @Override
    public int save(ExerciseTypeBo teacherExerciseTypeBo) {
        ExerciseType teacherExerciseType = BeanUtil.copyProperties(teacherExerciseTypeBo, ExerciseType.class);
        if(log.isInfoEnabled()){
            log.info("TeacherExerciseTypeServiceImpl.save.teacherExerciseType:{}",teacherExerciseType);
        }
        // 调用mapper层执行插入操作，先将入参转换为对应的实体类
        return teacherExerciseTypeMapper.insert(teacherExerciseType);
    }

    @Override
    public int updateById(ExerciseTypeBo teacherExerciseTypeBo) {
        ExerciseType teacherExerciseType = BeanUtil.copyProperties(teacherExerciseTypeBo, ExerciseType.class);
        if(log.isInfoEnabled()){
            log.info("TeacherExerciseTypeServiceImpl.updateById.teacherExerciseType:{}",teacherExerciseType);
        }
        return teacherExerciseTypeMapper.updateById(teacherExerciseType);
    }

    /**
     * 分页查询教师练习类型信息
     *
     * @param teacherExerciseTypeBo 教师练习类型业务对象，用于构建查询条件
     * @param pageQuery 分页查询参数，包含页码和每页大小等信息
     * @return 返回教师练习类型的数据信息，包含分页结果和总记录数等
     */
    @Override
    public TableDataInfo<ExerciseTypeVo> pageList(ExerciseTypeBo teacherExerciseTypeBo, PageQuery pageQuery) {
        // 根据查询条件和分页参数，执行数据库查询
        Page<ExerciseTypeVo> page =teacherExerciseTypeMapper
            .selectExerciseTypeVoPage(pageQuery.build(),this.buildQueryWrapper(teacherExerciseTypeBo));
        // 将查询结果封装成TableDataInfo对象返回
        return TableDataInfo.build(page);
    }

    @Override
    public List<ExerciseTypeVo> selectList(ExerciseTypeBo teacherExerciseTypeBo) {
        if(log.isInfoEnabled()){
            log.info("TeacherExerciseTypeServiceImpl.selectList.teacherExerciseTypeBo:{}",teacherExerciseTypeBo);
        }
        List<ExerciseType> teacherExerciseTypeList = teacherExerciseTypeMapper
            .selectList(this.buildQueryWrapper(teacherExerciseTypeBo));
        return MapstructUtils.convert(teacherExerciseTypeList, ExerciseTypeVo.class);
    }

    @Override
    public ExerciseTypeVo selectById(Long exerciseTypeId) {
        return teacherExerciseTypeMapper.selectVoById(exerciseTypeId);
    }

    /**
     * 根据给定的教师作业类型信息构建查询包装器。
     *
     * @param teacherExerciseTypeBo 教师作业类型业务对象，包含作业名称等查询条件。
     * @return Wrapper<TeacherExerciseType> 查询条件包装器，用于后续的数据库查询。
     */
    private Wrapper<ExerciseType> buildQueryWrapper(ExerciseTypeBo teacherExerciseTypeBo){
        // 创建一个空的查询包装器
        QueryWrapper<ExerciseType> wrapper = Wrappers.query();
        // 根据作业名称添加LIKE查询条件，如果作业名称不为空
        return wrapper
            .like(ObjectUtil.isNotNull(teacherExerciseTypeBo.getExerciseName()),
                "exercise_name",
                teacherExerciseTypeBo.getExerciseName()
            );
    }

}

