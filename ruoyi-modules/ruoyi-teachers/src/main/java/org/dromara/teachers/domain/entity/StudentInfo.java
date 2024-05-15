package org.dromara.teachers.domain.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;
import org.dromara.teachers.domain.vo.StudentInfoVo;

import java.io.Serializable;

/**
 * (StudentInfo)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-11 15:31:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@AutoMapper(target = StudentInfoVo.class)
public class StudentInfo extends TenantEntity {

    /** 学生的唯一标识符*/
    private Long id;

    /** 学生的姓名*/
    private String name;

    /** 手环id*/
    private String uuid;

    /** 学生的学号*/
    private String studentNumber;

}

