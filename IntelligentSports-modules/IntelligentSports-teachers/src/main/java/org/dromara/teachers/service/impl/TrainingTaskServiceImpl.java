package org.dromara.teachers.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
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
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.ToIntFunction;
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

    private final StudentTrainingTaskInfoService studentTrainingTaskInfoService;


    @Override
    public List<StudentTrainingTaskInfoVo> getLine(Long taskId, String braceletId) {
        return studentTrainingTaskInfoService.selectList(taskId, braceletId);
    }

    /**
     * 根据学生ID获取学生训练任务信息。
     *
     * @param taskId     训练任务的ID。
     * @param braceletId 学生佩戴的腕带ID。
     * @return 返回学生训练任务的信息汇总，包括实时和历史的健康指标。
     */
    @Override
    @Transactional
    public StudentTrainingTaskInfoVo getStudentTrainingTaskInfoByStudentId(Long taskId, String braceletId) {
        if (log.isInfoEnabled()) {
            log.info("getStudentTrainingTaskInfoByStudentId taskId: {}, braceletId: {}", taskId, braceletId);
        }
        long time = (System.currentTimeMillis() / 1000) - 5;
        List<TaskHealthMetricsVo> taskHealthMetricsVoList = healthMetricsService.selectHealthMetricsListByBraceletsId(braceletId, time);
        StudentTrainingTaskInfoVo studentTrainingTaskInfoVo = new StudentTrainingTaskInfoVo();
        if (taskHealthMetricsVoList.size() == 2) {
            //最新数据
            TaskHealthMetricsVo currentMetrics = taskHealthMetricsVoList.get(0);
            //上一条数据
//            TaskHealthMetricsVo previousMetrics = taskHealthMetricsVoList.get(1);
            //设置实时心率和血氧
            studentTrainingTaskInfoVo.setRealTimeHeartRate(currentMetrics.getHeartRate());
//            studentTrainingTaskInfoVo.setRealTimeBloodOxygen(currentMetrics.getBloodOxygen());
//            int matchingSpeed = currentMetrics.getTotalDistance() - previousMetrics.getTotalDistance();
            //设置实时配速
//            studentTrainingTaskInfoVo.setRealTimePace(matchingSpeed);
//            currentMetrics.setMatchingSpeed(matchingSpeed);

        } else {
            log.warn("Expected 2 elements in taskHealthMetricsVoList, but found {}", taskHealthMetricsVoList.size());
            return studentTrainingTaskInfoVo;
        }
        try {
            //获取学生训练任务历史信息
            List<TaskHealthMetricsVo> historyMetricsList = taskHealthMetricsService.selectTaskHealthMetricsList(taskId, braceletId);
            if (!historyMetricsList.isEmpty()) {
                historyMetricsList.add(taskHealthMetricsVoList.get(0));
                setMetrics(studentTrainingTaskInfoVo, historyMetricsList);
            }
        } catch (Exception e) {
            log.error("Exception occurred while fetching task health metrics", e);
        }

        studentTrainingTaskInfoVo.setTaskId(taskId);
        studentTrainingTaskInfoVo.setBraceletId(braceletId);
        studentTrainingTaskInfoService.insert(studentTrainingTaskInfoVo);
        List<StudentTrainingTaskInfoVo> studentTrainingTaskInfoVos = studentTrainingTaskInfoService.selectList(taskId, braceletId);
        studentTrainingTaskInfoVo.setList(studentTrainingTaskInfoVos);
        return studentTrainingTaskInfoVo;
    }


    /**
     * 设置学生的训练任务指标信息。
     *
     * @param studentTrainingTaskInfoVo 学生训练任务信息载体对象，用于接收计算后的各项指标数据。
     * @param historyMetricsList        历史指标数据列表，用于计算各项指标的最新值和平均值、最大值。
     */
    private void setMetrics(StudentTrainingTaskInfoVo studentTrainingTaskInfoVo, List<TaskHealthMetricsVo> historyMetricsList) {
        if (ObjectUtil.isEmpty(historyMetricsList)) {
            return;
        }
        // 获取最新的两个历史信息
//        TaskHealthMetricsVo latestMetrics = historyMetricsList.get(historyMetricsList.size() - 1);
//        TaskHealthMetricsVo previousMetrics = historyMetricsList.get(historyMetricsList.size() - 2);

//        int totalDistance = latestMetrics.getTotalDistance();
//        int totalCalories = latestMetrics.getTotalCalories();

//        studentTrainingTaskInfoVo.setRealTimeStepNumber(totalDistance - previousMetrics.getTotalDistance());
//        studentTrainingTaskInfoVo.setBurningCalories(totalCalories - previousMetrics.getTotalCalories());
        //设置平均心率、平均血氧、平均配速
//        studentTrainingTaskInfoVo.setAverageBloodOxygen(calculateMetric(historyMetricsList, TaskHealthMetricsVo::getBloodOxygen));
        studentTrainingTaskInfoVo.setAverageHeartRate(calculateMetric(historyMetricsList, TaskHealthMetricsVo::getHeartRate));
//        studentTrainingTaskInfoVo.setAveragePace(calculateMetric(historyMetricsList, TaskHealthMetricsVo::getMatchingSpeed));
        //设置最大心率、最大血氧、最大配速
//        studentTrainingTaskInfoVo.setMaxBloodOxygen(calculateMaxMetric(historyMetricsList, TaskHealthMetricsVo::getBloodOxygen));
        studentTrainingTaskInfoVo.setMaxHeartRate(calculateMaxMetric(historyMetricsList, TaskHealthMetricsVo::getHeartRate));
//        studentTrainingTaskInfoVo.setMaxPace(calculateMaxMetric(historyMetricsList, TaskHealthMetricsVo::getMatchingSpeed));
    }

    /**
     * 计算给定任务健康指标列表的平均指标值。
     *
     * @param list   任务健康指标列表，不应为null。
     * @param mapper 一个函数接口，用于从TaskHealthMetricsVo对象映射到int值。
     * @return 计算得到的平均指标值，如果列表为空则返回0。
     */
    private int calculateMetric(List<TaskHealthMetricsVo> list, ToIntFunction<TaskHealthMetricsVo> mapper) {
        // 使用stream和mapper从列表中映射出int值，并计算平均值
        return (int) list.stream().mapToInt(mapper).average().orElse(0);
    }

    /**
     * 计算给定任务健康指标列表中最大指标值。
     *
     * @param list   任务健康指标列表，不应为null。
     * @param mapper 一个函数接口，用于从TaskHealthMetricsVo对象映射到int值。
     * @return 计算得到的最大指标值，如果列表为空则返回0。
     */
    private int calculateMaxMetric(List<TaskHealthMetricsVo> list, ToIntFunction<TaskHealthMetricsVo> mapper) {
        // 使用stream和mapper从列表中映射出int值，并计算最大值
        return list.stream().mapToInt(mapper).max().orElse(0);
    }


    /**
     * 重置任务
     *
     * @param taskId
     * @return
     */
    @Override
    public int resetTrainingTask(Long taskId) {
        if (log.isInfoEnabled()) {
            log.info("TrainingTaskServiceImpl.resetTrainingTask.taskId:{}", taskId);
        }
        return taskHealthMetricsService.resetTaskHealthMetrics(taskId);
    }


    /**
     * 异步查询检测数据。
     * 使用CompletableFuture和线程池来实现学生信息的异步查询和处理，以提高查询效率。
     *
     * @param detectionDataBo 检测数据的业务对象，包含学生ID列表。
     * @return 返回处理后的检测数据的视图对象。
     */
    @Override
    @Transactional
    public DetectionDataVo selectDetectionData(DetectionDataBo detectionDataBo) {
        // 日志记录输入参数
        if (log.isInfoEnabled()) {
            log.info("TrainingTaskServiceImpl.selectDetectionData.detectionDataBo{}", detectionDataBo);
        }
        // 创建固定大小的线程池，用于执行异步任务
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(40);
        // 初始化返回的检测数据视图对象
        DetectionDataVo detectionDataVo = new DetectionDataVo();
        // 获取待查询的学生ID列表
        List<Long> studentIds = detectionDataBo.getStudentIds();
        try {
            // 异步查询学生信息，并将结果封装为CompletableFuture
            // 使用CompletableFuture来异步处理学生信息
            CompletableFuture<List<StudentInfoVo>> studentInfoFuture = CompletableFuture.supplyAsync(() ->
                studentInfoService.batchSelectStudentInfoListByStudentIdList(studentIds), threadPoolExecutor);
            // 创建一个CompletableFuture，用于等待所有异步任务完成
            // 使用allOf等待所有异步任务完成
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                studentInfoFuture,
                CompletableFuture.runAsync(() -> {
                    try {
                        // 获取学生信息Future的结果，即查询到的学生信息列表
                        List<StudentInfoVo> studentInfoVoList = studentInfoFuture.get();
                        // 如果查询结果不为空，则处理学生信息
                        if (!ObjectUtil.isEmpty(studentInfoVoList)) {
                            this.processStudentInfo(studentInfoVoList, detectionDataVo, detectionDataBo.getTaskId(),
                                threadPoolExecutor, detectionDataBo.getNumber());
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        // 记录处理学生信息过程中的异常
                        log.error("Error processing student info: ", e);
                        // 设置当前线程的中断状态
                        Thread.currentThread().interrupt();
                    }
                }, threadPoolExecutor)
            );
            // 等待所有异步任务完成
            allFutures.join(); // 非阻塞等待所有任务完成
        } finally {
            // 关闭线程池
            // 确保线程池被关闭
            threadPoolExecutor.shutdown();
            try {
                // 等待线程池关闭，如果在指定时间内未关闭，则记录日志
                if (!threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    log.error("Thread pool did not terminate");
                }
            } catch (InterruptedException e) {
                // 设置当前线程的中断状态
                Thread.currentThread().interrupt();
            }
        }
        // 返回处理后的检测数据视图对象
        return detectionDataVo;
    }

    /**
     * 处理学生信息，包括获取学生手环的状态和健康指标数据。
     * 异步方式进行，以提高处理效率。
     *
     * @param studentInfoVoList  学生信息列表，包含每个学生的手环UUID。
     * @param detectionDataVo    用于存储检测数据的vo对象，包括手环总数、在线手环数和健康指标数据。
     * @param taskId             任务ID，用于关联健康指标数据和特定任务。
     * @param threadPoolExecutor 线程池执行器，用于异步处理手环状态和健康指标数据的获取。
     */
    private void processStudentInfo(List<StudentInfoVo> studentInfoVoList, DetectionDataVo detectionDataVo, Long taskId,
                                    ExecutorService threadPoolExecutor, Integer number) {
        if (ObjectUtil.isEmpty(studentInfoVoList)) {
            return;
        }
        // 获取学生手环id列表
        List<String> braceletsTotalNum = studentInfoVoList.stream()
            .map(StudentInfoVo::getUuid)
            .collect(Collectors.toList());
        //手环总数
        detectionDataVo.setBraceletsTotalNum(braceletsTotalNum.size());
        CompletableFuture<Void> braceletStatusFuture = CompletableFuture.runAsync(() -> {
            try {
                // 异步获取在线手环列表
//                List<BraceletStatusVo> braceletStatusVoList = braceletStatusService.selectBraceletList(braceletsTotalNum);
//                List<BraceletStatusVo> onlineBracelets = braceletStatusVoList.stream()
//                    .filter(braceletStatusVo -> Objects.equals(braceletStatusVo.getIsOnline(), Constants.IsOnline))
//                    .toList();
                // 获取在线手环的UUID列表
//                List<String> onlineBraceletIds = braceletsTotalNum.stream()
//                    .map(BraceletStatusVo::getUuid)
//                    .collect(Collectors.toList());
                long time = (System.currentTimeMillis() / 1000) - 5;
                // 异步获取手环实时数据
                Map<String, List<TaskHealthMetricsVo>> healthMetricsVoMap = healthMetricsService
                    .selectHealthMetricsMapByBraceletsIdList(braceletsTotalNum, time);
                List<TaskHealthMetricsVo> taskHealthMetricsVoArrayList = new ArrayList<>();
                healthMetricsVoMap.forEach(
                    (braceletsId, healthMetricsVoList) -> {
                        TaskHealthMetricsVo currentMetrics = new TaskHealthMetricsVo();
                        if (!healthMetricsVoList.isEmpty()) {
                            currentMetrics = healthMetricsVoList.get(0);
                            taskHealthMetricsVoArrayList.add(currentMetrics);
                        }
                    }
                );
                //手环在线数
                detectionDataVo.setBraceletsOnlineNum(taskHealthMetricsVoArrayList.size());
                //学生实时数据
                detectionDataVo.setTaskHealthMetricsVoList(taskHealthMetricsVoArrayList);
                //异步插入数据
                if (!taskHealthMetricsVoArrayList.isEmpty()) {
                    List<TaskHealthMetricsBo> metricsBoList = MapstructUtils.convert(taskHealthMetricsVoArrayList, TaskHealthMetricsBo.class);
                    metricsBoList.forEach(metricsBo -> metricsBo.setTaskId(taskId));
                    CompletableFuture.runAsync(() -> taskHealthMetricsService.insertList(metricsBoList), threadPoolExecutor).get();
                }
//                List<List<TaskHealthMetricsVo>> healthMetricsVos = healthMetricsService
//                    .selectHealthMetricsListByBraceletsIdList(braceletsTotalNum, time);
//                if (!healthMetricsVos.isEmpty()) {
//                    List<TaskHealthMetricsVo> latestMetrics = healthMetricsVos.stream()
//                        .map(metrics -> {
//                            TaskHealthMetricsVo currentMetrics =new TaskHealthMetricsVo();
//                            TaskHealthMetricsVo previousMetrics=new TaskHealthMetricsVo() ;
//                            if (!metrics.isEmpty()) {
//                                currentMetrics = metrics.get(0);
//                                currentMetrics.setNumber(number);
//                            }
//                            if (ObjectUtil.isNotNull(metrics.get(0)) && !ObjectUtil.isNotNull(metrics.get(1))) {
//                                currentMetrics = metrics.get(0);
//                                previousMetrics = metrics.get(1);
//                            }
                // 计算实时配速
//                            if (ObjectUtil.isNotNull(currentMetrics.getTotalDistance())
//                                && ObjectUtil.isNotNull(previousMetrics.getTotalDistance())) {
//                                long currentDistance = currentMetrics.getTotalDistance();
//                                long previousDistance = previousMetrics.getTotalDistance();
//                                currentMetrics.setMatchingSpeed((int) (currentDistance - previousDistance));
//                            }
//                            return currentMetrics;
//                        })
//                        .collect(Collectors.toList());
//                    detectionDataVo.setTaskHealthMetricsVoList(latestMetrics);
                detectionDataVo.setTaskHealthMetricsVoList(taskHealthMetricsVoArrayList);
                // 异步插入处理后的健康指标数据
//                    if (!latestMetrics.isEmpty()) {
//                        List<TaskHealthMetricsBo> metricsBoList = MapstructUtils.convert(latestMetrics, TaskHealthMetricsBo.class);
//                        if (!metricsBoList.isEmpty()) {
//                            metricsBoList.forEach(metricsBo -> metricsBo.setTaskId(taskId));
//                            CompletableFuture.runAsync(() -> taskHealthMetricsService.insertList(metricsBoList), threadPoolExecutor).get();
//                        }
//                    }
//                }
            } catch (Exception e) {
                log.error("Error processing bracelet status or health metrics: ", e);
                Thread.currentThread().interrupt();
            }
        }, threadPoolExecutor);
        // 确保所有操作完成
        braceletStatusFuture.join();
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
        //修改训练人数
        trainingTaskMapper.updateById(new TrainingTask().setId(taskId).setPersonNum(studentIdList.size()));
        // 如果学生ID列表不为空，则设置到训练任务信息中
        if (!ObjectUtil.isEmpty(studentIdList)) {
            trainingTaskVo.setStudents(studentIdList);
            List<StudentInfoVo> studentInfoVos = studentInfoService.batchSelectStudentInfoListByStudentIdList(studentIdList);
            List<TaskHealthMetricsVo> list = studentInfoVos.stream().map(
                e -> {
                    return new TaskHealthMetricsVo().setStudentId(e.getId()).setStudentName(e.getName());
                }
            ).toList();
            trainingTaskVo.setStudentInfoList(list);
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
        //查询训练队下学生ID列表
        Long trainingTeamId = trainingTaskBo.getTrainingTeamId();
        List<TrainingTeamStudentVo> trainingTeamStudentVos = trainingTeamStudentService
            .selectList(new TrainingTeamStudentBo().setTrainingTeamId(trainingTeamId));
        List<Long> studentIdList = trainingTeamStudentVos.stream().map(TrainingTeamStudentVo::getStudentId).toList();
        TrainingTaskVo trainingTaskVo = BeanUtil.copyProperties(trainingTask, TrainingTaskVo.class);
        trainingTaskVo.setStudents(studentIdList);
        return trainingTaskVo;
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
        return BeanUtil.copyProperties(trainingTask, TrainingTaskVo.class);

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
        return TableDataInfo.build(page);
    }


    private Wrapper<TrainingTask> buildQueryWrapper(TrainingTaskBo trainingTaskBo) {
        // 创建一个空的查询Wrapper
        QueryWrapper<TrainingTask> wrapper = Wrappers.query();
        Map<String, Object> params = trainingTaskBo.getParams();

        // 如果团队名称不为空，则在查询中添加like条件
        if (ObjectUtil.isNotNull(trainingTaskBo.getTrainingTeamName())) {
            wrapper.like("Training_team_name", trainingTaskBo.getTrainingTeamName());
        }
        // 如果练习类型名称不为空，则在查询中添加eq条件
        if (ObjectUtil.isNotNull(trainingTaskBo.getExerciseTypeName())) {
            wrapper.eq("Exercise_type_name", trainingTaskBo.getExerciseTypeName());
        }
        // 如果教师名称不为空，则在查询中添加eq条件
        if (ObjectUtil.isNotNull(trainingTaskBo.getTeacherName())) {
            wrapper.eq("Teacher_name", trainingTaskBo.getTeacherName());
        }
        // 如果params的大小等于2，则在查询中添加between条件
        if (ObjectUtil.isNotNull(params.get("beginTime")) && ObjectUtil.isNotNull(params.get("endTime"))) {
            String beginTime = (String) params.get("beginTime");
            String endTime = (String) params.get("endTime");
            wrapper.between("create_time", Timestamp.valueOf(beginTime), Timestamp.valueOf(endTime));

        }
        return wrapper;
    }
}

