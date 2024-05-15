package org.dromara.teachers.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;

/**
 * 训练队(TeacherTrainingTeam)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-11 23:55:56
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTeamVo extends TenantEntity {

    /**唯一标识*/
    private Long id;

    /**训练队名*/
    private String teamName;


}

