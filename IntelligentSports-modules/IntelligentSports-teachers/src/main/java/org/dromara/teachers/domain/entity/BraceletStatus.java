package org.dromara.teachers.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.teachers.domain.vo.BraceletStatusVo;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("teacher_bracelet_status")
@AutoMapper(target = BraceletStatusVo.class)
public class BraceletStatus {
    private String uuid;
    private Integer isOnline;
}
