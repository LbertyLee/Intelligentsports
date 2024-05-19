package org.dromara.teachers.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectionDataVo {

    private Integer braceletsTotalNum;

    private Integer braceletsOnlineNum;

    private List<TaskHealthMetricsVo> taskHealthMetricsVoList;
}
