package org.dromara.teachers.controller;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.excel.core.ExcelResult;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.teachers.domain.bo.BindDraceletBo;
import org.dromara.teachers.domain.bo.StudentInfoBo;
import org.dromara.teachers.domain.vo.StudentInfoExportVo;
import org.dromara.teachers.domain.vo.StudentInfoImportVo;
import org.dromara.teachers.domain.vo.StudentInfoVo;
import org.dromara.teachers.listener.StudentInfoImportListener;
import org.dromara.teachers.service.StudentInfoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


/**
 * 学生信息Controller
 *
 * @author LbertyLee
 * @since 2024-05-11 15:31:58
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/teacher/studentInfo")
public class StudentInfoController extends BaseController {

    private final StudentInfoService studentInfoService;


    /**
     * 绑定手环信息
     *
     * @param bindBracelet 包含绑定手环所需信息的请求体对象，例如学生ID和手环ID等
     * @return 返回一个结果对象，如果绑定成功，则返回成功状态；如果绑定失败，则返回失败状态及错误信息
     */
    @PostMapping("/bindbracelet")
    public R<Void> bindBracelet(@RequestBody BindDraceletBo bindBracelet){
        // 调用学生信息服务进行手环绑定操作，并将操作结果转换为Ajax响应格式返回
        return toAjax(studentInfoService.bindbracelet(bindBracelet));
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

    /**
     * 导入学生数据。
     *
     * @param file 需要导入的文件，通过MultipartFile类型接收。
     * @param updateSupport 是否支持更新现有数据。如果为true，则在导入时会尝试更新已存在的数据；如果为false，则只插入新数据。
     * @return 返回一个包含导入结果信息的R<Void>对象，其中的result.getAnalysis()提供了导入分析结果。
     * @throws Exception 如果导入过程中发生错误，则抛出异常。
     */
    @PostMapping("/importData")
    public R<Void> importData(@RequestPart("file") MultipartFile file, boolean updateSupport) throws Exception {
        // 使用ExcelUtil工具类的importExcel方法导入Excel文件，将文件流、期望的实体类类型，以及一个监听器作为参数传入
        ExcelResult<StudentInfoImportVo> result = ExcelUtil.importExcel(
            file.getInputStream(), StudentInfoImportVo.class, new StudentInfoImportListener(updateSupport)
        );
        // 返回导入结果
        return R.ok(result.getAnalysis());
    }

    /**
     * 导出学生信息列表
     */
    @PostMapping("/export")
    public void export(StudentInfoBo studentInfoBo, HttpServletResponse response) {
        List<StudentInfoVo> list = studentInfoService.selectStudentInfoList(studentInfoBo);
        List<StudentInfoExportVo> listVo = MapstructUtils.convert(list, StudentInfoExportVo.class);
        ExcelUtil.exportExcel(listVo, "学生手环数据", StudentInfoExportVo.class, response);
    }

    /**
     * 获取导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "用户数据", StudentInfoImportVo.class, response);
    }
    /**
     * 删除学生
     */
    @DeleteMapping("/{studentId}")
    public R<Void> deleteStudent(@PathVariable Long studentId) {
        studentInfoService.deleteStudent(studentId);
        return R.ok();
    }

    /**
     * 更新学生信息。
     *
     * @param studentInfoBo 包含学生信息的请求体，类型为StudentInfoBo。
     * @return 返回操作结果，成功则返回R<Void>.ok()，表示操作成功。
     */
    @PutMapping
    public R<Void> updateStudent(@RequestBody StudentInfoBo studentInfoBo) {
        // 调用学生信息服务，更新学生信息
        studentInfoService.updateStudentInfo(studentInfoBo);
        return R.ok();
    }
}

