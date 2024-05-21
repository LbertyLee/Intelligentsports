package org.dromara.teachers.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BraceletStatusBo {
    private String uuid;
    private Integer isOnline;
}
