package org.dromara.teachers.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.ValidatorUtils;
import org.dromara.common.excel.core.ExcelListener;
import org.dromara.common.excel.core.ExcelResult;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.teachers.domain.bo.StudentInfoBo;
import org.dromara.teachers.domain.entity.StudentInfo;
import org.dromara.teachers.domain.vo.StudentInfoImportVo;
import org.dromara.teachers.service.StudentInfoService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
public class StudentInfoImportListener extends AnalysisEventListener<StudentInfoImportVo> implements ExcelListener<StudentInfoImportVo> {
    private final StudentInfoService  studentInfoService;
    // 成功操作的数量。
    private int successNum = 0;
    // 失败操作的数量。
    private int failureNum = 0;
    // 指示是否支持更新操作的标志
    private final Boolean isUpdateSupport;
    // 执行操作的用户ID。
    private final Long operUserId;
    // 用于存储成功操作的消息的StringBuilder
    private final StringBuilder successMsg = new StringBuilder();
    // 用于存储失败操作的消息的StringBuilder。
    private final StringBuilder failureMsg = new StringBuilder();

    /**
     * 构造函数：StudentInfoImportListener
     * 用于初始化学生信息导入监听器。
     *
     * @param isUpdateSupport 一个布尔值，标识是否支持更新操作。true表示支持更新，false表示不支持。
     */
    public StudentInfoImportListener(Boolean isUpdateSupport) {
        // 通过Spring工具类获取StudentInfoService的实例
        this.studentInfoService = SpringUtils.getBean(StudentInfoService.class);
        // 设置是否支持更新的标志
        this.isUpdateSupport = isUpdateSupport;
        // 获取当前操作用户的ID
        this.operUserId = LoginHelper.getUserId();
    }


    /**
     * 获取Excel处理结果的接口方法。
     *
     * @return ExcelResult<StudentInfoImportVo> 返回处理后的Excel结果，这里返回null代表默认实现不提供具体结果。
     */
    @Override
    public ExcelResult<StudentInfoImportVo> getExcelResult() {
        return new ExcelResult<>() {

            @Override
            public String getAnalysis() {
                if (failureNum > 0) {
                    failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                    throw new ServiceException(failureMsg.toString());
                } else {
                    successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
                }
                return successMsg.toString();
            }

            @Override
            public List<StudentInfoImportVo> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }

    /**
     * 对单个学生信息进行处理的接口方法。
     *
     * @param studentInfoImportVo 学生信息导入Vo对象，包含单个学生的信息。
     * @param analysisContext 分析上下文对象，提供分析过程中的上下文环境信息。
     */
    @Override
    public void invoke(StudentInfoImportVo studentInfoImportVo, AnalysisContext analysisContext) {
        StudentInfo studentInfo =this.studentInfoService
            .selectStudentInfoByStudentNumber(studentInfoImportVo.getStudentNumber());

        try{
            if(ObjectUtil.isNull(studentInfo)){
                StudentInfoBo studentInfoBo = BeanUtil.copyProperties(studentInfoImportVo, StudentInfoBo.class);
                ValidatorUtils.validate(studentInfoBo);
                studentInfoService.insertStudentInfo(studentInfoBo);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、账号 ").append(studentInfoBo.getName()).append(" 导入成功");
            }else if(isUpdateSupport){
                StudentInfoBo studentInfoBo = BeanUtil.copyProperties(studentInfoImportVo, StudentInfoBo.class);
                ValidatorUtils.validate(studentInfoBo);
                studentInfoBo.setUpdateBy(operUserId);
                studentInfoService.updateStudentInfo(studentInfoBo);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、账号 ").append(studentInfoBo.getName()).append(" 更新成功");
            }else {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(studentInfo.getName()).append(" 已存在");
            }
        }catch (Exception  e){
            failureNum++;
            String msg = "<br/>" + failureNum + "、账号 " + studentInfoImportVo.getName() + " 导入失败：";
            failureMsg.append(msg).append(e.getMessage());
            log.error(msg, e);
        }

    }

    /**
     * 所有学生信息处理完毕后的接口方法。
     *
     * @param analysisContext 分析上下文对象，提供分析过程中的上下文环境信息。
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
