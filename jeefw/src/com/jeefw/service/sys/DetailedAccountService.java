package com.jeefw.service.sys;

import java.util.List;
import java.util.Map;

import com.jeefw.model.sys.DetailedAccount;

import core.service.Service;

public interface DetailedAccountService extends Service<DetailedAccount>{

	List<DetailedAccount> queryDetailedAccountList(
			Map<String, String[]> detailedMap);
     //详情复核中 修改投资人信息
	void modifyDetailedAccountByEntity(DetailedAccount entity);
     //详情复核中  确认复核
	void modifyDetailedAccountStatusByCol(List<DetailedAccount> selectedRows,DetailedAccount detailaccountInfo);

}
