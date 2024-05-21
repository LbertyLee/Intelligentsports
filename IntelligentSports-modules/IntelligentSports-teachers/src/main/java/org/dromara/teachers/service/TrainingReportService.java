package org.dromara.teachers.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.TrainingTaskBo;
import org.dromara.teachers.domain.vo.FullDetailsVo;
import org.dromara.teachers.domain.vo.TrainingTaskVo;

import java.util.List;

public interface TrainingReportService {

    List<FullDetailsVo> getFullDetails(Long taskId);

    TableDataInfo<TrainingTaskVo> selectPageTrainingReport(TrainingTaskBo trainingTaskBo, PageQuery pageQuery);
}
