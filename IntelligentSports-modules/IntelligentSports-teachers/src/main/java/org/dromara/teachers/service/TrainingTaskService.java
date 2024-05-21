package org.dromara.teachers.service;

import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.TrainingTaskBo;
import org.dromara.teachers.domain.bo.DetectionDataBo;
import org.dromara.teachers.domain.vo.FullDetailsVo;
import org.dromara.teachers.domain.vo.StudentTrainingTaskInfoVo;
import org.dromara.teachers.domain.vo.TrainingTaskVo;
import org.dromara.teachers.domain.vo.DetectionDataVo;

import java.util.List;

/**
 * 训练任务表(TeacherTrainingTask)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-15 11:18:55
 */
public interface TrainingTaskService  {

    TrainingTaskVo save(TrainingTaskBo trainingTaskBo);

    int updateById(TrainingTaskBo trainingTaskBo);

    int removeByIds(List<Long> idList);

    TrainingTaskVo selectOne(Long id);

    TableDataInfo<TrainingTaskVo> selectPageTrainingTask(TrainingTaskBo trainingTaskBo, PageQuery pageQuery);

    TrainingTaskVo selectTaskBaseInfoByTaskId(Long taskId);

    DetectionDataVo selectDetectionData(DetectionDataBo detectionDataBo);

    int resetTrainingTask(Long taskId);

    StudentTrainingTaskInfoVo getStudentTrainingTaskInfoByStudentId(Long taskId, String braceletId);

    List<StudentTrainingTaskInfoVo> getLine(Long taskId, String braceletId);

}

