package org.dromara.teachers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.TrainingTaskBo;
import org.dromara.teachers.domain.bo.TrainingTeamStudentBo;
import org.dromara.teachers.domain.vo.*;
import org.dromara.teachers.service.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingReportServiceImpl implements TrainingReportService {

    private final StudentTrainingTaskInfoService studentTrainingTaskInfoService;

    private final TaskHealthMetricsService taskHealthMetricsService;

    private final StudentInfoService studentInfoService;

    private final TrainingTaskService trainingTaskService;

    private final TrainingTeamStudentService trainingTeamStudentService;


    /**
     * 查询训练报告列表
     *
     * @param trainingTaskBo
     * @param pageQuery
     * @return
     */
    @Override
    public TableDataInfo<TrainingTaskVo> selectPageTrainingReport(TrainingTaskBo trainingTaskBo, PageQuery pageQuery) {
        TableDataInfo<TrainingTaskVo> trainingTaskVoTableDataInfoS = trainingTaskService.selectPageTrainingTask(trainingTaskBo, pageQuery);
        List<TrainingTaskVo> rows = trainingTaskVoTableDataInfoS.getRows();
        // 将查询结果包装成TableDataInfo对象返回
        List<TrainingTaskVo> list = rows.stream().peek(trainingTaskVo -> {
            Long trainingTeamId = trainingTaskVo.getTrainingTeamId();
            List<TrainingTeamStudentVo> trainingTeamStudentVos = trainingTeamStudentService.selectList(new TrainingTeamStudentBo().setTrainingTeamId(trainingTeamId));
            trainingTaskVo.setPersonNum(trainingTeamStudentVos.size());
        }).toList();
        trainingTaskVoTableDataInfoS.setRows(list);
        return trainingTaskVoTableDataInfoS;
    }

    /**
     * 查询学生心率明细报告
     *
     * @param taskId
     * @return
     */
    @Override
    public List<HeartRateDetailsVo> getHeartRate(Long taskId, String braceletId) {
        if (log.isInfoEnabled()) {
            log.info("TrainingReportServiceImpl.getHeartRate.taskId={}", taskId);
        }
        List<TaskHealthMetricsVo> taskHealthMetricsVos = taskHealthMetricsService.selectTaskHealthMetricsList(taskId, braceletId);
        if (taskHealthMetricsVos.isEmpty()) {
            return new ArrayList<HeartRateDetailsVo>();
        }

        Map<Integer, List<TaskHealthMetricsVo>> collect = taskHealthMetricsVos.stream().collect(Collectors.groupingBy(TaskHealthMetricsVo::getNumber));
        ArrayList<HeartRateDetails> heartRateDetailsList = new ArrayList<>();
//        collect.forEach((number, studentTrainingTaskInfoVos) -> {
//            FullDetailsVo fullDetailsVo = this.calculateSummary(studentTrainingTaskInfoVos);
//            HeartRateDetails heartRateDetails = new HeartRateDetails()
//                .setAverageHeartRate(fullDetailsVo.getAverageHeartRate())
//                .setMinHeartRate(fullDetailsVo.getMinHeartRate())
//                .setMaxHeartRate(fullDetailsVo.getMaxHeartRate())
//                .setNumber(number);
//            heartRateDetailsList.add(heartRateDetails);
//        });

        return List.of();
    }


    /**
     * 查询全部明细报告
     *
     * @param taskId 任务id
     * @return 明细报告列表
     */
    @Override
    public FullDetailsVo getFullDetails(Long taskId) {
        if (log.isInfoEnabled()) {
            log.info("TrainingReportServiceImpl.getFullDetailsVo.taskId={}", taskId);
        }
        List<TaskHealthMetricsVo> taskHealthMetricsVos = taskHealthMetricsService.selectTaskHealthMetricsList(taskId);
        if (taskHealthMetricsVos.isEmpty()) {
            return new FullDetailsVo();
        }
        //训练任务基础信息
        TrainingTaskVo trainingTaskVo = trainingTaskService.selectOne(taskId);
        FullDetailsVo fullDetailsVo = new FullDetailsVo();
        fullDetailsVo.setTrainingName(trainingTaskVo.getTrainingTeamName())
            .setTeacherName(trainingTaskVo.getTeacherName())
            .setTeacherName(trainingTaskVo.getTeacherName())
            .setTrainingType(trainingTaskVo.getExerciseTypeName())
            .setTrainingDate(trainingTaskVo.getCreateTime())
            .setPersonNum(trainingTaskVo.getPersonNum());

        //训练任务学生数据
        Map<String, List<TaskHealthMetricsVo>> fullDetailsMap = taskHealthMetricsVos.stream()
            .collect(Collectors.groupingBy(TaskHealthMetricsVo::getBraceletId));
        ArrayList<FullDetailsInfoVo> fullDetailsList = new ArrayList<>();
        fullDetailsMap.forEach((braceletId, studentTrainingTaskInfoVos) -> {
                FullDetailsInfoVo fullDetailsInfoVo = this.calculateSummary(studentTrainingTaskInfoVos);
                StudentInfoVo studentInfoVo = studentInfoService.selectStudentInfoByBraceletId(braceletId);
                fullDetailsInfoVo.setStudentName(studentInfoVo.getName());
                fullDetailsList.add(fullDetailsInfoVo);
            }
        );
        fullDetailsVo.setFullDetailsReportVoList(fullDetailsList);
        return fullDetailsVo;
    }

    private FullDetailsInfoVo calculateSummary(List<TaskHealthMetricsVo> studentTrainingTaskInfoVoList) {
        FullDetailsInfoVo fullDetailsInfoVo = new FullDetailsInfoVo();
//        double averageBloodOxygen = studentTrainingTaskInfoVoList.stream()
//            .mapToDouble(StudentTrainingTaskInfoVo::getAverageBloodOxygen)
//            .average()
//            .orElse(0);
//        fullDetailsVo.setAverageBloodOxygen(averageBloodOxygen);
        // 平均心率
        double averageHeartRate = studentTrainingTaskInfoVoList.stream()
            .mapToDouble(TaskHealthMetricsVo::getHeartRate)
            .average()
            .orElse(0);
        fullDetailsInfoVo.setAverageHeartRate(averageHeartRate);
//        double averagePace = studentTrainingTaskInfoVoList.stream()
//            .mapToDouble(StudentTrainingTaskInfoVo::getAveragePace)
//            .average()
//            .orElse(0);
//        fullDetailsVo.setAveragePace(averagePace);
//        double maxBloodOxygen = studentTrainingTaskInfoVoList.stream()
//            .mapToDouble(StudentTrainingTaskInfoVo::getMaxBloodOxygen)
//            .max()
//            .orElse(Double.MIN_VALUE);
//        fullDetailsVo.setMaxBloodOxygen(maxBloodOxygen);
        // 最大心率
        double maxHeartRate = studentTrainingTaskInfoVoList.stream()
            .mapToDouble(TaskHealthMetricsVo::getHeartRate)
            .max()
            .orElse(Double.MIN_VALUE);
        fullDetailsInfoVo.setMaxHeartRate(maxHeartRate);
        //最低心率
        double minHeartRate = studentTrainingTaskInfoVoList.stream()
            .mapToDouble(TaskHealthMetricsVo::getHeartRate)
            .min()
            .orElse(Double.MAX_VALUE);
        fullDetailsInfoVo.setMinHeartRate(minHeartRate);
//        double maxPace = studentTrainingTaskInfoVoList.stream()
//            .mapToDouble(StudentTrainingTaskInfoVo::getMaxPace)
//            .max()
//            .orElse(Double.MIN_VALUE);
//        fullDetailsVo.setMaxPace(maxPace);
        return fullDetailsInfoVo;
    }
}
