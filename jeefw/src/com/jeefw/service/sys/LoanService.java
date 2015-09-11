package com.jeefw.service.sys;


import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

import com.jeefw.model.sys.Loan;

import core.service.Service;

/**
 * 借款的业务逻辑层的接口
 */
public interface LoanService extends Service<Loan> {

	public HashMap<String, String> addLoanData(String filePath, String fileName,String userId);
	public Long addVirtualLoan(final Loan loan ,final int userId)throws CannotGetJdbcConnectionException, SQLException;
}
