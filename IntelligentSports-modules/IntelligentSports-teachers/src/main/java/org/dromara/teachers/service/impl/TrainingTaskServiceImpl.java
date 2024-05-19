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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
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

    private final BraceletStatusService braceletStatusService;

    private final HealthMetricsService healthMetricsService;

    private final TaskHealthMetricsService taskHealthMetricsService;



    @Override
    public int resetTrainingTask(Long taskId) {
        if (log.isInfoEnabled()) {
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
        // 创建一个固定大小的线程池
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(20);
        try {
            // 异步获取学生信息列表
            CompletableFuture<List<StudentInfoVo>> studentInfoFuture = CompletableFuture.supplyAsync(() ->
                studentInfoService.batchSelectStudentInfoListByStudentIdList(studentIds), threadPoolExecutor);
            // 获取学生信息列表
            List<StudentInfoVo> studentInfoVoList = studentInfoFuture.get();
            if (ObjectUtil.isEmpty(studentInfoVoList)) {
                return detectionDataVo;
            }
            // 获取学生手环id列表
            List<String> braceletsTotalNum = studentInfoVoList.stream()
                .map(StudentInfoVo::getUuid)
                .collect(Collectors.toList());
            detectionDataVo.setBraceletsTotalNum(braceletsTotalNum.size());
            // 异步获取在线手环列表
            CompletableFuture<List<BraceletStatusVo>> braceletStatusFuture = CompletableFuture.supplyAsync(() ->
                braceletStatusService.selectBraceletList(braceletsTotalNum), threadPoolExecutor);
            // 获取在线手环列表
            List<BraceletStatusVo> braceletStatusVoList = braceletStatusFuture.get();
            List<BraceletStatusVo> onlineBracelets = braceletStatusVoList.stream()
                .filter(braceletStatusVo -> Objects.equals(braceletStatusVo.getIsOnline(), Constants.IsOnline))
                .toList();
            detectionDataVo.setBraceletsOnlineNum(onlineBracelets.size());
            // 获取在线手环的UUID列表
            List<String> onlineBraceletIds = onlineBracelets.stream()
                .map(BraceletStatusVo::getUuid)
                .collect(Collectors.toList());
            // 模拟时间戳
            long time = 1714585138;
            // 异步获取手环实时数据
            CompletableFuture<List<List<TaskHealthMetricsVo>>> healthMetricsFuture = CompletableFuture.supplyAsync(() ->
                healthMetricsService.selectHealthMetricsListByBraceletsIdList(onlineBraceletIds, time), threadPoolExecutor);
            // 获取手环实时数据
            List<List<TaskHealthMetricsVo>> healthMetricsVos = healthMetricsFuture.get();
            if (ObjectUtil.isEmpty(healthMetricsVos)) {
                return detectionDataVo;
            }
            // 处理最新数据，计算实时配速
            List<TaskHealthMetricsVo> latestMetrics = healthMetricsVos.stream()
                .map(metrics -> {
                    TaskHealthMetricsVo currentMetrics = metrics.get(0);
                    TaskHealthMetricsVo previousMetrics = metrics.get(1);
                    long currentDistance = currentMetrics.getTotalDistance();
                    long previousDistance = previousMetrics.getTotalDistance();
                    currentMetrics.setMatchingSpeed(currentDistance - previousDistance);
                    return currentMetrics;
                }).collect(Collectors.toList());
            detectionDataVo.setTaskHealthMetricsVoList(latestMetrics);
            // 异步插入处理后的健康指标数据
            if (ObjectUtil.isNotNull(latestMetrics)) {
                List<TaskHealthMetricsBo> metricsBoList = MapstructUtils.convert(latestMetrics, TaskHealthMetricsBo.class);
                if (ObjectUtil.isNotNull(metricsBoList)) {
                    metricsBoList.forEach(metricsBo -> metricsBo.setTaskId(detectionDataBo.getTaskId()));
                    CompletableFuture.runAsync(() -> taskHealthMetricsService.insertList(metricsBoList), threadPoolExecutor).get();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error in selectDetectionData: ", e);
            Thread.currentThread().interrupt();
        } finally {
            threadPoolExecutor.shutdown();
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
        // 查询训练任务信息并进行转换
        TrainingTaskVo trainingTaskVo = BeanUtil.copyProperties(trainingTaskMapper.selectById(taskId), TrainingTaskVo.class);
        if (ObjectUtil.isNull(trainingTaskVo)) {
            throw new ServiceException("该训练任务不存在，请输入正确的训练任务编号");
        }
        // 获取学生ID列表
        List<Long> studentIdList = trainingTeamStudentService
            .selectList(new TrainingTeamStudentBo().setTrainingTeamId(trainingTaskVo.getTrainingTeamId()))
            .stream().map(TrainingTeamStudentVo::getStudentId)
            .toList();
        // 如果学生ID列表不为空，则设置到训练任务信息中
        if (!ObjectUtil.isEmpty(studentIdList)) {
            trainingTaskVo.setStudents(studentIdList);
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

