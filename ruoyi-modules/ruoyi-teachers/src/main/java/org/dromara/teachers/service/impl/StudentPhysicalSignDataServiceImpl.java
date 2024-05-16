package org.dromara.teachers.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.dromara.teachers.mapper.StudentPhysicalSignDataMapper;
import org.dromara.teachers.domain.entity.StudentPhysicalSignData;
import org.dromara.teachers.service.StudentPhysicalSignDataService;
import org.springframework.stereotype.Service;

/**
 * 学生体征数据(TeacherStudentPhysicalSignData)表服务实现类
 *
 * @author LbertyLee
 * @since 2024-05-16 16:35:28
 */
@RequiredArgsConstructor
@Service("teacherStudentPhysicalSignDataService")
public class StudentPhysicalSignDataServiceImpl extends ServiceImpl<StudentPhysicalSignDataMapper, StudentPhysicalSignData> implements StudentPhysicalSignDataService {

}

