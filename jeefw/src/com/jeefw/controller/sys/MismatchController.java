package com.jeefw.controller.sys;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeefw.controller.base.BaseController;
import com.jeefw.model.sys.Loan;
import com.jeefw.model.sys.SysUser;
import com.jeefw.service.sys.MismatchService;
import com.jeefw.service.sys.SysUserService;



/**
 * 错配控制
 */
@Controller
@RequestMapping("/mismatch")
public class MismatchController extends BaseController<Loan> {
	
	private static final Log log = LogFactory.getLog(MismatchController.class.getCanonicalName());
	@Resource
	private SysUserService sysUserService;
	@Resource
	private MismatchService mismatchService;
	/*
	 * 进行错配
	 */
	public void save(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		SysUser sysuser = sysUserService.get(getUserId());
		/*mismatchService.addMismatch(sysuser.getId(),"");*/
		log.debug("进入 错配");
		//writeJSON(response, result);
	}
	
	/*
	 * 进行错配
	 */
	public void check(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		SysUser sysuser = sysUserService.get(getUserId());
		/*mismatchService.addMismatch(sysuser.getId(),"");*/
		log.debug("进入 错配");
		//writeJSON(response, result);
	}
	
}
