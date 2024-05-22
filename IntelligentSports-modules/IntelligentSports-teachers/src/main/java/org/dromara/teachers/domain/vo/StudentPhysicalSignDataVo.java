package org.dromara.teachers.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.tenant.core.TenantEntity;

/**
 * 学生体征数据(TeacherStudentPhysicalSignData)表实体类
 *
 * @author LbertyLee
 * @since 2024-05-16 16:35:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper=false)
public class StudentPhysicalSignDataVo extends TenantEntity {
    //唯一标识
    private Integer id;
    //训练ID
    private Long taskId;
    //学生ID
    private Long studentId;
    //学生姓名
    private String studentName;
    //实时血氧
    private Long bloodOxygen;
    //实时心率
    private Long heartRate;
    //实时配速
    private Long realTimeSpeedMatching;
    //共计用时
    private Long totalTime;




    }

