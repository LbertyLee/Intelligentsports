package org.dromara.teachers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.teachers.domain.vo.BraceletStatusVo;
import org.dromara.teachers.mapper.BraceletStatusMapper;
import org.dromara.teachers.service.BraceletStatusService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 手环状态(TeacherBraceletStatus)表服务实现类
 *
 * @author LbertyLee
 * @since 2024-05-18 14:49:36
 */
@Slf4j
@Service("teacherBraceletStatusService")
@RequiredArgsConstructor
public class BraceletStatusServiceImpl  implements BraceletStatusService {


    private final BraceletStatusMapper braceletStatusMapper;
    @Override
    public List<BraceletStatusVo> selectBraceletList(List<String> braceletsTotalNum) {
        if(log.isInfoEnabled()){
            log.info("BraceletStatusServiceImpl.selectBraceletList.braceletsTotalNum:{}",braceletsTotalNum);
        }
        return braceletStatusMapper.selectVoBatchIds(braceletsTotalNum);
    }
}

