package org.dromara.teachers.domain.bo;


import lombok.Data;

@Data
public class TeacherInfoBo {
    /**用户ID*/
    private Long userId;
    /**旧密码*/
    private String oldPassword;
    /**新密码*/
    private String newPassword;
    /**确认密码*/
    private String confirmPassword;
}
