package org.dromara.teachers.domain.bo;

import lombok.Data;

/**
 * 绑定手环的数据实体类
 * 用于记录学生和其所绑定的手环信息
 */
@Data
public class BindDraceletBo {

    /**学生的唯一标识ID*/
    private Long studentId;

    /**手环的唯一标识ID*/
    private String braceletId;
}
