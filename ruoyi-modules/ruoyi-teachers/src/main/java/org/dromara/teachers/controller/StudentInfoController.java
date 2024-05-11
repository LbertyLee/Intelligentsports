package org.dromara.teachers.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.domain.bo.BindbraceletBo;
import org.dromara.teachers.domain.bo.StudentInfoBo;
import org.dromara.teachers.domain.entity.StudentInfo;
import org.dromara.teachers.domain.vo.StudentInfoVo;
import org.dromara.teachers.service.StudentInfoService;
import org.springframework.web.bind.annotation.*;


/**
 * (StudentInfo)表控制层
 *
 * @author LbertyLee
 * @since 2024-05-11 15:31:58
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/teacher/studentInfo")
public class StudentInfoController extends BaseController {

    @Resource
    private  StudentInfoService studentInfoService;


    /**
     * 绑定手环信息
     *
     * @param bindbraceletBo 包含绑定手环所需信息的请求体对象，例如学生ID和手环ID等
     * @return 返回一个结果对象，如果绑定成功，则返回成功状态；如果绑定失败，则返回失败状态及错误信息
     */
    @PostMapping("/bindbracelet")
    public R<Void> bindbracelet(@RequestBody BindbraceletBo bindbraceletBo){
        // 调用学生信息服务进行手环绑定操作，并将操作结果转换为Ajax响应格式返回
        return toAjax(studentInfoService.bindbracelet(bindbraceletBo));
    }

    /**
     * 获取学生信息列表
     *
     * @param studentInfoBo 学生信息查询条件，用于筛选学生信息
     * @param pageQuery 分页查询条件，用于指定返回结果的分页信息
     * @return TableDataInfo<StudentInfoVo> 包含学生信息列表的分页数据，包括当前页的学生信息列表和分页相关数据（如总记录数、总页数等）
     */
    @GetMapping("/list")
    public TableDataInfo<StudentInfoVo> list(StudentInfoBo studentInfoBo, PageQuery pageQuery){
        // 调用服务层方法，根据提供的查询条件获取学生信息的分页列表
        return studentInfoService.selectPageStudentInfoList(studentInfoBo,pageQuery);
    }

}

