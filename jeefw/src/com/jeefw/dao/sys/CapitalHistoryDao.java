package com.jeefw.dao.sys;

import java.util.List;

import com.jeefw.model.sys.CapitalHistory;

import core.dao.Dao;

/**
 * 用户的数据持久层的接口
 */
public interface CapitalHistoryDao extends Dao<CapitalHistory> {

	@SuppressWarnings("rawtypes")
	List getInvestMisByCapitalHistoryId(Long CapitalHistoryId);

}
