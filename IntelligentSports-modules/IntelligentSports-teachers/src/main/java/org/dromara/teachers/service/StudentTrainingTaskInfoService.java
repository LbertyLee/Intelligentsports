package org.dromara.teachers.service;


import org.dromara.teachers.domain.vo.StudentTrainingTaskInfoVo;

import java.util.List;

public interface StudentTrainingTaskInfoService {
    void insert(StudentTrainingTaskInfoVo studentTrainingTaskInfoVo);

    List<StudentTrainingTaskInfoVo> selectList(Long taskId, String braceletId);

    List<StudentTrainingTaskInfoVo> selectListByTaskId(Long taskId);

}
