package org.dromara.teachers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.teachers.constants.Constants;
import org.dromara.teachers.domain.bo.DailyBaseDataBo;
import org.dromara.teachers.domain.bo.TrainingTeamStudentBo;
import org.dromara.teachers.domain.entity.TrainingTeamStudent;
import org.dromara.teachers.domain.vo.*;
import org.dromara.teachers.service.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class DailyDataServiceImpl implements DailyDataService {

    private final TrainingTeamService trainingTeamService;

    private final TrainingTeamStudentService trainingTeamStudentService;

    private final StudentInfoService studentInfoService;

    private final HealthMetricsService healthMetricsService;

    private final BraceletStatusService braceletStatusService;



    @Override
    public DailyBaseDataVo getDailyBaseData(DailyBaseDataBo dailyDataBo) {
        if(log.isInfoEnabled()){
            log.info("DailyDataServiceImpl.getDailyBaseData.dailyDataBo={}", dailyDataBo);
        }
        List<TrainingTeamStudentVo> trainingTeamStudentVos = trainingTeamStudentService
            .selectList(new TrainingTeamStudentBo().setTrainingTeamId(dailyDataBo.getTrainingTeamId()));
        List<Long> studentIds = trainingTeamStudentVos.stream().map(TrainingTeamStudentVo::getStudentId).toList();
        List<StudentInfoVo> studentInfoVos = studentInfoService.batchSelectStudentInfoListByStudentIdList(studentIds);
        //训练队学生手环ID
        List<String> braceletsIdList = studentInfoVos.stream().map(StudentInfoVo::getUuid).toList();
        List<BraceletStatusVo> braceletStatusVos = braceletStatusService.selectBraceletList(braceletsIdList);
        //在线手环ID列表
        List<String> IsOnlineBraceletsIdList = braceletStatusVos.stream().filter(
            braceletStatusVo -> Objects.equals(braceletStatusVo.getIsOnline(), Constants.IsOnline)
        ).map(BraceletStatusVo::getUuid).toList();
        //查询时间范围内的数据
        long time = 1714585138;
        List<List<TaskHealthMetricsVo>> lists = healthMetricsService.selectHealthMetricsListByBraceletsIdList(IsOnlineBraceletsIdList, time);
        return null;
    }
}
