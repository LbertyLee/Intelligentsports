package org.dromara.teachers.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.BindbraceletBo;
import org.dromara.teachers.domain.bo.StudentInfoBo;
import org.dromara.teachers.domain.entity.StudentInfo;
import org.dromara.teachers.domain.vo.StudentInfoVo;

/**
 * (StudentInfo)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-11 15:32:00
 */
public interface StudentInfoService  {

    /**
     * 绑定手环接口
     *
     * @param bindbraceletBo 绑定手环业务对象，包含绑定所需的全部信息
     * @return 返回操作结果，成功返回1，失败返回0
     */
    int bindbracelet(BindbraceletBo bindbraceletBo);


    /**
     * 分页查询学生信息列表
     *
     * @param studentInfoBo 包含查询条件的学生信息实体，用于筛选学生信息
     * @return TableDataInfo<StudentInfoVo> 返回一个包含分页学生信息的实体，包括学生信息列表、总记录数等
     */
    TableDataInfo<StudentInfoVo> selectPageStudentInfoList(StudentInfoBo studentInfoBo, PageQuery pageQuery);
}

