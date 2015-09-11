package com.jeefw.controller.sys;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jeefw.controller.base.BaseController;
import com.jeefw.core.Constant;
import com.jeefw.model.sys.Loan;
import com.jeefw.model.sys.SysUser;
import com.jeefw.service.sys.InvestService;
import com.jeefw.service.sys.LoanService;
import com.jeefw.service.sys.MailService;
import com.jeefw.service.sys.SysUserService;

import core.util.Const;
import core.util.FileUpload;
import core.util.PathUtil;


/**
 * 文件上传
 */
@Controller
@RequestMapping("/uploadFile")
public class UploadController extends BaseController<Loan> implements Constant{
	
	private static final Log log = LogFactory.getLog(UploadController.class.getCanonicalName());
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private InvestService investService;
	@Resource
	private LoanService loanService;
	@Resource
	private MailService mailService;
	/*
	 * 投资信息上传
	 */
	@RequestMapping(value = "/uploadInvestFile")
	public void uploadInvestFile(@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request, HttpServletResponse response)throws IOException {
		log.debug("投资信息上传");
		Map<String, Object> result = new HashMap<String, Object>();
		String userId = ((SysUser) request.getSession().getAttribute(SESSION_SYS_USER)).getId().toString();
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;
			String fileName = FileUpload.fileUp(file, filePath, DateFormatUtils.format(new Date(), "yyyyMMddHHssmm"));
			String errMess = investService.addInvestData(filePath, fileName,userId).get("errMess");
			if(errMess != null){
				result.put("result", "投资信息导入失败："+"\n"+errMess);
			}else{
				result.put("result", "投资信息导入成功！");
			}
		}
		writeJSON(response, result);
	}
	
	/*
	 * 借款信息上传
	 */
	@RequestMapping(value = "/uploadLoanFile")
	public void uploadLoanFile(@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request, HttpServletResponse response)throws IOException {
		log.debug("借款信息上传");
		Map<String, Object> result = new HashMap<String, Object>();
		String userId = ((SysUser) request.getSession().getAttribute(SESSION_SYS_USER)).getId().toString();
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE; //文件上传路径
			String fileName = FileUpload.fileUp(file, filePath, DateFormatUtils.format(new Date(), "yyyyMMddHHssmm")); //执行上传
			String errMess = loanService.addLoanData(filePath, fileName,userId).get("errMess");
			if(errMess != null){
				result.put("result", "借款信息导入失败："+"\n"+errMess);
			}else{
				result.put("result", "借款信息导入成功！");
			}
		}
		writeJSON(response, result);
	}
	
	
	
	
}

