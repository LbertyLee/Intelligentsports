package org.dromara.teachers.domain.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BraceletStatusBO {
    private String uuid;
    private Integer isOnline;
}
