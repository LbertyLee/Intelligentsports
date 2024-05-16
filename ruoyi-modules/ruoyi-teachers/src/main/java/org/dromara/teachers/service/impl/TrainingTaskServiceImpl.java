package org.dromara.teachers.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.binder.BaseUnits;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.constants.Constants;
import org.dromara.teachers.domain.bo.TrainingTaskBo;
import org.dromara.teachers.domain.bo.TrainingTeamBo;
import org.dromara.teachers.domain.bo.TrainingTeamStudentBo;
import org.dromara.teachers.domain.entity.*;
import org.dromara.teachers.domain.vo.*;
import org.dromara.teachers.mapper.TrainingTaskMapper;
import org.dromara.teachers.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 训练任务表(TeacherTrainingTask)表服务实现类
 *
 * @author LbertyLee
 * @since 2024-05-15 11:18:55
 */
@Slf4j
@RequiredArgsConstructor
@Service("teacherTrainingTaskService")
public class TrainingTaskServiceImpl implements TrainingTaskService {


    private final TrainingTaskMapper trainingTaskMapper;

    private final TrainingTeamStudentService trainingTeamStudentService;

    private final StudentInfoService studentInfoService;

    private final ExerciseTypeService exerciseTypeService;

    private final TrainingTeamService trainingTeamService;


    @Override
public TrainingTaskVo selectTaskBaseInfoByTaskId(Long taskId) {
    if (log.isInfoEnabled()) {
        log.info("Start querying task base info by taskId, taskId:{}", taskId);
    }
    TrainingTaskVo trainingTaskVo = null;
    try {
        // 调用mapper层执行查询操作，增加空指针异常处理
        trainingTaskVo = BeanUtil.copyProperties(trainingTaskMapper.selectById(taskId), TrainingTaskVo.class);
        if (trainingTaskVo == null) {
            if (log.isInfoEnabled()) {
                log.info("No task found with taskId:{}", taskId);
            }
            throw new RuntimeException("该训练任务不存在,请输入正确的训练任务编}");// 或者抛出一个自定义异常
        }
        // 学生id列表
        List<Long> studentIdList = trainingTeamStudentService
            .selectList(new TrainingTeamStudentBo().setTrainingTeamId(trainingTaskVo.getTrainingTeamId()))
            .stream().map(TrainingTeamStudentVo::getStudentId).toList();
        if (ObjectUtil.isEmpty(studentIdList)) {
            return trainingTaskVo;
        }
        //设置任务中的学生id列表
        trainingTaskVo.setStudents(studentIdList);
        //设置手环总数
        trainingTaskVo.setBraceletsTotal(studentIdList.size());
        List<StudentInfoVo> studentInfoVoList = studentInfoService.batchSelectStudentInfoListByStudentIdList(studentIdList);
        //在线手环数量统计
        List<String> braceletIdList = studentInfoVoList.stream().map(StudentInfoVo::getUuid).toList();
        if (ObjectUtil.isEmpty(braceletIdList)) {
            trainingTaskVo.setBraceletsOnlineNum(0);
        } else {
            // todo 根据手环id列表查询在线手环数量
            trainingTaskVo.setBraceletsOnlineNum(braceletIdList.size());
        }
    } catch (Exception e) {
        // 异常处理逻辑
        log.error("Error occurred while querying task base info by taskId, taskId:{}", taskId, e);
        // 根据实际情况，可能需要抛出自定义异常或返回null
        throw new RuntimeException("Error occurred while querying task base info by taskId");
    }
    return trainingTaskVo;
}


    /**
     * 保存训练任务信息
     *
     * @param trainingTaskBo 训练任务业务对象，包含需要保存的训练任务的详细信息。
     * @return 返回插入操作影响的行数，通常为1表示插入成功。
     */
    @Override
    @Transactional
    public TrainingTaskVo save(TrainingTaskBo trainingTaskBo) {
        if (log.isInfoEnabled()) {
            log.info("TrainingTaskServiceImpl.save.trainingTaskBo:{}", trainingTaskBo);
        }
        TrainingTask trainingTask = BeanUtil.copyProperties(trainingTaskBo, TrainingTask.class);
        trainingTaskMapper.insert(trainingTask);
        return BeanUtil.copyProperties(trainingTask, TrainingTaskVo.class);
    }

    /**
     * 根据ID更新培训任务信息。
     *
     * @param trainingTaskBo 培训任务业务对象，包含需要更新的培训任务信息。
     * @return 返回更新的记录条数，通常为1表示更新成功。
     */
    @Override
    public int updateById(TrainingTaskBo trainingTaskBo) {
        if (log.isInfoEnabled()) {
            log.info("TrainingTaskServiceImpl.updateById.trainingTaskBo:{}", trainingTaskBo);
        }
        // 调用培训任务Mapper，使用BeanUtil工具类将trainingTaskBo的属性复制到TrainingTask实体类后进行更新
        return trainingTaskMapper.updateById(BeanUtil.copyProperties(trainingTaskBo, TrainingTask.class));
    }

    /**
     * 根据提供的ID列表删除训练任务。
     *
     * @param idList 要删除的训练任务的ID列表，类型为List<Long>。
     * @return 返回删除的记录数，类型为int。
     * @Override
     */
    public int removeByIds(List<Long> idList) {
        if (log.isInfoEnabled()) {
            log.info("TrainingTaskServiceImpl.removeByIds.idList:{}", idList);
        }
        // 调用mapper层执行批量删除操作
        return trainingTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public TrainingTaskVo selectOne(Long id) {
        if (log.isInfoEnabled()) {
            log.info("TrainingTaskServiceImpl.selectOne.id:{}", id);
        }
        TrainingTask trainingTask = trainingTaskMapper.selectById(id);
        TrainingTaskVo trainingTaskVo = BeanUtil.copyProperties(trainingTask, TrainingTaskVo.class);
        //todo 学生数据封装
        return trainingTaskVo;
    }

    /**
     * 分页查询训练任务信息
     *
     * @param trainingTaskBo 查询条件对象，包含训练任务的过滤条件等
     * @param pageQuery      分页查询条件对象，包含页码和每页大小等信息
     * @return TableDataInfo<TrainingTaskVo> 分页查询结果，包含数据列表、总条数、总页数等信息
     */
    @Override
    public TableDataInfo<TrainingTaskVo> selectPageTrainingTask(TrainingTaskBo trainingTaskBo, PageQuery pageQuery) {
        // 根据查询条件和分页条件执行数据库查询
        Page<TrainingTaskVo> page = trainingTaskMapper
            .selectPageTrainingTaskList(pageQuery.build(), this.buildQueryWrapper(trainingTaskBo));
        // 将查询结果包装成TableDataInfo对象返回
        return TableDataInfo.build(page);
    }



    private Wrapper<TrainingTask> buildQueryWrapper(TrainingTaskBo trainingTaskBo) {
        // 创建一个空的查询Wrapper
        QueryWrapper<TrainingTask> wrapper = Wrappers.query();
        // 如果团队名称不为空，则在查询中添加like条件
        return wrapper.like(ObjectUtil.isNotNull(trainingTaskBo.getTrainingTeamName()),
                "Training_team_name", trainingTaskBo.getTrainingTeamName())
            .eq(ObjectUtil.isNotNull(trainingTaskBo.getExerciseTypeName()),
                "Exercise_type_name", trainingTaskBo.getExerciseTypeName())
            .eq(ObjectUtil.isNotNull(trainingTaskBo.getTeacherName()),
                "Teacher_name", trainingTaskBo.getTeacherName());
    }
}

