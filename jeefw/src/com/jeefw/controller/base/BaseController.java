package com.jeefw.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.jeefw.core.Constant;
import com.jeefw.model.sys.SysUser;

import core.controller.ExtJSBaseController;
import core.support.ExtJSBaseParameter;
import core.util.UuidUtil;



public class BaseController<E extends ExtJSBaseParameter> extends ExtJSBaseController<E> implements Constant{
	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView() {
		return new ModelAndView();
	}

	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		return request;
	}
	
	/**
	 * 得到32位的uuid
	 * 
	 * @return
	 */
	public String get32UUID() {

		return UuidUtil.get32UUID();
	}

    public Long getUserId(){
    	 return ((SysUser) getRequest().getSession().getAttribute(SESSION_SYS_USER)).getId();
    }	
 

}
