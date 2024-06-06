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

    /**
     * 插入学生训练任务信息。
     *
     * 本方法用于将学生训练任务信息对象插入到数据库中。首先，它通过BeanUtil的copyProperties方法将StudentTrainingTaskInfoVo对象复制到StudentTrainingTaskInfo对象中，
     * 然后调用studentTrainingTaskInfoMapper的insert方法将复制后的对象插入数据库。
     * 这种设计模式遵循了领域驱动设计（DDD）中的仓库模式，将数据访问逻辑封装在映射器中，保持业务逻辑的整洁和独立。
     *
     * @param studentTrainingTaskInfoVo 学生训练任务信息的视图对象，包含了需要插入数据库的所有信息。
     */
    @Override
    public void insert(StudentTrainingTaskInfoVo studentTrainingTaskInfoVo) {
        // 如果日志级别为DEBUG，则输出学生训练任务信息对象的详细信息，用于调试和日志记录
        if (log.isInfoEnabled()){
            log.info("StudentTrainingTaskInfoServiceImpl.insert.studentTrainingTaskInfoVo:{}",studentTrainingTaskInfoVo);
        }
        // 将学生训练任务信息的视图对象复制到领域对象中，以准备插入数据库
        StudentTrainingTaskInfo studentTrainingTaskInfo = BeanUtil.copyProperties(studentTrainingTaskInfoVo, StudentTrainingTaskInfo.class);
        // 调用映射器的insert方法，将学生训练任务信息插入数据库
        studentTrainingTaskInfoMapper.insert(studentTrainingTaskInfo);
    }

    /**
     * 根据任务ID和手环ID查询学生训练任务信息列表。
     *
     * @param taskId 任务ID，用于筛选特定任务下的学生训练任务信息。
     * @param braceletId 手环ID，用于关联特定手环的学生训练任务信息。
     * @return 返回匹配条件的学生训练任务信息列表，列表中包含每个学生的训练任务详情。
     */
    @Override
    public List<StudentTrainingTaskInfoVo> selectList(Long taskId, String braceletId) {
        // 创建LambdaQueryWrapper实例，用于构建查询条件
        LambdaQueryWrapper<StudentTrainingTaskInfo> eq = new LambdaQueryWrapper<StudentTrainingTaskInfo>()
            // 筛选任务ID与参数taskId相同的记录
            .eq(StudentTrainingTaskInfo::getTaskId, taskId)
            // 筛选手环ID与参数braceletId相同的记录
            .eq(StudentTrainingTaskInfo::getBraceletId, braceletId);

        // 调用studentTrainingTaskInfoMapper的selectVoList方法，根据构建的查询条件查询学生训练任务信息列表
        // 这里的selectVoList方法是通过Mapper接口的实现类将查询结果转换为StudentTrainingTaskInfoVo对象的列表，以便于业务处理和展示
        return studentTrainingTaskInfoMapper.selectVoList(eq);
    }

    /**
     * 根据任务ID查询学生训练任务信息列表。
     *
     * 本方法通过调用学生训练任务信息Mapper，使用LambdaQueryWrapper构造查询条件，查询与指定任务ID相关的学生训练任务信息。
     * 主要用于在系统中获取特定任务下的学生训练任务详情，以便进行进一步的操作或展示。
     *
     * @param taskId 任务ID，用于查询特定任务下的学生训练任务信息。
     * @return 返回匹配任务ID的学生训练任务信息列表。
     */
    @Override
    public List<StudentTrainingTaskInfoVo> selectListByTaskId(Long taskId) {
        // 如果日志级别允许，记录查询任务ID的信息
        if(log.isInfoEnabled()){
            log.info("StudentTrainingTaskInfoServiceImpl.selectListByTaskId.taskId:{}",taskId);
        }
        // 使用Lambda表达式构建查询条件，查询指定任务ID的学生训练任务信息
        LambdaQueryWrapper<StudentTrainingTaskInfo> eq = new LambdaQueryWrapper<StudentTrainingTaskInfo>()
            .eq(StudentTrainingTaskInfo::getTaskId, taskId);
        // 调用Mapper方法，根据查询条件获取学生训练任务信息的Vo列表
        return studentTrainingTaskInfoMapper.selectVoList(eq);
    }
}
