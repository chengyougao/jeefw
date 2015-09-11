package com.jeefw.controller.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeefw.core.Constant;
import com.jeefw.core.JavaEEFrameworkBaseController;
import com.jeefw.model.sys.CapitalHistory;
import com.jeefw.model.sys.DetailedAccount;
import com.jeefw.service.sys.CapitalHistoryService;

import core.support.JqGridPageView;
import core.support.QueryResult;

/**
 * 附件的控制层
 */
@Controller
@RequestMapping("/sys/capitalhistory")
public class CapitalHistoryController extends
		JavaEEFrameworkBaseController<CapitalHistory> {
	private static final Log log = LogFactory
			.getLog(CapitalHistoryController.class);
	@Resource
	private CapitalHistoryService capitalHistoryService;

	@RequestMapping("/capitalhistoryQuery")
	@ResponseBody
	public void getCapitalHistoryByDate(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 查询日期
		String historyDate = request.getParameter("historyDate");

		Integer firstResult = Integer.valueOf(request.getParameter("page"));
		Integer maxResults = Integer.valueOf(request.getParameter("rows"));
		// CapitalHistory capitalhistory = new CapitalHistory();
		// capitalhistory.setFirstResult((firstResult - 1) * maxResults);
		// capitalhistory.setMaxResults(maxResults);
		JqGridPageView<CapitalHistory> capitalhistoryListView = new JqGridPageView<CapitalHistory>();
		capitalhistoryListView.setMaxResults(maxResults);
		List<CapitalHistory> capitalhistoryList = capitalHistoryService
				.queryCapitalHistoryList(historyDate);

		List<CapitalHistory> capitalhistoryShowList = capitalhistoryList;
		if ((firstResult - 1) * maxResults + maxResults > capitalhistoryList
				.size()) {
			capitalhistoryShowList = capitalhistoryList.subList(
					(firstResult - 1) * maxResults, capitalhistoryList.size());

		} else {
			capitalhistoryShowList = capitalhistoryList.subList(
					(firstResult - 1) * maxResults, (firstResult - 1)
							* maxResults + maxResults);
		}

		capitalhistoryListView.setRows(capitalhistoryShowList);
		capitalhistoryListView.setRecords(capitalhistoryList.size());
		writeJSON(response, capitalhistoryListView);

	}
}
