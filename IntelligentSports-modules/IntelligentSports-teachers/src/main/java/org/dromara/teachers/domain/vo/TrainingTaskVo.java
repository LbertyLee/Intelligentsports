package org.dromara.teachers.domain.vo;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.List;


/**
 * 训练任务表(TeacherTrainingTask)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-15 11:18:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingTaskVo extends TenantEntity {

    //唯一标识
    private Long id;

    //训练队ID
    private Long trainingTeamId;

    //训练队名称
    private String trainingTeamName;

    //训练类型名称
    private String exerciseTypeName;

    //组数
    private Integer number;

    //教师姓名
    private String teacherName;

    //训练时长（单位秒）
    private Long trainingTime;

    //手环总数
    private Integer  braceletsTotal;

    //手环在线数
    private Integer braceletsOnlineNum;

    //训练任务中所有学生id
    private List<Long> students;


}

