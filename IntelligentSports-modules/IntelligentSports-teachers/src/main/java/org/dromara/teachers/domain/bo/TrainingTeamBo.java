package org.dromara.teachers.domain.bo;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 训练队(TeacherTrainingTeam)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-11 23:55:56
 */
@Data
@Accessors(chain = true)
public class TrainingTeamBo {

    /**唯一标识*/
    private Long id;

    /**训练队名*/
    private String teamName;


}

