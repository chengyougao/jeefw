package com.jeefw.service.sys;



import java.util.HashMap;

import com.jeefw.model.sys.Invest;

import core.service.Service;

/**
 * 投资的业务逻辑层的接口
 */
public interface InvestService extends Service<Invest> {

	 public HashMap<String, String> addInvestData(String filePath, String fileName,String userId);
	 public boolean updateInvest(String investId, String investName,String investIdentityId);

}
