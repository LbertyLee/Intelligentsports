package org.dromara.teachers.service.impl;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.teachers.constants.Constants;
import org.dromara.teachers.domain.bo.DailyBaseDataBo;
import org.dromara.teachers.domain.bo.TrainingTeamStudentBo;
import org.dromara.teachers.domain.vo.*;
import org.dromara.teachers.service.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class DailyDataServiceImpl implements DailyDataService {

    private final TrainingTeamStudentService trainingTeamStudentService;

    private final StudentInfoService studentInfoService;

    private final HealthMetricsService healthMetricsService;

    private final BraceletStatusService braceletStatusService;

    private final TrainingTeamService trainingTeamService;


    /**
     * 根据每日基础数据信息，获取每日基础数据的视图对象。
     *
     * @param dailyDataBo 每日基础数据业务对象，包含训练团队ID等信息。
     * @return DailyBaseDataVo 每日基础数据视图对象，包含学生人数、在线手环数量、以及健康指标数据。
     */
    @Override
    public DailyBaseDataVo getDailyBaseData(DailyBaseDataBo dailyDataBo) {
        try {
            if (log.isInfoEnabled()) {
                log.info("DailyDataServiceImpl.getDailyBaseData.dailyDataBo={}", dailyDataBo);
            }
            DailyBaseDataVo dailyBaseDataVo = new DailyBaseDataVo();
            TrainingTeamVo trainingTeamVo = trainingTeamService.selectTeacherTrainingTeamById(dailyDataBo.getTrainingTeamId());
            dailyBaseDataVo.setTrainingTeamId(dailyDataBo.getTrainingTeamId());
            dailyBaseDataVo.setTrainingTeamName(trainingTeamVo.getTeamName());
            // 训练团队信息
            List<TrainingTeamStudentVo> trainingTeamStudentVos = trainingTeamStudentService
                .selectList(new TrainingTeamStudentBo().setTrainingTeamId(dailyDataBo.getTrainingTeamId()));
            // 学生人数
            List<Long> stduentsIdList = this.getBraceletsIdList(trainingTeamStudentVos);
            dailyBaseDataVo.setTrainingPeopleNumber(this.calculateStudentCount(trainingTeamStudentVos));
            List<StudentInfoVo> studentInfoVos = studentInfoService.batchSelectStudentInfoListByStudentIdList(stduentsIdList);
            List<String> braceletsIdList = studentInfoVos.stream().map(StudentInfoVo::getUuid).toList();
            // 在线手环ID列表
            List<String> isOnlineBraceletsIdList = this.getIsOnlineBraceletsIdList(braceletsIdList);
            dailyBaseDataVo.setBraceletOnlineNumber(stduentsIdList.size());
            long startTime = this.calculateStartTime(dailyDataBo);
            long endTime = this.calculateEndTime(dailyDataBo);
            // 查询时间范围内的数据
            List<HealthMetricsVo> healthMetricsVos = healthMetricsService
                .selectDataWithinTimeRange(startTime, endTime, isOnlineBraceletsIdList);
            // 计算平均心率、平均血氧、最大心率、最大血氧、最小心率、最小血氧
            this.populateMetrics(dailyBaseDataVo, healthMetricsVos);

            //计算配速

            return dailyBaseDataVo;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process daily base data", e);
        }
    }

    private int calculateStudentCount(List<TrainingTeamStudentVo> trainingTeamStudentVos) {
        return trainingTeamStudentVos.size();
    }

    private List<Long> getBraceletsIdList(List<TrainingTeamStudentVo> trainingTeamStudentVos) {
        return trainingTeamStudentVos.stream()
            .map(TrainingTeamStudentVo::getStudentId)
            .collect(Collectors.toList());
    }

    private List<String> getIsOnlineBraceletsIdList(List<String> braceletsIdList) {
        return braceletStatusService.selectBraceletList(braceletsIdList).stream()
            .filter(braceletStatusVo -> Objects.equals(braceletStatusVo.getIsOnline(), Constants.IsOnline))
            .map(BraceletStatusVo::getUuid)
            .collect(Collectors.toList());
    }

    private long calculateStartTime(DailyBaseDataBo dailyDataBo) {
        if (ObjectUtil.isNotNull(dailyDataBo.getStartTime())) {
            return dailyDataBo.getStartTime();
        } else {
            LocalDate today = LocalDate.now();
            return today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        }
    }

    private long calculateEndTime(DailyBaseDataBo dailyDataBo) {
        if (ObjectUtil.isNotNull(dailyDataBo.getEndTime())) {
            return dailyDataBo.getEndTime();
        } else {
            LocalDate today = LocalDate.now();
            return today.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        }
    }

    private void populateMetrics(DailyBaseDataVo dailyBaseDataVo, List<HealthMetricsVo> healthMetricsVos) {
        OptionalDouble avgHeartRate = healthMetricsVos.stream()
            .mapToDouble(HealthMetricsVo::getHeartRate)
            .average();
        OptionalDouble avgBloodOxygen = healthMetricsVos.stream()
            .mapToDouble(HealthMetricsVo::getBloodOxygen)
            .average();
        dailyBaseDataVo.setAverageHeartRate((int) avgHeartRate.orElse(0));
        dailyBaseDataVo.setAverageBloodOxygen((int) avgBloodOxygen.orElse(0));
        OptionalDouble maxHeartRate = healthMetricsVos.stream()
            .mapToDouble(HealthMetricsVo::getHeartRate)
            .max();
        OptionalDouble maxBloodOxygen = healthMetricsVos.stream()
            .mapToDouble(HealthMetricsVo::getBloodOxygen)
            .max();
        dailyBaseDataVo.setMaxHeartRate((int) maxHeartRate.orElse(0));
        dailyBaseDataVo.setMaxBloodOxygen((int) maxBloodOxygen.orElse(0));
        OptionalDouble minHeartRate = healthMetricsVos.stream()
            .mapToDouble(HealthMetricsVo::getHeartRate)
            .min();
        OptionalDouble minBloodOxygen = healthMetricsVos.stream()
            .mapToDouble(HealthMetricsVo::getBloodOxygen)
            .min();
        dailyBaseDataVo.setMinHeartRate((int) minHeartRate.orElse(0));
        dailyBaseDataVo.setMinBloodOxygen((int) minBloodOxygen.orElse(0));
    }
}
