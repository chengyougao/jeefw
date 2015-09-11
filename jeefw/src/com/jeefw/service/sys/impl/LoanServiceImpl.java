package com.jeefw.service.sys.impl;


import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.jeefw.core.ExcelImport;
import com.jeefw.dao.jdbc.JdbcBaseDao;
import com.jeefw.model.sys.Loan;
import com.jeefw.service.sys.LoanService;

import core.service.BaseService;
import core.util.DateUtil;

/**
 * 借款的业务逻辑层的实现
 */
@Service
public class LoanServiceImpl extends BaseService<Loan> implements LoanService {
	@Resource
	private JdbcBaseDao jdbcBaseDao;
	
	@Override
	public HashMap<String, String> addLoanData(String filePath, String fileName,String userId) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> associations = new HashMap<String, String>();
		List<Loan> loanList = null;
		associations.put("客户姓名", "borrower");
		associations.put("借款人身份证号", "identityId");
		associations.put("借款人联系方式", "phone");
		associations.put("期限", "loanTerm");
		associations.put("合同金额", "loanAmount");
		associations.put("起息日", "loanDate");
		associations.put("借款用途", "loanPurpose");
		associations.put("产品类别", "loanName");
		ExcelImport excelImport = new ExcelImport(associations);
		excelImport.init(new File(filePath+fileName));
		loanList = excelImport.bindToModels(Loan.class, true);
		if(excelImport.hasError()){
			resultMap.put("errMess", excelImport.getError().toString());
			return resultMap;
		}
		try {
			addLoan(loanList,userId);
		} catch (CannotGetJdbcConnectionException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		return resultMap;
	}
	
	private  void addLoan(final List<Loan> list,final String userId)throws CannotGetJdbcConnectionException, SQLException {
		JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
		String sqlJdbcString = "INSERT INTO loan (borrower,identityId,phone,loanTerm,loanAmount,loanDate,loanPurpose,loanName,loanDays,importPeId,importDate,loanDateEnd) VALUES (?,?,?,?,?,?,?,?,?,?,curdate(),?) ";
		jdbcTemplate.batchUpdate(sqlJdbcString,
			new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return list.size();
				}
				@Override
				public void setValues(PreparedStatement preparedStatement,int i) throws SQLException {
					preparedStatement.setObject(1,list.get(i).getBorrower());
					preparedStatement.setObject(2, list.get(i).getIdentityId());
					preparedStatement.setObject(3, list.get(i).getPhone());
					preparedStatement.setObject(4, list.get(i).getLoanTerm());
					preparedStatement.setObject(5, list.get(i).getLoanAmount());
					preparedStatement.setObject(6, list.get(i).getLoanDate());
					preparedStatement.setObject(7, list.get(i).getLoanPurpose());
					preparedStatement.setObject(8, list.get(i).getLoanName());
					preparedStatement.setObject(9, list.get(i).getLoanTerm()*30);
					preparedStatement.setObject(10, userId);
					preparedStatement.setObject(11, DateUtil.subMonth(list.get(i).getLoanDate(), list.get(i).getLoanTerm()));
				}
			});
	}

	
	
	/**
	 * 新增虚拟借款
	 * 
	 * 增加并且获取主键
	 * @param sql sql语句
	 * @param params 参数
	 * @return 主键
	 */
	@Override
	public  Long addVirtualLoan(final Loan loan,final int userId) {
		final String sqlJdbcString = "INSERT INTO loan (borrower,loanTerm,loanAmount,loanName,importPeId,importDate) VALUES (?,?,?,?,?,curdate()) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcBaseDao.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sqlJdbcString,Statement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, loan.getBorrower());
	                ps.setObject(2, loan.getLoanTerm());
	                ps.setObject(3, loan.getLoanAmount());
	                ps.setObject(4, loan.getLoanName());
	                ps.setObject(5, userId);
                return ps;
			}
		}, keyHolder);
		
		Long generatedId = keyHolder.getKey().longValue(); 
		return generatedId;
	}
}