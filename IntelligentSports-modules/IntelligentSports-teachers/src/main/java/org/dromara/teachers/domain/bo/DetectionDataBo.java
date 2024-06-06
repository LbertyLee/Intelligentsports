package org.dromara.teachers.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class DetectionDataBo {

    /**训练队id*/
    private Long taskId;

    /**学生id列表*/
    private List<Long> studentIds;

    /**训练时长*/
    private Long  trainingTime;

    /**次数*/
    private Integer number;
}
