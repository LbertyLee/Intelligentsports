package org.dromara.teachers.domain.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;

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
public class StudentInfo extends TenantEntity {

    private Long id;

    private String name;

    private String uuid;

}

