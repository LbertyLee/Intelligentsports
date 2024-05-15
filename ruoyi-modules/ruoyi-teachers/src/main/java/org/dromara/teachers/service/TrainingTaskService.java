package org.dromara.teachers.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.TrainingTaskBo;
import org.dromara.teachers.domain.entity.TrainingTask;
import org.dromara.teachers.domain.vo.TrainingTaskVo;

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

}

