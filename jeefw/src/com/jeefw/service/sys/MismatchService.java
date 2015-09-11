package com.jeefw.service.sys;

import com.jeefw.model.sys.Mismatch;

import core.service.Service;



public interface MismatchService extends Service<Mismatch>  {
  	boolean addMismatch(int userId,String importDate);
  	double  queryDifferMoney(String importDate);
	public boolean updateMismatch(String investId);
}
