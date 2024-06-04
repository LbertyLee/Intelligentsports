package org.dromara.teachers.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.service.HealthMetricsService;
import org.dromara.teachers.tcp.client.TcpClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health/metrics")
@RequiredArgsConstructor
public class HealthMetricsController extends BaseController {

    private final HealthMetricsService healthMetricsService;

//    @GetMapping("/add")
//    public R<Void> addHealthMetrics() throws Exception {
//        new TcpClient(healthMetricsService).run();
//       return R.ok();
//    }
//    @GetMapping("/close")
//    public R<Void> closeHealthMetrics() throws Exception {
//        new TcpClient(healthMetricsService).close();
//        return R.ok();
//    }
}
