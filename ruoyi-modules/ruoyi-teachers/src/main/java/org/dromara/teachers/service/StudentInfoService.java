package org.dromara.teachers.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.teachers.domain.bo.BindDraceletBo;
import org.dromara.teachers.domain.bo.StudentInfoBo;
import org.dromara.teachers.domain.entity.StudentInfo;
import org.dromara.teachers.domain.vo.StudentInfoVo;

import java.util.List;

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
    int bindbracelet(BindDraceletBo bindbraceletBo);


    /**
     * 分页查询学生信息列表
     *
     * @param studentInfoBo 包含查询条件的学生信息实体，用于筛选学生信息
     * @return TableDataInfo<StudentInfoVo> 返回一个包含分页学生信息的实体，包括学生信息列表、总记录数等
     */
    TableDataInfo<StudentInfoVo> selectPageStudentInfoList(StudentInfoBo studentInfoBo, PageQuery pageQuery);

    /**
     * 根据学号查询学生信息。
     *
     * @param studentNumber 学生的编号，用于查询和设置学生信息的关键标识。
     * @return 返回设置好的学生信息对象，如果找不到对应学生编号的信息，则返回null。
     */
    StudentInfo selectStudentInfoByStudentNumber(String studentNumber);


    /**
     * 插入学生信息到数据库。
     *
     * @param studentInfo 包含学生详细信息的对象。该对象应包含学生的所有必要属性，如姓名、年龄、年级等。
     *                    通过这个方法，可以将新的学生信息存储到数据库中。
     * @return 无返回值。此方法操作数据库，将学生信息插入到指定的数据表中。
     */
    void insertStudentInfo(StudentInfoBo studentInfo);

    /**
     * 更新学生信息。
     * <p>
     * 该方法用于根据传入的学生信息对象，更新学生的信息。这可能涉及到数据库的更新操作。
     *
     * @param studentInfoBo 学生信息对象，包含需要更新的学生信息。该对象不应为空。
     */
    void updateStudentInfo(StudentInfoBo studentInfoBo);

    /**
     * 删除学生信息。
     *
     * @param studentId 学生的唯一标识符，类型为Long。
     *                  通过该标识符在数据库中定位并删除对应的学生记录。
     * @return 无返回值。
     */
    void deleteStudent(Long studentId);

    /**
     * 查询学生信息列表
     *
     * @param studentInfoBo 学生信息查询条件对象，封装了查询时需要的条件
     * @return 返回学生信息的列表，列表中每个元素都是StudentInfoVo对象，封装了学生详细信息
     */
    List<StudentInfoVo> selectStudentInfoList(StudentInfoBo studentInfoBo);

    List<StudentInfo> batchSelectStudentInfoListByStudentIdList(List<Long> studentIdList);

}

