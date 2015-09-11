package com.jeefw.controller.sys;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeefw.core.JavaEEFrameworkBaseController;
import com.jeefw.model.sys.CapitalHistory;
import com.jeefw.model.sys.DetailedAccount;
import com.jeefw.model.sys.Role;
import com.jeefw.model.sys.SysUser;
import com.jeefw.service.sys.DetailedAccountService;

import core.support.ExtJSBaseParameter;
import core.support.JqGridPageView;

/**
 * 附件的控制层
 */
@Controller
@RequestMapping("/sys/detailedaccounting")
public class DetailedAccountingController extends
		JavaEEFrameworkBaseController<DetailedAccount> {
	private static final Log log = LogFactory
			.getLog(DetailedAccountingController.class);
	@Resource
	private DetailedAccountService detailedAccountService;

	@RequestMapping(value = "/detailedaccountingQuery", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void getDetailedAccountByParam(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, String[]> detailedMap = request.getParameterMap();
		Integer firstResult = Integer.valueOf(request.getParameter("page"));
		Integer maxResults = Integer.valueOf(request.getParameter("rows"));
		JqGridPageView<DetailedAccount> detailedaccountListView = new JqGridPageView<DetailedAccount>();
		detailedaccountListView.setMaxResults(maxResults);
		List<DetailedAccount> detailedaccountList = detailedAccountService
				.queryDetailedAccountList(detailedMap);
		// 分页查询
		List<DetailedAccount> detailedaccountShowList = detailedaccountList;
		if ((firstResult - 1) * maxResults + maxResults > detailedaccountList
				.size()) {
			detailedaccountShowList = detailedaccountList.subList(
					(firstResult - 1) * maxResults, detailedaccountList.size());

		} else {
			detailedaccountShowList = detailedaccountList.subList(
					(firstResult - 1) * maxResults, (firstResult - 1)
							* maxResults + maxResults);
		}

		detailedaccountListView.setRows(detailedaccountShowList);
		detailedaccountListView.setRecords(detailedaccountList.size());
		writeJSON(response, detailedaccountListView);

	}

	// 修改 复核状态
	@RequestMapping(value = "/detailedaccountingStatusModify", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void modifyDetailedAccountStatusByID(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		Map<String, String[]> rowInfoMap = request.getParameterMap();
		int selectedRowCount = Integer.parseInt(request
				.getParameter("selectedRowCount"));
		List<DetailedAccount> modifyIDs = new ArrayList<DetailedAccount>();

		StringBuffer str = new StringBuffer();
		for (int i = 0; i < selectedRowCount; i++) {

			str.append("operation[");
			str.append(i);
			str.append("][mismatchID]");

			for (String key : rowInfoMap.keySet()) {
				if (str.toString().equals(key)) {
					for (String value : rowInfoMap.get(key)) {
						DetailedAccount selectRow = new DetailedAccount();
						selectRow.setMismatchID(Long.parseLong(value));
						modifyIDs.add(selectRow);
					}
				}
			}
			str.delete(0, str.length());
		}

		DetailedAccount entity = new DetailedAccount();
		//获取当前用户的ID
		String userId = ((SysUser) request.getSession().getAttribute(SESSION_SYS_USER)).getId().toString();
		entity.setDetailAccountID(Long.parseLong(userId));
		Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sysDatetime = fmt.format(rightNow.getTime());   
		entity.setDetailAccountTime(sysDatetime);
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		detailedAccountService.modifyDetailedAccountStatusByCol(modifyIDs,entity);

		writeJSON(response, parameter);
	}

	// 修改 详情复核 投资人信息 修改 复核状态
	@RequestMapping(value = "/detailedaccountingModify", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void modifyDetailedAccountByID(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		String invID = request.getParameter("invID");
		String investor = request.getParameter("investor");
		String investorID = request.getParameter("investorID");
		if (StringUtils.isBlank(investor) || StringUtils.isBlank(investorID)) {
			response.setStatus(HttpServletResponse.SC_LENGTH_REQUIRED);
			result.put("message", "请填写完整信息");
			writeJSON(response, result);
		} else {
			DetailedAccount entity = new DetailedAccount();
			entity.setInvID(Long.parseLong(invID));
			entity.setInvestor(investor);
			entity.setInvestorID(investorID);
			doSave(entity, request, response);
		}
	}

	// 修改投资人信息
	@RequestMapping(value = "/modifyDetailedAccount", method = {
			RequestMethod.POST, RequestMethod.GET })
	public void doSave(DetailedAccount entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		//
		detailedAccountService.modifyDetailedAccountByEntity(entity);

		parameter.setSuccess(true);
		writeJSON(response, parameter);
	}

}
