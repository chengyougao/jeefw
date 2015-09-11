package com.jeefw.service.sys;

import java.util.List;

import com.jeefw.model.sys.CapitalHistory;

import core.service.Service;

public interface CapitalHistoryService extends Service<CapitalHistory> {
	

	//查询历史资金信息
	List<CapitalHistory> queryCapitalHistoryList(String historyDate);

}
