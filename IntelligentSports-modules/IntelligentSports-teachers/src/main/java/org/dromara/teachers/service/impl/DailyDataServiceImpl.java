package org.dromara.teachers.service.impl;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.teachers.constants.CacheConstants;
import org.dromara.teachers.constants.Constants;
import org.dromara.teachers.domain.bo.DailyBaseDataBo;
import org.dromara.teachers.domain.bo.TrainingTeamStudentBo;
import org.dromara.teachers.domain.vo.*;
import org.dromara.teachers.service.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    private final DailyHealthMetricsService dailyHealthMetricsService;


    /***
     * 统计每日数据-折线图信息。
     */
    @Override
    public LineDataVo getLineData(DailyBaseDataBo dailyDataBo) {
        //todo 获取实时数据

        //todo 获取当日数据
        // 训练团队成员信息
        List<TrainingTeamStudentVo> trainingTeamStudentVos = this.getTrainingTeamStudentVos(dailyDataBo);
        // 学生人数
        List<Long> stduentsIdList = this.getBraceletsIdList(trainingTeamStudentVos);
        List<StudentInfoVo> studentInfoVos = studentInfoService.batchSelectStudentInfoListByStudentIdList(stduentsIdList);
        List<String> braceletsIdList = studentInfoVos.stream().map(StudentInfoVo::getUuid).toList();
        // 每日学生健康数据
        List<DailyHealthMetricsVo> dailyHealthMetricsVos = dailyHealthMetricsService.selectListByBraceletsIdList(braceletsIdList);
        ArrayList<LineBloodOxygenVo> lineBloodOxygenVoArrayList = new ArrayList<>();
        ArrayList<LineHeartRateVo> lineHeartRateVoArrayList = new ArrayList<>();
        dailyHealthMetricsVos.forEach(
            dailyHealthMetricsVo -> {
                LineBloodOxygenVo lineBloodOxygenVo = new LineBloodOxygenVo().setAvgBloodOxygen(dailyHealthMetricsVo.getAvgBloodOxygen())
                    .setMaxBloodOxygen(dailyHealthMetricsVo.getMaxBloodOxygen())
                    .setMinBloodOxygen(dailyHealthMetricsVo.getMinBloodOxygen());
                //将时间戳转为字符串的格式“yyyy-MM-dd hh:mm:ss”
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(dailyHealthMetricsVo.getStatisticalTime()*1000);
                String formattedDate = formatter.format(date);
                lineBloodOxygenVo.setStatisticalTime(formattedDate);
                lineBloodOxygenVoArrayList.add(lineBloodOxygenVo);
                LineHeartRateVo lineHeartRateVo = new LineHeartRateVo().setAvgHeartRate(dailyHealthMetricsVo.getAvgHeartRate())
                    .setMinHeartRate(dailyHealthMetricsVo.getMinHeartRate())
                    .setMaxHeartRate(dailyHealthMetricsVo.getMaxHeartRate());
                lineHeartRateVo.setStatisticalTime(formattedDate);
                lineHeartRateVoArrayList.add(lineHeartRateVo);
            }
        );
        return new LineDataVo().setBloodOxygenDataToDay(lineBloodOxygenVoArrayList).setHeartRateDataToDay(lineHeartRateVoArrayList);
    }

    /**
     * 根据每日基础数据信息
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
            dailyBaseDataVo.setTrainingTeamId(trainingTeamVo.getId());
            dailyBaseDataVo.setTrainingTeamName(trainingTeamVo.getTeamName());
            // 训练团队成员信息
            List<TrainingTeamStudentVo> trainingTeamStudentVos = this.getTrainingTeamStudentVos(dailyDataBo);
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
            //todo 计算配速
            return dailyBaseDataVo;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process daily base data", e);
        }
    }


    private List<TrainingTeamStudentVo> getTrainingTeamStudentVos(DailyBaseDataBo dailyDataBo) {
        return trainingTeamStudentService
            .selectList(new TrainingTeamStudentBo().setTrainingTeamId(dailyDataBo.getTrainingTeamId()));
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
