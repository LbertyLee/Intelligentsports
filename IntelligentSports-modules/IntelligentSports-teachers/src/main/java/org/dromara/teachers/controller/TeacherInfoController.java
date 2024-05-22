package org.dromara.teachers.controller;


import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.domain.bo.TeacherInfoBo;
import org.dromara.teachers.domain.vo.TeacherInfoVo;
import org.dromara.teachers.service.TeacherInfoService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/teacher/info")
@RestController
public class TeacherInfoController extends BaseController {

    private final TeacherInfoService teacherInfoService;

    /**
     * 根据用户ID获取教师信息。
     *
     * @param userId 教师的用户ID，通过URL路径变量传递。
     * @return 返回对应教师ID的教师信息，封装在TeacherInfoVo对象中。
     */
    @GetMapping("/{userId}")
    public R<TeacherInfoVo> getTeacherInfo(@PathVariable("userId") Long userId) {
        // 通过教师ID查询并返回教师信息
        return R.ok(teacherInfoService.selectTeacherInfoById(userId));
    }

    /**
     * 重置密码接口
     *
     * @param teacherInfoBo 包含教师信息的请求体，用于识别需要重置密码的教师
     * @return R<Void> 返回一个结果对象，如果操作成功，result.code为200；否则根据具体错误情况返回相应代码
     */
    @PostMapping("/resetPwd")
    public R<Void> resetPwd(@RequestBody TeacherInfoBo teacherInfoBo) {
       // 调用服务层方法，执行密码重置操作，并将结果转换为前端可识别的格式返回
       return toAjax(teacherInfoService.resetPwd(teacherInfoBo));
    }


}