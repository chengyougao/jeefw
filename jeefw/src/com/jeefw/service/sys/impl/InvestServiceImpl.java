package com.jeefw.service.sys.impl;


import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeefw.core.ExcelImport;
import com.jeefw.dao.jdbc.JdbcBaseDao;
import com.jeefw.model.sys.Invest;
import com.jeefw.service.sys.InvestService;

import core.service.BaseService;
import core.util.DateUtil;

/**
 * 投资的业务逻辑层的实现
 */
@Service
@Transactional
public class InvestServiceImpl extends BaseService<Invest> implements InvestService {
	
	@Resource
	private JdbcBaseDao jdbcBaseDao;
	@Override
	public HashMap<String, String> addInvestData(String filePath, String fileName,String userId) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> associations = new HashMap<String, String>();
		List<Invest> investList = null;
		List<Invest> investCombinList = null;
		associations.put("客户姓名", "investor");
		associations.put("身份证号码", "identityId");
		associations.put("电子邮箱", "email");
		associations.put("期限（月/天）", "investTerm");
		associations.put("业绩", "investAmount");
		associations.put("确认收款日", "investDate");
		associations.put("产品", "investProdut");
		ExcelImport excelImport = new ExcelImport(associations);
		excelImport.init(new File(filePath+fileName));
		investList = excelImport.bindToModels(Invest.class, true);
		if(excelImport.hasError()){
			resultMap.put("errMess", excelImport.getError().toString());
			return resultMap;
		}
		try {
			addInvest(investList,userId);
		} catch (CannotGetJdbcConnectionException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		investCombinList = combinInvestList();
		deleteCombindedInvestLast(investCombinList);
		try {
			addInvest(investCombinList,userId);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return resultMap;
	}
	
	public void addInvest(final List<Invest> list,final String userId)throws CannotGetJdbcConnectionException, SQLException {
		JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
		String sqlJdbcString = "INSERT INTO invest (investor,identityId,email,investTerm,investAmount,investDate,investProdut,importPeId,importDate,investDateEnd) VALUES (?,?,?,?,?,?,?,?,curdate(),?) ";
		jdbcTemplate.batchUpdate(sqlJdbcString,
			new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return list.size(); 
				}
				@Override
				public void setValues(PreparedStatement preparedStatement,int i) throws SQLException {
					preparedStatement.setObject(1, list.get(i).getInvestor());
					preparedStatement.setObject(2, list.get(i).getIdentityId());
					preparedStatement.setObject(3, list.get(i).getEmail());
					preparedStatement.setObject(4, list.get(i).getInvestTerm());
					preparedStatement.setObject(5, list.get(i).getInvestAmount()*1000);
					preparedStatement.setObject(6, list.get(i).getInvestDate());
					preparedStatement.setObject(7, list.get(i).getInvestProdut());
					preparedStatement.setObject(8, userId);
					preparedStatement.setObject(9, DateUtil.subMonth(list.get(i).getInvestDate(), list.get(i).getInvestTerm()/30));
				}
			});
	}
	//合并重复项（investor，investterm，investdate，investProdut）
	public List<Invest> combinInvestList() { 
    	List<Invest> investList = null;
    	JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
    	String sql = "select investor,investTerm,identityId,email,investProdut,sum(investAmount) as investAmount,from_unixtime(unix_timestamp(investDate), '%Y-%m-%d') as investDate from invest group by investor,investTerm,investDate,investProdut having count(0) > 1";   
		investList = jdbcTemplate.query(sql,   
										new RowMapper<Invest>() {  
								            @Override  
								            public Invest mapRow(ResultSet rs, int rowNum) throws SQLException {  
								            	Invest invest = new Invest();   
								            	invest.setInvestor(rs.getString("investor"));
								            	invest.setIdentityId(rs.getString("identityId"));   
								            	invest.setEmail(rs.getString("email"));   
								            	invest.setInvestTerm(rs.getInt("investTerm"));   
								            	invest.setInvestAmount(rs.getInt("investAmount")/1000);
								            	invest.setInvestDate(rs.getString("investDate"));
								            	invest.setInvestProdut(rs.getString("investProdut"));
								                return invest;   
								            }  
							        	}
									);  
		return investList;
    }
    //删除被合并投资记录
    public void deleteCombindedInvestLast(final List<Invest> list) { 
    	JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
    	String sqlJdbcString = "delete t.* from invest t where  t.investor = ? and t.investTerm = ? and from_unixtime(unix_timestamp(t.investDate), '%Y-%m-%d') = ? and t.investProdut = ?";
		jdbcTemplate.batchUpdate(sqlJdbcString,
			new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return list.size();
				}
				@Override
				public void setValues(PreparedStatement preparedStatement,int i) throws SQLException {
					preparedStatement.setObject(1,list.get(i).getInvestor());
					preparedStatement.setObject(2, list.get(i).getInvestTerm());
					preparedStatement.setObject(3, list.get(i).getInvestDate());
					preparedStatement.setObject(4, list.get(i).getInvestProdut());
				}
			});
	}

	@Override
	public boolean updateInvest(final String investId, final String investName,final String investIdentityId) {
		Boolean flag = false;
		JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
		try {
			jdbcTemplate.update(  
	                "update invest set investor = ?,identityId = ? where id = ?",   
	                new PreparedStatementSetter(){  
	                    @Override  
	                    public void setValues(PreparedStatement ps) throws SQLException {  
	                        ps.setObject(1, investName);  
	                        ps.setObject(2, investIdentityId);  
	                        ps.setObject(3, investId);  
	                    }  
	                }  
	        );
			flag=true;
		} catch (Exception e) {
			flag = false ;
		}
    	return flag;
	}
    
}
