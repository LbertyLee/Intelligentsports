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
        List<TrainingTeamStudentVo> trainingTeamStudentVos = this.getTrainingTeamStudentVos(dailyDataBo);
        List<Long> studentsIdList = this.getBraceletsIdList(trainingTeamStudentVos);
        List<StudentInfoVo> studentInfoVos = studentInfoService.batchSelectStudentInfoListByStudentIdList(studentsIdList);
        List<String> braceletsIdList = studentInfoVos.stream().map(StudentInfoVo::getUuid).collect(Collectors.toList());
        long startTime = this.calculateStartTime(dailyDataBo);
        long endTime = this.calculateEndTime(dailyDataBo);
        List<String> isOnlineBraceletsIdList = this.getIsOnlineBraceletsIdList(braceletsIdList);
        List<HealthMetricsVo> healthMetricsVos = healthMetricsService.selectDataWithinTimeRange(startTime, endTime, isOnlineBraceletsIdList);
        List<HealthMetricsVo> latestMetrics = healthMetricsVos.stream()
            .sorted(Comparator.comparingLong(HealthMetricsVo::getTimestamp).reversed())
            .toList();
        long latestTimestamp = latestMetrics.stream()
            .mapToLong(HealthMetricsVo::getTimestamp)
            .max()
            .orElse(0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<LineBloodOxygenRealTimeVo> lineBloodOxygenRealTimeVos = new ArrayList<>();
        List<LineHeartRateRealTimeVo> lineHeartRateRealTimeVos = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            long start = latestTimestamp - (i + 1) * 10;
            long end = latestTimestamp - i * 10;
            long middleTime = (start + end) / 2;
            List<HealthMetricsVo> segment = latestMetrics.stream()
                .filter(vo -> vo.getTimestamp() >= start && vo.getTimestamp() < end)
                .collect(Collectors.toList());
            if (!segment.isEmpty()) {
                Map<String, Object> lineChartData = this.createLineChartData(segment, middleTime, formatter);
                lineChartData.forEach(
                    (key, value) -> {
                        if (key.equals("lineBloodOxygenRealTimeVo")) {
                            lineBloodOxygenRealTimeVos.add((LineBloodOxygenRealTimeVo) value);
                        }
                        if (key.equals("lineHeartRateRealTimeVo")) {
                            lineHeartRateRealTimeVos.add((LineHeartRateRealTimeVo) value);
                        }
                    }
                );
            }
        }
        List<DailyHealthMetricsVo> dailyHealthMetricsVos = dailyHealthMetricsService.selectListByBraceletsIdList(braceletsIdList);

        List<LineBloodOxygenToDayVo> lineBloodOxygenVoArrayList = dailyHealthMetricsVos.stream()
            .map(vo -> this.createLineBloodOxygenToDayVo(vo, formatter))
            .collect(Collectors.toList());
        List<LineHeartRateToDayVo> lineHeartRateVoArrayList = dailyHealthMetricsVos.stream()
            .map(vo -> this.createLineHeartRateToDayVo(vo, formatter))
            .collect(Collectors.toList());
        return new LineDataVo()
            .setBloodOxygenDataToDay(lineBloodOxygenVoArrayList)
            .setHeartRateDataToDay(lineHeartRateVoArrayList)
            .setBloodOxygenDataToRealTime(lineBloodOxygenRealTimeVos)
            .setHeartRateDataToRealTime(lineHeartRateRealTimeVos);
    }

    private Map<String, Object> createLineChartData(List<HealthMetricsVo> segment, long middleTime, SimpleDateFormat formatter) {
        Map<String, Object> data = new HashMap<>();
        String formattedDate = formatter.format(new Date(middleTime * 1000));
        double avgBloodOxygen = segment.stream().mapToDouble(HealthMetricsVo::getBloodOxygen).average().orElse(0);
        double maxBloodOxygen = segment.stream().mapToDouble(HealthMetricsVo::getBloodOxygen).max().orElse(0);
        double minBloodOxygen = segment.stream().mapToDouble(HealthMetricsVo::getBloodOxygen).min().orElse(0);
        double avgHeartRate = segment.stream().mapToDouble(HealthMetricsVo::getHeartRate).average().orElse(0);
        double maxHeartRate = segment.stream().mapToDouble(HealthMetricsVo::getHeartRate).max().orElse(0);
        double minHeartRate = segment.stream().mapToDouble(HealthMetricsVo::getHeartRate).min().orElse(0);
        LineBloodOxygenRealTimeVo lineBloodOxygenRealTimeVo = new LineBloodOxygenRealTimeVo()
            .setAvgBloodOxygen(avgBloodOxygen)
            .setMaxBloodOxygen(maxBloodOxygen)
            .setMinBloodOxygen(minBloodOxygen)
            .setStatisticalTime(formattedDate);
        LineHeartRateRealTimeVo lineHeartRateRealTimeVo = new LineHeartRateRealTimeVo()
            .setAvgHeartRate(avgHeartRate)
            .setMaxHeartRate(maxHeartRate)
            .setMinHeartRate(minHeartRate)
            .setStatisticalTime(formattedDate);
        data.put("lineBloodOxygenRealTimeVo", lineBloodOxygenRealTimeVo);
        data.put("lineHeartRateRealTimeVo", lineHeartRateRealTimeVo);
        return data;
    }

    /**
     * 根据每日健康指标数据创建血氧日至视图对象
     *
     * @param vo        每日健康指标数据，包含统计时间、平均血氧、最大血氧和最小血氧
     * @param formatter 日期格式化工具，用于将统计时间转换为指定格式的字符串
     * @return 血氧日至视图对象，包含平均血氧、最大血氧、最小血氧和统计时间（格式化后）
     */
    private LineBloodOxygenToDayVo createLineBloodOxygenToDayVo(DailyHealthMetricsVo vo, SimpleDateFormat formatter) {
        String formattedDate = formatter.format(new Date(vo.getStatisticalTime() * 1000));
        return new LineBloodOxygenToDayVo()
            .setAvgBloodOxygen(vo.getAvgBloodOxygen())
            .setMaxBloodOxygen(vo.getMaxBloodOxygen())
            .setMinBloodOxygen(vo.getMinBloodOxygen())
            .setStatisticalTime(formattedDate);
    }

    /**
     * 根据每日健康指标数据创建线图心率日至视图对象。
     *
     * @param vo        每日健康指标数据，包含统计时间、平均心率、最大心率和最小心率。
     * @param formatter 日期格式化工具，用于将统计时间转换为指定格式的字符串。
     * @return 线图心率日至视图对象，包含平均心率、最大心率、最小心率和统计时间（格式化后）。
     */
    private LineHeartRateToDayVo createLineHeartRateToDayVo(DailyHealthMetricsVo vo, SimpleDateFormat formatter) {
        String formattedDate = formatter.format(new Date(vo.getStatisticalTime() * 1000));
        return new LineHeartRateToDayVo()
            .setAvgHeartRate(vo.getAvgHeartRate())
            .setMaxHeartRate(vo.getMaxHeartRate())
            .setMinHeartRate(vo.getMinHeartRate())
            .setStatisticalTime(formattedDate);
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
            List<HealthMetricsVo> healthMetricsVos = healthMetricsService.selectDataWithinTimeRange(startTime, endTime, isOnlineBraceletsIdList);
            // 计算平均心率、平均血氧、最大心率、最大血氧、最小心率、最小血氧
            this.populateMetrics(dailyBaseDataVo, healthMetricsVos);
            //todo 计算配速
            return dailyBaseDataVo;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process daily base data", e);
        }
    }


    /**
     * 获取指定训练团队的学生信息列表
     *
     * @param dailyDataBo 包含每日基础数据信息的对象，其中需要指定训练团队的ID
     * @return 返回一个训练团队学生信息的列表
     */
    private List<TrainingTeamStudentVo> getTrainingTeamStudentVos(DailyBaseDataBo dailyDataBo) {
        // 根据训练团队ID查询学生信息列表
        return trainingTeamStudentService.selectList(new TrainingTeamStudentBo().setTrainingTeamId(dailyDataBo.getTrainingTeamId()));
    }

    /**
     * 计算学生数量
     *
     * @param trainingTeamStudentVos 训练团队学生信息的列表
     * @return 返回学生数量
     */
    private int calculateStudentCount(List<TrainingTeamStudentVo> trainingTeamStudentVos) {
        // 返回学生信息列表的大小，即学生数量
        return trainingTeamStudentVos.size();
    }

    /**
     * 获取训练团队学生列表中学生的手环ID列表。
     *
     * @param trainingTeamStudentVos 训练团队学生视图对象列表，每个对象包含学生相关信息，如学生ID。
     * @return 返回一个包含学生手环ID的列表。
     */
    private List<Long> getBraceletsIdList(List<TrainingTeamStudentVo> trainingTeamStudentVos) {
        return trainingTeamStudentVos.stream().map(TrainingTeamStudentVo::getStudentId).collect(Collectors.toList());
    }

    /**
     * 获取当前在线手环的ID列表。
     *
     * @param braceletsIdList 手环ID列表，是一个字符串列表，每个字符串代表一个手环的ID。
     * @return 返回一个在线手环ID的列表。列表中的每个元素都是字符串，代表一个在线手环的唯一标识符。
     */
    private List<String> getIsOnlineBraceletsIdList(List<String> braceletsIdList) {
        return braceletStatusService.selectBraceletList(braceletsIdList).stream().filter(braceletStatusVo -> Objects.equals(braceletStatusVo.getIsOnline(), Constants.IsOnline)).map(BraceletStatusVo::getUuid).collect(Collectors.toList());
    }

    /**
     * 计算开始时间。该方法首先检查传入的DailyBaseDataBo对象是否有设置开始时间，
     * 如果有，则直接返回该开始时间。如果没有设置开始时间，则返回当前日期的开始时间（凌晨）。
     *
     * @param dailyDataBo DailyBaseDataBo对象，包含待检查的开始时间。
     * @return 长整型表示的开始时间，单位为秒。
     */
    private long calculateStartTime(DailyBaseDataBo dailyDataBo) {
        if (ObjectUtil.isNotNull(dailyDataBo.getStartTime())) {
            return dailyDataBo.getStartTime();
        } else {
            LocalDate today = LocalDate.now();
            return today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        }
    }

    /**
     * 计算结束时间。该方法首先检查传入的DailyBaseDataBo对象的endTime是否为空，
     * 如果不为空，则直接返回该时间值。如果为空，则计算当前日期的23:59:59作为结束时间，
     * 并返回该时间的毫秒数除以1000后的值。
     *
     * @param dailyDataBo DailyBaseDataBo对象，包含待检查的结束时间。
     * @return 结束时间的秒数表示。如果endTime不为空，则返回该值；如果为空，返回当天的23:59:59的秒数表示。
     */
    private long calculateEndTime(DailyBaseDataBo dailyDataBo) {
        // 检查传入对象的endTime是否为空，不为空则直接返回endTime的值
        if (ObjectUtil.isNotNull(dailyDataBo.getEndTime())) {
            return dailyDataBo.getEndTime();
        } else {
            // 如果endTime为空，计算当前日期的23:59:59并返回其秒数表示
            LocalDate today = LocalDate.now();
            return today.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        }
    }

    /**
     * 填充每日基础数据视图对象的度量指标。
     *
     * @param dailyBaseDataVo  表示每日基础数据的视图对象，此对象将被填充平均心率、平均血氧饱和度、最大心率、最大血氧饱和度、最小心率和最小血氧饱和度等指标。
     * @param healthMetricsVos 一个包含健康指标数据的视图对象列表，这些数据将被用来计算平均值、最大值和最小值。
     */
    private void populateMetrics(DailyBaseDataVo dailyBaseDataVo, List<HealthMetricsVo> healthMetricsVos) {
        OptionalDouble avgHeartRate = healthMetricsVos.stream().mapToDouble(HealthMetricsVo::getHeartRate).average();
        OptionalDouble avgBloodOxygen = healthMetricsVos.stream().mapToDouble(HealthMetricsVo::getBloodOxygen).average();
        dailyBaseDataVo.setAverageHeartRate((int) avgHeartRate.orElse(0));
        dailyBaseDataVo.setAverageBloodOxygen((int) avgBloodOxygen.orElse(0));
        OptionalDouble maxHeartRate = healthMetricsVos.stream().mapToDouble(HealthMetricsVo::getHeartRate).max();
        OptionalDouble maxBloodOxygen = healthMetricsVos.stream().mapToDouble(HealthMetricsVo::getBloodOxygen).max();
        dailyBaseDataVo.setMaxHeartRate((int) maxHeartRate.orElse(0));
        dailyBaseDataVo.setMaxBloodOxygen((int) maxBloodOxygen.orElse(0));
        OptionalDouble minHeartRate = healthMetricsVos.stream().mapToDouble(HealthMetricsVo::getHeartRate).min();
        OptionalDouble minBloodOxygen = healthMetricsVos.stream().mapToDouble(HealthMetricsVo::getBloodOxygen).min();
        dailyBaseDataVo.setMinHeartRate((int) minHeartRate.orElse(0));
        dailyBaseDataVo.setMinBloodOxygen((int) minBloodOxygen.orElse(0));
    }
}
