package org.dromara.teachers.service;

import org.dromara.teachers.domain.bo.TeacherInfoBo;
import org.dromara.teachers.domain.vo.TeacherInfoVo;

public interface TeacherInfoService {

    /**
     * 根据用户ID查询教师信息。
     *
     * @param userId 教师的用户ID，不能为空。
     * @return TeacherInfoVo 教师信息的视图对象，包含教师的基本信息。
     */
    TeacherInfoVo selectTeacherInfoById(Long userId);

    /**
     * 重置密码接口。
     * 该方法用于根据传入的教师信息对象重置密码。
     *
     * @param teacherInfoBo 教师信息对象，包含需要重置密码的教师的标识信息。
     * @return 返回操作结果，通常为成功（正数）或失败（负数）的整数值。
     */
    int resetPwd(TeacherInfoBo teacherInfoBo);

}

