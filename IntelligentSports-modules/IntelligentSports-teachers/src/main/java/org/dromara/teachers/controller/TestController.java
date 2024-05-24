package org.dromara.teachers.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.job.TrainingDataToDay;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController extends BaseController {


    @Resource
    private TrainingDataToDay trainingDataToDay;

    @GetMapping
    public R<Object> test() {
        trainingDataToDay.trainingDataToDay();
        return R.ok();
    }
}
