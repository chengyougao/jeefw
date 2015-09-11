package com.jeefw.service.sys.impl;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.jeefw.dao.jdbc.JdbcBaseDao;
import com.jeefw.model.sys.DetailedAccount;
import com.jeefw.service.sys.DetailedAccountService;

import core.service.BaseService;

/**
 * 用户的业务逻辑层的实现
 */
@Service
public class DetailedAccountServiceImpl extends BaseService<DetailedAccount>
		implements DetailedAccountService {
	@Resource
	private JdbcBaseDao jdbcBaseDao;

	@Override
	public List<DetailedAccount> queryDetailedAccountList(
			Map<String, String[]> detailedMap) {

		List<DetailedAccount> detailedAccount = null;
		JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
		String sql = "SELECT o.id,o.investProdut,o.investor,o.identityId,o.investTerm,o.investDate,b.status mailStatus,b.misAmount investMisMoney,l.loanAmount loanMisMoney ,b.id mismatchid,l.borrower ,b.status   FROM invest o  JOIN t_mismatch b ON b.invesId =o.id  JOIN  loan l ON l.id=b.loanId"
				+ "  WHERE 1=1";

		try {

			for (String key : detailedMap.keySet()) {
				if (key.equals("investor")) {
					for (String value : detailedMap.get(key)) {
						if (!"".equals(value) && value != null) {
							value = new String(value.getBytes("ISO-8859-1"),
									"utf-8");
							sql = sql + " AND o.investor LIKE '%" + value
									+ "%'";
						}
					}
				}
				if (key.equals("investorterm")) {
					for (String value : detailedMap.get(key)) {
						if (!"".equals(value) && value != null) {
							sql = sql + " AND o.investTerm =  '" + value + "'";
						}
					}
				}
				if (key.equals("loancompany")) {
					for (String value : detailedMap.get(key)) {
						if (!"".equals(value) && value != null) {
							value = new String(value.getBytes("ISO-8859-1"),
									"utf-8");
							sql = sql + " AND l.borrower LIKE '%" + value
									+ "%'";
						}
					}
				}
				if (key.equals("product")) {
					for (String value : detailedMap.get(key)) {
						if (!"".equals(value) && value != null) {
							value = new String(value.getBytes("ISO-8859-1"),
									"utf-8");
							sql = sql + " AND o.investProdut LIKE '%" + value
									+ "%'";
						}
					}
				}
				if (key.equals("status")) {
					for (String value : detailedMap.get(key)) {
						if (!"".equals(value) && value != null) {
							sql = sql + " AND l.status = " + value + "";
						}
					}
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		detailedAccount = jdbcTemplate.query(sql,
				new RowMapper<DetailedAccount>() {
					@Override
					public DetailedAccount mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						DetailedAccount detailedaccount = new DetailedAccount();
						detailedaccount.setInvID(Long.parseLong(rs
								.getString("id")));
						detailedaccount.setInvestor(rs.getString("investor"));
						detailedaccount.setProduct(rs.getString("investProdut"));
						detailedaccount.setInvestorterm(Integer.parseInt(rs
								.getString("investTerm")));
						detailedaccount.setInvestorID(rs
								.getString("identityId"));
						detailedaccount.setInvestMoney(Integer.parseInt(rs
								.getString("investMisMoney")));
						detailedaccount.setInvestDate(rs
								.getString("investDate"));
						// 增加借款人id
						detailedaccount.setMismatchID(Long.parseLong(rs
								.getString("mismatchid")));
						detailedaccount.setLoanMoney(Integer.parseInt(rs
								.getString("loanMisMoney")));
						detailedaccount.setLoancompany(rs.getString("borrower"));
						detailedaccount.setStatus(Integer.parseInt(rs
								.getString("status")));
						detailedaccount.setMailStatus(Integer.parseInt(rs
								.getString("mailStatus")));
						return detailedaccount;
					}
				});
		return detailedAccount;

	}

	@Override
	public void modifyDetailedAccountByEntity(DetailedAccount entity) {
		JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE invest o  SET  o.investor = '");
		sql.append(entity.getInvestor());
		sql.append("' ,o.identityId  = '");
		sql.append(entity.getInvestorID());
		sql.append("' WHERE o.id = '");
		sql.append(entity.getInvID() + "'");
		jdbcTemplate.update(sql.toString());
	}

	@Override
	public void modifyDetailedAccountStatusByCol(
			List<DetailedAccount> selectedRows,DetailedAccount detailaccountInfo) {
		JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
		StringBuffer sql = new StringBuffer();
		String operSql = "";
		sql.append("insert into t_mismatch(id,status,detailAccountID ,detailAccountTime)values");
		for (DetailedAccount entity : selectedRows) {
			sql.append("("+entity.getMismatchID()+","+1+","+detailaccountInfo.getDetailAccountID()+",'"+detailaccountInfo.getDetailAccountTime()+"'),");
		}
		operSql = sql.substring(0, sql.length() - 1);
		operSql=operSql+"  on duplicate key update detailAccountID=values(detailAccountID), detailAccountTime=values(detailAccountTime) ,status=values(status)";
		
		//System.out.println(operSql);
		jdbcTemplate.update(operSql);
		return;

	}
}
