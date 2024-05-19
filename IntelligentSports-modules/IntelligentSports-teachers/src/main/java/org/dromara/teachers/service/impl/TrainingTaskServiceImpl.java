package org.dromara.teachers.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.constants.Constants;
import org.dromara.teachers.domain.bo.TaskHealthMetricsBo;
import org.dromara.teachers.domain.bo.TrainingTaskBo;
import org.dromara.teachers.domain.bo.TrainingTeamStudentBo;
import org.dromara.teachers.domain.bo.DetectionDataBo;
import org.dromara.teachers.domain.entity.*;
import org.dromara.teachers.domain.vo.*;
import org.dromara.teachers.mapper.TrainingTaskMapper;
import org.dromara.teachers.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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

    private final BraceletStatusService braceletStatusService;

    private final HealthMetricsService healthMetricsService;

    private final TaskHealthMetricsService taskHealthMetricsService;


    @Override
    public int resetTrainingTask(Long taskId) {
        if (log.isInfoEnabled()){
            log.info("TrainingTaskServiceImpl.resetTrainingTask.taskId:{}", taskId);
        }
        return taskHealthMetricsService.resetTaskHealthMetrics(taskId);
    }

    @Override
    public DetectionDataVo selectDetectionData(DetectionDataBo detectionDataBo) {
        if (log.isInfoEnabled()) {
            log.info("TrainingTaskServiceImpl.selectDetectionData.detectionDataBo{}", detectionDataBo);
        }
        DetectionDataVo detectionDataVo = new DetectionDataVo();
        List<Long> studentIds = detectionDataBo.getStudentIds();
        List<StudentInfoVo> studentInfoVoList = studentInfoService.batchSelectStudentInfoListByStudentIdList(studentIds);
        if (ObjectUtil.isEmpty(studentInfoVoList)) {
            return detectionDataVo;
        }
        //学生手环id列表
        List<String> braceletsTotalNum = studentInfoVoList.stream().map(StudentInfoVo::getUuid).toList();
        detectionDataVo.setBraceletsTotalNum(braceletsTotalNum.size());
        //在线手环列表
        List<BraceletStatusVo> braceletStatusVoList = braceletStatusService.selectBraceletList(braceletsTotalNum);
        List<BraceletStatusVo> braceletStatusVos = braceletStatusVoList.stream()
            .filter(braceletStatusVo -> Objects.equals(braceletStatusVo.getIsOnline(), Constants.IsOnline)).toList();
        detectionDataVo.setBraceletsOnlineNum(braceletStatusVos.size());
        //在线的手环的uuid
        List<String> braceletsIdList = braceletStatusVos.stream().map(BraceletStatusVo::getUuid).toList();
        //手环实时数据
//        long time = System.currentTimeMillis()/1000;
        long time = 1714585138;  //模拟时间
        List<List<TaskHealthMetricsVo>> healthMetricsVos = healthMetricsService.selectHealthMetricsListByBraceletsIdList(braceletsIdList, time);
        if (ObjectUtil.isEmpty(healthMetricsVos)) {
            return detectionDataVo;
        }
        //最新数据
        List<TaskHealthMetricsVo> list = healthMetricsVos.stream().map(
            healthMetricsVoList -> {
                TaskHealthMetricsVo taskHealthMetricsVoNew = healthMetricsVoList.get(0);
                TaskHealthMetricsVo taskHealthMetricsVo = healthMetricsVoList.get(1);
                //计算实时配速[实时配速=(上一秒的总距离-此时的总距离)/1s]
                Long taskHealthMetricsVoNewTotalDistance = taskHealthMetricsVoNew.getTotalDistance();
                Long healthMetricsVoTotalDistance = taskHealthMetricsVo.getTotalDistance();
                taskHealthMetricsVoNew.setMatchingSpeed((taskHealthMetricsVoNewTotalDistance-healthMetricsVoTotalDistance));
                return taskHealthMetricsVoNew;
            }).toList();
        detectionDataVo.setTaskHealthMetricsVoList(list);
        if (ObjectUtil.isNotNull(list)) {
            List<TaskHealthMetricsBo> convert = MapstructUtils.convert(list, TaskHealthMetricsBo.class);
            if (ObjectUtil.isNotNull(convert)) {
                List<TaskHealthMetricsBo> taskHealthMetricsBos = convert.stream().peek(
                    taskHealthMetricsBo -> taskHealthMetricsBo.setTaskId(detectionDataBo.getTaskId())
                ).toList();
                taskHealthMetricsService.insertList(taskHealthMetricsBos);
            }
        }
        return detectionDataVo;
    }



    /**
     * 根据任务ID查询训练任务的基本信息。
     *
     * @param taskId 训练任务的ID，不能为空。
     * @return TrainingTaskVo 训练任务的视图对象，包含了任务的基本信息以及关联的学生ID列表。
     * @throws ServiceException 如果指定的训练任务不存在，则抛出异常。
     */
    @Override
    public TrainingTaskVo selectTaskBaseInfoByTaskId(Long taskId) {
        if (log.isInfoEnabled()) {
            log.info("Start querying task base info by taskId, taskId:{}", taskId);
        }
        TrainingTaskVo trainingTaskVo = null;
        trainingTaskVo = BeanUtil.copyProperties(trainingTaskMapper.selectById(taskId), TrainingTaskVo.class);
        if (ObjectUtil.isNull(trainingTaskVo)) {
            throw new ServiceException("该训练任务不存在,请输入正确的训练任务编}");
        }
        List<Long> studentIdList = trainingTeamStudentService
            .selectList(new TrainingTeamStudentBo().setTrainingTeamId(trainingTaskVo.getTrainingTeamId()))
            .stream().map(TrainingTeamStudentVo::getStudentId).toList();
        if (ObjectUtil.isEmpty(studentIdList)) {
            return trainingTaskVo;
        }
        trainingTaskVo.setStudents(studentIdList);
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

