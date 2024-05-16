package org.dromara.teachers.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.domain.entity.HealthMetrics;
import org.dromara.teachers.domain.vo.HealthMetricsVo;
import org.dromara.teachers.mapper.HealthMetricsMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor

public class TestController extends BaseController {

    private final HealthMetricsMapper healthMetricsMapper;

    @GetMapping
    public R<List<HealthMetrics>> getHealthMetrics(){
        return R.ok(healthMetricsMapper.selectList());
    }

}