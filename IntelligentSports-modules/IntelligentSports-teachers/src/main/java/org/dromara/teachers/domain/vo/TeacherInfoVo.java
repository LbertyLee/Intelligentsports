package org.dromara.teachers.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.sensitive.annotation.Sensitive;
import org.dromara.common.sensitive.core.SensitiveStrategy;
import org.dromara.system.domain.SysUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@AutoMapper(target = SysUser.class)
public class TeacherInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /*** 用户ID*/
    private Long userId;

    /*** 租户ID*/
    private String tenantId;

    /*** 用户账号*/
    private String userName;

    /*** 用户昵称*/
    private String nickName;

    /*** 手机号码*/
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phonenumber;

    /*** 备注*/
    private String remark;

    /*** 最后登录时间*/
    private Date loginDate;
}
