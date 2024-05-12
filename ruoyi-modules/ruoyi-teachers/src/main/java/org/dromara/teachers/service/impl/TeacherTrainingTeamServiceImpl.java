package org.dromara.teachers.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.TeacherTrainingTeamBo;
import org.dromara.teachers.domain.bo.TeacherTrainingTeamStudentBo;
import org.dromara.teachers.domain.entity.StudentInfo;
import org.dromara.teachers.domain.entity.TeacherTrainingTeamStudent;
import org.dromara.teachers.domain.vo.StudentInfoVo;
import org.dromara.teachers.domain.vo.TeacherTrainingTeamVo;
import org.dromara.teachers.mapper.TeacherTrainingTeamMapper;
import org.dromara.teachers.domain.entity.TeacherTrainingTeam;
import org.dromara.teachers.service.TeacherTrainingTeamService;
import org.dromara.teachers.service.TeacherTrainingTeamStudentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 训练队(TeacherTrainingTeam)表服务实现类
 *
 * @author LbertyLee
 * @since 2024-05-11 23:55:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherTrainingTeamServiceImpl  implements TeacherTrainingTeamService {

    private final TeacherTrainingTeamMapper teacherTrainingTeamMapper;

    private final TeacherTrainingTeamStudentService teacherTrainingTeamStudentService;

    /**
     * 添加训练队信息。
     *
     * @param teacherTrainingTeamBo 添加训练队信息，类型为TeacherTrainingTeamBo
     * @return 返回添加成功的记录数
     */
    @Override
    public int addTrainingTeam(TeacherTrainingTeamBo teacherTrainingTeamBo) {
        // 将TeacherTrainingTeamBo对象复制到TeacherTrainingTeam对象
        TeacherTrainingTeam teacherTrainingTeam = new TeacherTrainingTeam().setTeamName(teacherTrainingTeamBo.getTeamName());
        if (log.isInfoEnabled()){
            log.info("TeacherTrainingTeamServiceImpl.addTrainingTeam.teacherTrainingTeam：{}", teacherTrainingTeam);
        }
        // 将训练队信息信息插入数据库，并返回插入的记录数
        return teacherTrainingTeamMapper.insert(teacherTrainingTeam);
    }

    /**
     * 更新训练团队信息。
     *
     * @param teacherTrainingTeamBo 训练团队的业务对象，包含需要更新的信息。
     * @return 返回更新的记录数。
     */
    @Override
    public int updateTrainingTeam(TeacherTrainingTeamBo teacherTrainingTeamBo) {
        // 复制TeacherTrainingTeamBo对象到TeacherTrainingTeam对象，以进行更新
        TeacherTrainingTeam teacherTrainingTeam = BeanUtil.copyProperties(teacherTrainingTeamBo, TeacherTrainingTeam.class);
        if (log.isInfoEnabled()){
            // 如果日志级别允许，记录更新前的训练团队信息
            log.info("TeacherTrainingTeamServiceImpl.updateTrainingTeam.teacherTrainingTeam：{}", teacherTrainingTeam);
        }
        // 调用mapper层，根据更新后的对象更新数据库中的记录，并返回更新的记录数
        return teacherTrainingTeamMapper.updateById(teacherTrainingTeam);
    }

    /**
     * 删除训练团队方法
     *
     * @param id 训练团队的唯一标识符
     * @return 删除操作影响的行数
     */
    @Override
    public int deleteTrainingTeam(Long id) {
        // 检查日志级别是否允许记录信息
        if (log.isInfoEnabled()){
            // 记录删除操作前的训练团队ID，用于日志记录
            log.info("TeacherTrainingTeamServiceImpl.deleteTrainingTeam.id：{}", id);
        }
        if(!teacherTrainingTeamStudentService.checkIsBindStudent(id)){
            throw new ServiceException("该训练队有学员，不能删除");
        }
        return teacherTrainingTeamMapper.deleteById(id);
    }

    /**
     * 分页查询教师培训团队列表信息
     *
     * @param teacherTrainingTeamBo 教师培训团队查询条件对象，包含查询时所需的各种条件
     * @param pageQuery 分页查询参数对象，包含页码和每页显示数量等信息
     * @return TableDataInfo<TeacherTrainingTeamVo> 分页查询结果对象，包含查询到的数据列表、总记录数、总页数等信息
     */
    @Override
    public TableDataInfo<TeacherTrainingTeamVo> selectPageTeacherTrainingTeamList(TeacherTrainingTeamBo teacherTrainingTeamBo,
                                                                                  PageQuery pageQuery) {
        // 调用TeacherTrainingTeamMapper的selectPageTeacherTrainingTeamList方法进行分页查询
        Page<TeacherTrainingTeamVo> page=teacherTrainingTeamMapper.selectPageTeacherTrainingTeamList(
           pageQuery.build(), this.buildQueryWrapper(teacherTrainingTeamBo)
            );
        // 将查询结果封装成TableDataInfo对象并返回
        return TableDataInfo.build(page);
    }

    /**
     * 绑定学生到教师培训团队
     *
     * @param teacherTrainingTeamBindStudentBo 包含培训团队ID和学生ID的绑定信息对象
     * @return 返回批量插入学生的结果，即影响的行数
     * @throws ServiceException 如果没有选择学生，则抛出异常
     */
    @Override
    public boolean bindStudent(TeacherTrainingTeamStudentBo teacherTrainingTeamBindStudentBo) {
        // 获取传入的绑定信息中的培训团队ID和学生ID列表
        Long trainingTeamId = teacherTrainingTeamBindStudentBo.getTrainingTeamId();
        ArrayList<Long> studentId = teacherTrainingTeamBindStudentBo.getStudentId();
        // 检查学生ID列表是否为空，为空则抛出异常
        if(ObjectUtil.isEmpty(studentId)){
            throw new ServiceException("请选择学生");
        }

        // 将学生ID列表转换为教师培训团队学生对象列表，为每个学生对象设置培训团队ID和学生ID
        List<TeacherTrainingTeamStudent> teacherTrainingTeamStudentList = studentId.stream().map(id -> {
            return new TeacherTrainingTeamStudent().setStudentId(id).setTrainingTeamId(trainingTeamId);
        }).toList();

        // 调用服务层方法，批量插入教师培训团队学生信息
        return  teacherTrainingTeamStudentService.insertBatch(teacherTrainingTeamStudentList);
    }

    /**
     * 构建查询Wrapper用于查询教师培训团队信息。
     *
     * @param teacherTrainingTeamBo 教师培训团队业务对象，包含查询所需的条件。
     * @return Wrapper<TeacherTrainingTeam> 包装了查询条件的Wrapper对象，用于动态生成SQL查询。
     */
    private Wrapper<TeacherTrainingTeam> buildQueryWrapper(TeacherTrainingTeamBo teacherTrainingTeamBo) {
        // 创建一个空的查询Wrapper
        QueryWrapper<TeacherTrainingTeam> wrapper = Wrappers.query();
        // 如果团队名称不为空，则在查询中添加like条件
        return wrapper.like(ObjectUtil.isNotNull(teacherTrainingTeamBo.getTeamName()),
            "team_name", teacherTrainingTeamBo.getTeamName());
    }

}

