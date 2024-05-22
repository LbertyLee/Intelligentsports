package org.dromara.teachers.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;
import org.dromara.teachers.domain.vo.TrainingTeamVo;
import org.mybatis.spring.annotation.MapperScan;

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
@TableName("teacher_training_team")
@AutoMapper(target = TrainingTeamVo.class)
@EqualsAndHashCode(callSuper=false)
public class TrainingTeam extends TenantEntity {

    /**唯一标识*/
    private Long id;

    /**训练队名*/
    private String teamName;


}

