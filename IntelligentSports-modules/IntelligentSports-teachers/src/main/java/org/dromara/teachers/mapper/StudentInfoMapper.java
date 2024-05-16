package org.dromara.teachers.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.teachers.domain.entity.StudentInfo;
import org.dromara.teachers.domain.vo.StudentInfoVo;

import java.util.List;


/**
 * (StudentInfo)表数据库访问层
 *
 * @author LbertyLee
 * @since 2024-05-11 15:31:59
 */
@Mapper
public interface StudentInfoMapper extends BaseMapper<StudentInfo> {
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<StudentInfoVo> selectPageStudentInfoList(@Param("page") Page<StudentInfo> page,
                                                  @Param(Constants.WRAPPER) Wrapper<StudentInfo> queryWrapper);

    StudentInfo selectByStudentNumber(String studentNumber);

    List<StudentInfoVo> selectStudentInfoVoList(@Param(Constants.WRAPPER) Wrapper<StudentInfo> queryWrapper);

}

