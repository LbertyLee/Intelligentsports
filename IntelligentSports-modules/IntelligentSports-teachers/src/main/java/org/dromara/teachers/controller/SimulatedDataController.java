package org.dromara.teachers.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.domain.entity.HealthMetrics;
import org.dromara.teachers.service.HealthMetricsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/simulatedData")
public class SimulatedDataController extends BaseController {

    private final HealthMetricsService healthMetricsService;


    @PostMapping("/add")
    public R<Void> addSimulatedData(@RequestBody HealthMetrics healthMetrics) {
       return toAjax(healthMetricsService.add(healthMetrics));
    }
}
