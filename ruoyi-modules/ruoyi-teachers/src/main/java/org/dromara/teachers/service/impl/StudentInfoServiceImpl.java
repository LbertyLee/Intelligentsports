package org.dromara.teachers.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.BindbraceletBo;
import org.dromara.teachers.domain.bo.StudentInfoBo;
import org.dromara.teachers.domain.entity.StudentInfo;
import org.dromara.teachers.domain.vo.StudentInfoVo;
import org.dromara.teachers.mapper.StudentInfoMapper;
import org.dromara.teachers.service.StudentInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * (StudentInfo)表服务实现类
 *
 * @author LbertyLee
 * @since 2024-05-11 15:32:01
 */
@Service("studentInfoService")
@RequiredArgsConstructor
public class StudentInfoServiceImpl  implements StudentInfoService {

    private final StudentInfoMapper studentInfoMapper;

    /**
     * 绑定学生和手环信息
     *
     * @param bindbraceletBo 包含学生ID和手环ID的绑定信息对象
     * @return 返回更新记录的数量，即影响的行数
     */
    @Override
    public int bindbracelet(BindbraceletBo bindbraceletBo) {
        // 创建学生信息对象，并设置学生ID和手环ID
        StudentInfo studentInfo = new StudentInfo()
            .setId(bindbraceletBo.getStudentId())
            .setUuid(bindbraceletBo.getBraceletId());
        // 根据学生信息更新数据库中的记录，并返回更新的行数
        return studentInfoMapper.updateById(studentInfo);
    }

    /**
     * 分页查询学生信息列表
     *
     * @param studentInfoBo 学生信息查询条件
     * @param pageQuery 分页查询参数
     * @return TableDataInfo<StudentInfoVo> 包含分页学生信息的表格数据
     */
    @Override
    public TableDataInfo<StudentInfoVo> selectPageStudentInfoList(StudentInfoBo studentInfoBo, PageQuery pageQuery) {
        // 根据查询条件和分页参数，执行数据库查询
        Page<StudentInfoVo> page = studentInfoMapper.selectPageStudentInfoList(pageQuery.build(), this.buildQueryWrapper(studentInfoBo));
        // 将查询结果封装成TableDataInfo对象返回
        return TableDataInfo.build(page);
    }

    /**
     * 根据StudentInfoBo构建查询条件
     *
     * @param studentInfoBo 学生信息业务对象，包含查询所需的参数
     * @return Wrapper<StudentInfo> 包含查询条件的包装器对象，用于MyBatis Plus的查询操作
     */
    private Wrapper<StudentInfo> buildQueryWrapper(StudentInfoBo studentInfoBo) {
        // 获取传入参数
        Map<String, Object> params = studentInfoBo.getParams();
        // 创建学生信息的查询包装器
        QueryWrapper<StudentInfo> wrapper = Wrappers.query();
        // 根据传入的ID、UUID、姓名构建查询条件
        return  wrapper.eq(ObjectUtil.isNotNull(studentInfoBo.getId()), "id", studentInfoBo.getId())
            .eq(ObjectUtil.isNotNull(studentInfoBo.getUuid()), "uuid", studentInfoBo.getUuid())
            .like(ObjectUtil.isNotNull(studentInfoBo.getName()), "student_id", studentInfoBo.getName())
            .orderByAsc("id");
    }
}

