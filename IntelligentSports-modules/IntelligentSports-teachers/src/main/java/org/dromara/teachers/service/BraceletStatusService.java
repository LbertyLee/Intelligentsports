package org.dromara.teachers.service;


import org.dromara.teachers.domain.vo.BraceletStatusVo;

import java.util.List;

/**
 * 手环状态(TeacherBraceletStatus)表服务接口
 *
 * @author LbertyLee
 * @since 2024-05-18 14:49:36
 */
public interface BraceletStatusService{

    List<BraceletStatusVo> selectBraceletList(List<String> braceletsTotalNum);
}

