package org.dromara.teachers.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.teachers.domain.entity.StudentTrainingTaskInfo;
import org.dromara.teachers.domain.vo.StudentTrainingTaskInfoVo;
import org.dromara.teachers.mapper.StudentTrainingTaskInfoMapper;
import org.dromara.teachers.service.StudentTrainingTaskInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentTrainingTaskInfoServiceImpl implements StudentTrainingTaskInfoService {

    private final StudentTrainingTaskInfoMapper studentTrainingTaskInfoMapper;

    @Override
    public void insert(StudentTrainingTaskInfoVo studentTrainingTaskInfoVo) {
        if (log.isDebugEnabled()){
            log.info("StudentTrainingTaskInfoServiceImpl.insert.studentTrainingTaskInfoVo:{}",studentTrainingTaskInfoVo);
        }
        StudentTrainingTaskInfo studentTrainingTaskInfo = BeanUtil.copyProperties(studentTrainingTaskInfoVo, StudentTrainingTaskInfo.class);
        studentTrainingTaskInfoMapper.insert(studentTrainingTaskInfo);
    }

    @Override
    public List<StudentTrainingTaskInfoVo> selectList(Long taskId, String braceletId) {
        LambdaQueryWrapper<StudentTrainingTaskInfo> eq = new LambdaQueryWrapper<StudentTrainingTaskInfo>()
            .eq(StudentTrainingTaskInfo::getTaskId, taskId)
            .eq(StudentTrainingTaskInfo::getBraceletId, braceletId);
        return studentTrainingTaskInfoMapper.selectVoList(eq);
    }

    @Override
    public List<StudentTrainingTaskInfoVo> selectListByTaskId(Long taskId) {
        if(log.isInfoEnabled()){
            log.info("StudentTrainingTaskInfoServiceImpl.selectListByTaskId.taskId:{}",taskId);
        }
        LambdaQueryWrapper<StudentTrainingTaskInfo> eq = new LambdaQueryWrapper<StudentTrainingTaskInfo>()
            .eq(StudentTrainingTaskInfo::getTaskId, taskId);
        return studentTrainingTaskInfoMapper.selectVoList(eq);
    }
}
