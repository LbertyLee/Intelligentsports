package org.dromara.teachers.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.BindDraceletBo;
import org.dromara.teachers.domain.bo.StudentInfoBo;
import org.dromara.teachers.domain.entity.StudentInfo;
import org.dromara.teachers.domain.vo.StudentInfoVo;
import org.dromara.teachers.mapper.StudentInfoMapper;
import org.dromara.teachers.service.StudentInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * (StudentInfo)表服务实现类
 *
 * @author LbertyLee
 * @since 2024-05-11 15:32:01
 */
@Slf4j
@Service("studentInfoService")
@RequiredArgsConstructor
public class StudentInfoServiceImpl implements StudentInfoService {

    private final StudentInfoMapper studentInfoMapper;

    /**
     * 绑定学生和手环信息
     *
     * @param bindBracelet 包含学生ID和手环ID的绑定信息对象
     * @return 返回更新记录的数量，即影响的行数
     */
    @Override
    public int bindbracelet(BindDraceletBo bindBracelet) {
        if (log.isInfoEnabled()) {
            log.info("StudentInfoServiceImpl.bindbDracelet.bindDraceletBo{}", bindBracelet);
        }
        // 创建学生信息对象，并设置学生ID和手环ID
        StudentInfo studentInfo = new StudentInfo()
            .setId(bindBracelet.getStudentId())
            .setUuid(bindBracelet.getBraceletId());
        // 根据学生信息更新数据库中的记录，并返回更新的行数
        return studentInfoMapper.updateById(studentInfo);
    }

    /**
     * 分页查询学生信息列表
     *
     * @param studentInfoBo 学生信息查询条件
     * @param pageQuery     分页查询参数
     * @return TableDataInfo<StudentInfoVo> 包含分页学生信息的表格数据
     */
    @Override
    public TableDataInfo<StudentInfoVo> selectPageStudentInfoList(StudentInfoBo studentInfoBo, PageQuery pageQuery) {
        if (log.isInfoEnabled()) {
            log.info("StudentInfoServiceImpl.selectPageStudentInfoList.studentInfoBo{}", studentInfoBo);
            log.info("StudentInfoServiceImpl.selectPageStudentInfoList.pageQuery{}", pageQuery);
        }
        // 根据查询条件和分页参数，执行数据库查询
        Page<StudentInfoVo> page = studentInfoMapper.selectPageStudentInfoList(pageQuery.build(),
            this.buildQueryWrapper(studentInfoBo));
        // 将查询结果封装成TableDataInfo对象返回
        return TableDataInfo.build(page);
    }

    /**
     * 根据学生编号查询学生信息。
     *
     * @param studentNumber 学生编号，用于查询特定学生的信息。
     * @return 返回匹配的学生信息对象，如果没有找到，则返回null。
     */
    @Override
    public StudentInfo selectStudentInfoByStudentNumber(String studentNumber) {
        if (log.isInfoEnabled()) {
            log.info("StudentInfoServiceImpl.selectStudentInfoByStudentNumber.studentNumber{}", studentNumber);
        }
        // 通过学生编号查询学生信息
        return studentInfoMapper.selectByStudentNumber(studentNumber);
    }

    /**
     * 插入学生信息到数据库。
     * 该方法通过将StudentInfoBo对象的属性复制到StudentInfo对象中，并调用studentInfoMapper的insert方法来实现信息的插入。
     *
     * @param studentInfoBo 学生信息业务对象，包含需要插入的学生信息。
     *                      注意：该方法不返回任何值，即没有返回值。
     */
    @Override
    public int insertStudentInfo(StudentInfoBo studentInfoBo) {
        if (log.isInfoEnabled()) {
            log.info("StudentInfoServiceImpl.insertStudentInfo.studentInfoBo{}", studentInfoBo);
        }
        // 将StudentInfoBo对象转换为StudentInfo对象
        StudentInfo studentInfo = BeanUtil.copyProperties(studentInfoBo, StudentInfo.class);
        // 调用mapper插入学生信息
       return studentInfoMapper.insert(studentInfo);
    }

    /**
     * 更新学生信息。
     * 该方法通过接收一个学生信息BO（业务对象）实例，将其属性复制到学生信息实体中，然后根据实体的ID更新数据库中的学生信息。
     *
     * @param studentInfoBo 学生信息BO，包含需要更新的学生信息。
     *                      注意：该方法不返回任何值，更新操作的结果（例如成功或失败）无法通过方法调用直接获取。
     */
    @Override
    public void updateStudentInfo(StudentInfoBo studentInfoBo) {
        if (log.isInfoEnabled()) {
            log.info("StudentInfoServiceImpl.updateStudentInfo.studentInfoBo{}", studentInfoBo);
        }
        // 将学生信息BO复制到学生信息实体
        StudentInfo studentInfo = BeanUtil.copyProperties(studentInfoBo, StudentInfo.class);
        // 根据ID更新数据库中的学生信息
        studentInfoMapper.updateById(studentInfo);
    }

    /**
     * 删除指定的学生信息。
     *
     * @param studentId 学生的唯一标识符，类型为Long。
     *                  通过该标识符在数据库中定位并删除对应的 studentInfo 记录。
     * @return 无返回值。
     */
    @Override
    public void deleteStudent(Long studentId) {
        if (log.isInfoEnabled()) {
            log.info("StudentInfoServiceImpl.deleteStudent.studentId{}", studentId);
        }
        // 根据提供的学生ID创建一个StudentInfo实例，并设置ID，然后通过mapper删除该学生信息
        studentInfoMapper.deleteById(new StudentInfo().setId(studentId));
    }

    /**
     * 根据提供的学生信息查询条件，查询学生信息列表。
     *
     * @param studentInfoBo 学生信息查询条件对象，包含查询时所需的各种条件。
     * @return 返回学生信息的列表，列表中每个元素都是StudentInfoVo对象，封装了学生详细信息。
     */
    @Override
    public List<StudentInfoVo> selectStudentInfoList(StudentInfoBo studentInfoBo) {
        if (log.isInfoEnabled()) {
            log.info("StudentInfoServiceImpl.selectStudentInfoList.studentInfoBo{}", studentInfoBo);
        }
        // 构造查询条件包装对象，并通过mapper层执行查询操作，返回学生信息的Vo列表
        return studentInfoMapper.selectStudentInfoVoList(this.buildQueryWrapper(studentInfoBo));
    }

    /**
     * 批量根据学生ID列表查询学生信息列表。
     *
     * @param studentIdList 学生ID的列表，类型为List<Long>。这是要查询的学生ID的集合。
     * @return 返回一个学生信息列表，类型为List<StudentInfo>。这个列表包含了根据提供的学生ID列表查询到的学生信息。
     */
    @Override
    public List<StudentInfoVo> batchSelectStudentInfoListByStudentIdList(List<Long> studentIdList) {
        if (log.isInfoEnabled()) {
            log.info("StudentInfoServiceImpl.batchSelectStudentInfoListByStudentIdList.studentIdList:{}", studentIdList);
        }
        // 调用学生信息Mapper接口，根据提供的学生ID列表批量查询学生信息
        return MapstructUtils.convert(studentInfoMapper.selectBatchIds(studentIdList), StudentInfoVo.class);
    }

    /**
     * 根据手环ID查询学生信息。
     *
     * @param braceletId 手环的唯一标识符。
     * @return 返回匹配的学生信息的视图对象（StudentInfoVo）。
     */
    @Override
    public StudentInfoVo selectStudentInfoByBraceletId(String braceletId) {
        if (log.isInfoEnabled()) {
            log.info("StudentInfoServiceImpl.selectStudentInfoByBraceletId.braceletId{}", braceletId);
        }
        return studentInfoMapper.selectVoOne(new LambdaQueryWrapper<StudentInfo>()
            .eq(StudentInfo::getUuid, braceletId));
    }

    @Override
    public StudentInfoVo getStudentInfo(Long id) {
        return studentInfoMapper.selectVoOne(new LambdaQueryWrapper<StudentInfo>().eq(StudentInfo::getId, id));
    }


    /**
     * 根据StudentInfoBo构建查询条件
     *
     * @param studentInfoBo 学生信息业务对象，包含查询所需的参数
     * @return Wrapper<StudentInfo> 包含查询条件的包装器对象，用于MyBatis Plus的查询操作
     */
    private Wrapper<StudentInfo> buildQueryWrapper(StudentInfoBo studentInfoBo) {
        // 创建学生信息的查询包装器
        QueryWrapper<StudentInfo> wrapper = Wrappers.query();
        // 根据传入的ID、UUID、姓名构建查询条件
        return wrapper
            .eq(ObjectUtil.isNotNull(studentInfoBo.getId()),
                "id", studentInfoBo.getId())
            .eq(ObjectUtil.isNotNull(studentInfoBo.getUuid()),
                "uuid", studentInfoBo.getUuid())
            .like(ObjectUtil.isNotNull(studentInfoBo.getName()),
                "name", studentInfoBo.getName())
            .eq(ObjectUtil.isNotNull(studentInfoBo.getStudentNumber()),
                "student_number", studentInfoBo.getStudentNumber())
            .orderByAsc("id");
    }
}

