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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingReportServiceImpl implements TrainingReportService {

    private final StudentTrainingTaskInfoService studentTrainingTaskInfoService;

    private final StudentInfoService studentInfoService;

    private final TrainingTaskService trainingTaskService;

    private final TrainingTeamStudentService trainingTeamStudentService;


    @Override
    public TableDataInfo<TrainingTaskVo> selectPageTrainingReport(TrainingTaskBo trainingTaskBo, PageQuery pageQuery) {
        TableDataInfo<TrainingTaskVo> trainingTaskVoTableDataInfoS = trainingTaskService.selectPageTrainingTask(trainingTaskBo, pageQuery);
        List<TrainingTaskVo> rows = trainingTaskVoTableDataInfoS.getRows();
        // 将查询结果包装成TableDataInfo对象返回
        List<TrainingTaskVo> list = rows.stream().peek(trainingTaskVo -> {
            Long trainingTeamId = trainingTaskVo.getTrainingTeamId();
            List<TrainingTeamStudentVo> trainingTeamStudentVos = trainingTeamStudentService.selectList(new TrainingTeamStudentBo().setTrainingTeamId(trainingTeamId));
            trainingTaskVo.setTrainingPeopleNumber(trainingTeamStudentVos.size());
        }).toList();
        trainingTaskVoTableDataInfoS.setRows(list);
        return trainingTaskVoTableDataInfoS;
    }


    /**
     * 查询全部明细报告
     *
     * @param taskId 任务id
     * @return 明细报告列表
     */
    @Override
    public List<FullDetailsVo> getFullDetails(Long taskId) {
        if (log.isInfoEnabled()) {
            log.info("TrainingReportServiceImpl.getFullDetailsVo.taskId={}", taskId);
        }
        List<StudentTrainingTaskInfoVo> studentTrainingTaskInfoVoList = studentTrainingTaskInfoService.selectListByTaskId(taskId);
        if(studentTrainingTaskInfoVoList.isEmpty()){
            return new ArrayList<FullDetailsVo>();
        }
        Map<String, List<StudentTrainingTaskInfoVo>> collect = studentTrainingTaskInfoVoList.stream()
            .collect(Collectors.groupingBy(StudentTrainingTaskInfoVo::getBraceletId));
        List<FullDetailsVo> fullDetailsList = new ArrayList<>();
        collect.forEach((braceletId, studentTrainingTaskInfoVos) -> {
                FullDetailsVo fullDetailsVo = this.calculateSummary(studentTrainingTaskInfoVos);
                StudentInfoVo studentInfoVo = studentInfoService.selectStudentInfoByBraceletId(braceletId);
                fullDetailsVo.setStudentName(studentInfoVo.getName());
                fullDetailsList.add(fullDetailsVo);
            }
        );
        return fullDetailsList;
    }



    private FullDetailsVo calculateSummary(List<StudentTrainingTaskInfoVo> studentTrainingTaskInfoVoList) {
        FullDetailsVo fullDetailsVo = new FullDetailsVo();
        double averageBloodOxygen = studentTrainingTaskInfoVoList.stream()
            .mapToDouble(StudentTrainingTaskInfoVo::getAverageBloodOxygen)
            .average()
            .orElse(0);
        fullDetailsVo.setAverageBloodOxygen(averageBloodOxygen);
        double averageHeartRate = studentTrainingTaskInfoVoList.stream()
            .mapToDouble(StudentTrainingTaskInfoVo::getAverageHeartRate)
            .average()
            .orElse(0);
        fullDetailsVo.setAverageHeartRate(averageHeartRate);
        double averagePace = studentTrainingTaskInfoVoList.stream()
            .mapToDouble(StudentTrainingTaskInfoVo::getAveragePace)
            .average()
            .orElse(0);
        fullDetailsVo.setAveragePace(averagePace);
        double maxBloodOxygen = studentTrainingTaskInfoVoList.stream()
            .mapToDouble(StudentTrainingTaskInfoVo::getMaxBloodOxygen)
            .max()
            .orElse(Double.MIN_VALUE);
        fullDetailsVo.setMaxBloodOxygen(maxBloodOxygen);
        double maxHeartRate = studentTrainingTaskInfoVoList.stream()
            .mapToDouble(StudentTrainingTaskInfoVo::getMaxHeartRate)
            .max()
            .orElse(Double.MIN_VALUE);
        fullDetailsVo.setMaxHeartRate(maxHeartRate);
        double maxPace = studentTrainingTaskInfoVoList.stream()
            .mapToDouble(StudentTrainingTaskInfoVo::getMaxPace)
            .max()
            .orElse(Double.MIN_VALUE);
        fullDetailsVo.setMaxPace(maxPace);
        return fullDetailsVo;
    }
}
