package org.dromara.teachers.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.teachers.domain.vo.BraceletStatusVo;

import java.io.Serializable;

/**
 * 手环状态(TeacherBraceletStatus)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-18 14:49:35
 */
@Data
@TableName("teacher_bracelet_status")
@AutoMapper(target = BraceletStatusVo.class)
public class BraceletStatus {

    //手环唯一标识
    @TableId
    private String uuid;
    //是否在线（1：在线，0：不在线）
    private Integer isOnline;


}

