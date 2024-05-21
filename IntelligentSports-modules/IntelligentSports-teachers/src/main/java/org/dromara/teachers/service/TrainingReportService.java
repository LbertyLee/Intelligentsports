package org.dromara.teachers.service;

import org.dromara.teachers.domain.vo.FullDetailsVo;

import java.util.List;

public interface TrainingReportService {
    List<FullDetailsVo> getFullDetails(Long taskId);
}
