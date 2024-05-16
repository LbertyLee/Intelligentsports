package org.dromara.test;

import org.dromara.teachers.mapper.HealthMetricsMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("单元测试案例")
public class HealthMetricsTest {


    @Autowired
    private HealthMetricsMapper healthMetricsMapper;

    @Test
        public void testSelect() {
        System.out.println(healthMetricsMapper.selectList());
    }
}
