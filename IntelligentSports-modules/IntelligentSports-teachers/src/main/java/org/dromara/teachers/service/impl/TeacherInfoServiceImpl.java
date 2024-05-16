package org.dromara.teachers.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.model.LoginUser;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.domain.SysUser;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.dromara.teachers.domain.bo.TeacherInfoBo;
import org.dromara.teachers.domain.vo.TeacherInfoVo;
import org.dromara.teachers.service.TeacherInfoService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class TeacherInfoServiceImpl implements TeacherInfoService {

    private final ISysUserService sysUserService;

    /**
     * 根据用户ID查询教师信息。
     *
     * @param userId 教师的用户ID，用于查询特定教师的信息。
     * @return TeacherInfoVo 教师信息视图对象，包含了教师的详细信息。
     */
    @Override
    public TeacherInfoVo selectTeacherInfoById(Long userId) {
        // 通过用户ID查询系统用户信息
        SysUserVo sysUserVo = sysUserService.selectUserById(userId);
        // 将系统用户信息转换为教师信息视图对象
        return BeanUtil.copyProperties(sysUserVo, TeacherInfoVo.class);
    }

    /**
     * 重置密码接口。
     * 该方法允许教师重设他们的密码。如果尝试重设的教师是当前登录的教师，则会检查新密码和确认密码是否一致。
     * 如果一致，则更新密码。如果不是当前登录的教师尝试重设密码，会抛出一个服务异常。
     *
     * @param teacherInfoBo 包含教师信息的BO（业务对象），需要至少包含用户ID、新密码和确认新密码。
     * @return 返回更新成功与否的标志，通常为1表示成功，其他值或异常表示失败。
     * @throws ServiceException 如果两次输入的密码不一致或者尝试修改其他用户的密码，会抛出此异常。
     */
    @Override
    public int resetPwd(TeacherInfoBo teacherInfoBo) {
        Long userId = teacherInfoBo.getUserId();
        // 检查是否为当前登录用户尝试修改密码
        if (teacherInfoBo.getUserId().equals(LoginHelper.getUserId())) {
            // 校验新密码和确认密码是否一致
            if (!teacherInfoBo.getNewPassword().equals(teacherInfoBo.getConfirmPassword())) {
                throw new ServiceException("两次输入的密码不一致");
            }
            SysUserVo sysUserVo = sysUserService.selectUserById(userId);
            if (BCrypt.checkpw(teacherInfoBo.getNewPassword(), sysUserVo.getPassword())) {
                throw new ServiceException("原密码错误");
            }
            // 创建一个SysUser实例用于更新密码，使用BCrypt加密新密码
            SysUser sysUser = new SysUser().setUserId(userId).setPassword(BCrypt.hashpw(teacherInfoBo.getNewPassword()));
            // 调用服务更新教师信息
            return sysUserService.updateTeacherInfo(sysUser);
        } else {
            // 如果是尝试修改其他用户的密码，则抛出异常
            throw new ServiceException("您没有权限修改其他用户密码");
        }
    }

}
