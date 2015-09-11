package com.jeefw.service.sys.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.jeefw.dao.jdbc.JdbcBaseDao;
import com.jeefw.model.sys.Invest;
import com.jeefw.model.sys.Loan;
import com.jeefw.model.sys.Mismatch;
import com.jeefw.service.sys.LoanService;
import com.jeefw.service.sys.MismatchService;

import core.service.BaseService;

/**
 * 
 * @author pfzeng 错配服务管理
 */
@Service
public class MismatchServiceImpl extends BaseService<Mismatch> implements
		MismatchService {
	@Resource
	private JdbcBaseDao jdbcBaseDao;
	@Resource
	private LoanService loanService;

	Map<Integer, List<Loan>> loanGroups = new LinkedHashMap<Integer, List<Loan>>();
	Map<Integer, List<Invest>> investGroups = new LinkedHashMap<Integer, List<Invest>>();
	List<Mismatch> misList = new ArrayList<Mismatch>();

	public List<Invest> getMeetInvestList(Integer key, boolean bool) {
		if (investGroups.get(key) == null && bool == true) {
			return getMeetInvestList(key - 1, bool);
		}
		return investGroups.get(key);
	}

	public Invest getMeetInvest(Integer key, boolean bool, Integer amount) {
		List<Invest> investList = getMeetInvestList(key, false);
		if (investList != null && investList.size() != 0) {
			for (Invest invest : investList) {
				if (amount == invest.getInvestAmount()) {
					return invest;
				}
			}
		}
		return null;
	}

	private void loadLoanGroup(List<Loan> list) {
		for (Loan loan : list) {
			if (loanGroups.containsKey(loan.getLoanDays())) { // 如果已经存在这个数组，就放在这里
				List<Loan> entiryList = loanGroups.get(loan.getLoanDays());
				entiryList.add(loan);
			} else {
				List<Loan> entiryList = new ArrayList<Loan>(); // 重新声明一个数组list
				entiryList.add(loan);
				loanGroups.put(loan.getLoanDays(), entiryList);
			}
		}
	}

	private void loadInvestGroup(List<Invest> list) {
		for (Invest invest : list) {
			if (investGroups.containsKey(invest.getInvestTerm())) { // 如果已经存在这个数组，就放在这里
				List<Invest> entiryList = investGroups.get(invest
						.getInvestTerm());
				entiryList.add(invest);
			} else {
				List<Invest> entiryList = new ArrayList<Invest>(); // 重新声明一个数组list
				entiryList.add(invest);
				investGroups.put(invest.getInvestTerm(), entiryList);
			}
		}
	}

	@Override
	public boolean addMismatch(int userId, String importDate) {
		double expireLoanMoney = 0;
		double expireInvestMoney = 0;
		// 1.查询当天资产未错配的列表
		List<Loan> listLoan = queryLoanList(importDate);
		// 2.查询当天投资列表未错配的列表
		List<Invest> listInvest = queryInvestList(importDate);
		// 3.查询当天投资金额与资产金额相差金额
		double differMoney = queryDifferMoney(importDate);
		// 3.1查询历史多余资产金额集合
		List<Loan> expireLoan = queryExpireLoan();
		if (expireLoan != null && expireLoan.size() != 0) {
			expireLoanMoney = expireLoan.get(0).getTotalAmount();
		}
		// 3.2查询重新历史可投资金额集合
		List<Invest> expireInvest = queryExpireInvest();
		if (expireInvest != null && expireInvest.size() != 0) {
			expireInvestMoney = expireInvest.get(0).getTotalAmount();
		}
		// 3.3计算投资与资产包相差金额
		double totalDifferMoney = expireInvestMoney - expireLoanMoney
				+ differMoney;
		if (totalDifferMoney < 0) {
			// 如果投资包小于资产包直接返回 当天错配数据失败
			return false;
		} else if (totalDifferMoney > 0) {
			// 如果大于0则把金额按照比例切割到每个投资包
			// 新增虚拟资产
			Loan xuniLoan = addVirtualLoan(userId, totalDifferMoney);
			// 按比率计算投资，错配虚拟资产
			misVirtualAssets(listInvest,
					queryInvesSumAndloanSum(importDate)[0], xuniLoan);
		}
		// 4.合并历史资金与当天未错配的资金
		listLoan.addAll(expireLoan);
		listInvest.addAll(expireInvest);
		// 5.按照期限分组
		loadLoanGroup(listLoan);
		loadInvestGroup(listInvest);
		// 错配期限相等情况，投资按比率错配
		// 6.开始计算错配
		Iterator<Integer> loans = loanGroups.keySet().iterator();
		while (loans.hasNext()) {
			Integer loanKey = loans.next();
			List<Loan> loanList = loanGroups.get(loanKey);
			List<Invest> invs = investGroups.get(loanKey);
			
			 if (null != invs) { 
				 //错配投资与资产期限相等
				 misAssets(loanList, invs);
			 }
			 
			Iterator<Loan> loanLists = loanList.iterator();
			while (loanLists.hasNext()) {
				Loan loan = loanLists.next();
				// 查询匹配投资
				Iterator<Integer> it = investGroups.keySet().iterator();
				boolean flag = true;
				while (it.hasNext() && flag) {
					Integer key = it.next();
					List<Invest> value = investGroups.get(key);
					Iterator<Invest> invests = value.iterator();
					while (invests.hasNext()) {
						Invest invest = invests.next();
						if (0 == loan.getLoanAmount()) {// 表示單筆資產錯配完成
							flag = false;
							break;
						}
						// 判断此比投资是否已经在与资产包期限相等的情况下错误完所有投资金额
						if (invest.getInvestAmount() == 0) {
							invests.remove();
							continue;
						}
						double rest = loan.getLoanAmount()
								- invest.getInvestAmount();
						Mismatch mis = null;
						if (0 == rest) {// 匹配相等情況
							mis = assemblyEntry(loan, invest,
									invest.getInvestAmount());
							invests.remove();
							misList.add(mis);
							flag = false;
							break;
						} else if (rest < 0) {// 匹配投資端多情況
							mis = assemblyEntry(loan, invest,
									loan.getLoanAmount());
							// 從新計算此投資者剩于可投資資金
							double investAmount = invest.getInvestAmount()
									- loan.getLoanAmount();
							invest.setInvestAmount(investAmount);
							invests.remove();
							misList.add(mis);
							flag = false;
							break;
						} else if (rest > 0) {// 匹配資產端多情況
							mis = assemblyEntry(loan, invest,
									invest.getInvestAmount());
							misList.add(mis);
							// 從新計算此資產剩于可投資資金
							loan.setLoanAmount(rest);
						}
					}
				}

			}
		}
		try {
			addMismatch(misList);
		} catch (CannotGetJdbcConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		misList.clear();
		loanGroups.clear();
		investGroups.clear();
		return true;
	}

	private Loan addVirtualLoan(int userId, double totalDifferMoney) {
		Loan xuniLoan = null;
		try {
			xuniLoan = new Loan();
			xuniLoan.setBorrower("虚拟借款");
			xuniLoan.setLoanTerm(0);
			xuniLoan.setLoanName("虚拟借款");
			xuniLoan.setLoanAmount(totalDifferMoney);
			xuniLoan.setId(loanService.addVirtualLoan(xuniLoan, userId));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return xuniLoan;
	}

	/***
	 * 错配虚拟资产
	 * 
	 * @param listInvest
	 * @param invesSum
	 * @param xuni
	 */
	private void misVirtualAssets(List<Invest> listInvest, double invesSum,
			Loan virtualLoan) {
		Iterator<Invest> iter = listInvest.iterator();
		int i = 0;
		double investSum = 0;
		while (iter.hasNext()) {
			Invest invest = iter.next();
			double amount = (invest.getInvestAmount() / invesSum)
					* invest.getInvestAmount();
			investSum += amount;
			i += 1;
			if (i == listInvest.size()) {
				amount = amount + (invesSum - investSum);
			}
			invest.setInvestAmount(invest.getInvestAmount() - amount);
			// 增加错配信息
			misList.add(assemblyEntry(virtualLoan, invest, amount));
		}
	}

	private void misAssets(List<Loan> loanList, List<Invest> invs) {
		// 计算资产包总金额
		double loanSum = 0;
		for (Loan loan : loanList) {
			loanSum += loan.getLoanAmount();
		}

		double invsSum = 0;
		for (Invest invest : invs) {
			invsSum += invest.getInvestAmount();
		}

		if (loanSum > invsSum) {// 期限相同资产包大于投资
			// 验证单个资产金额是否等于资产总金额
			abc(loanList, invs, loanSum, invsSum, true);
		} else {// 期限相同投资大于等于资产
			// 计算单个投资所占资产包比率
			abc(loanList, invs, loanSum, invsSum, false);
		}

	}

	private void abc(List<Loan> loanList, List<Invest> invs, double loanSum, double invsSum,
			boolean flag) {
		Iterator<Loan> loanLists = loanList.iterator();
		while (loanLists.hasNext()) {
			Loan loan = loanLists.next();
			double loanAmount = loan.getLoanAmount();
			if (flag) {
				loanAmount = (loan.getLoanAmount() / loanSum)
						* invsSum;
			}
			double loanInvestSum = 0;
			Iterator<Invest> invests = invs.iterator();
			while (invests.hasNext()) {
				Invest invest = invests.next();
				// TODO 有可能除 不尽
				double invAmount = (invest.getGuding() / invsSum) * loanAmount;
				loanInvestSum += invAmount; // 累加资产的投资金额
				// 计算此投资剩余可用金额
				invest.setInvestAmount(invest.getInvestAmount() - invAmount);
				Mismatch mis = assemblyEntry(loan, invest, invAmount);
				misList.add(mis);
			}
			loan.setLoanAmount(loan.getLoanAmount() - loanInvestSum);
		}
	}

	private Mismatch assemblyEntry(Loan loan, Invest invest, double amount) {
		Mismatch mis = new Mismatch();
		mis.setInvesDays(invest.getInvestTerm());
		mis.setInvesId(invest.getId());
		mis.setLoanId(loan.getId());
		mis.setLoanDays(loan.getLoanDays());
		mis.setMisAmount(amount);
		mis.setDate(invest.getInvestDate());
		// 計算過期時間
		return mis;
	}

	private void addMismatch(final List<Mismatch> list)
			throws CannotGetJdbcConnectionException, SQLException {
		JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
		String sqlJdbcString = "INSERT INTO t_mismatch (invesId,loanId,date,misAmount,invesDays,loanDays,eropDate,state,status) VALUES (?,?,?,?,?,?,?,0,0) ";
		jdbcTemplate.batchUpdate(sqlJdbcString,
				new BatchPreparedStatementSetter() {
					@Override
					public int getBatchSize() {
						return list.size();
					}

					@Override
					public void setValues(PreparedStatement preparedStatement,
							int i) throws SQLException {
						preparedStatement
								.setObject(1, list.get(i).getInvesId());
						preparedStatement.setObject(2, list.get(i).getLoanId());
						preparedStatement.setObject(3, list.get(i).getDate());
						preparedStatement.setObject(4, list.get(i)
								.getMisAmount());
						preparedStatement.setObject(5, list.get(i)
								.getInvesDays());
						preparedStatement.setObject(6, list.get(i)
								.getLoanDays());
						preparedStatement.setObject(7, list.get(i)
								.getEropDate());
					}
				});
	}

	private List<Loan> queryLoanList(final String importDate) {
		String sqlString = "SELECT id , loanName, loanTerm ,loanAmount,loanDays,loandateEnd ,0 AS totalAmount  FROM loan WHERE  STATUS = 1 AND  importDate = ? ORDER BY  loanDays DESC,loanAmount DESC";
		return loadLoanList(sqlString, new Object[] { importDate });
	}

	private List<Invest> queryInvestList(final String importDate) {
		String sqlString = " SELECT id,  investTerm,investAmount,investDateEnd, 0 as investAmount FROM invest WHERE STATUS =1 AND importDate = ?  ORDER BY investTerm DESC,investAmount DESC;";
		return (jdbcBaseDao.getJdbcTemplate().query(sqlString,
				new Object[] { importDate }, new RowMapper<Invest>() {
					public Invest mapRow(ResultSet resultSet, int arg1)
							throws SQLException {
						Invest invest = new Invest();
						invest.setId(resultSet.getLong("id"));
						invest.setInvestTerm(resultSet.getInt("investTerm"));
						invest.setInvestAmount(resultSet
								.getDouble("investAmount"));
						invest.setInvestDateEnd(resultSet
								.getString("investDateEnd"));
						invest.setGuding(resultSet
								.getDouble("investAmount"));
						return invest;
					}
				}));
	}

	/**
	 * 查询当天导入数据空缺
	 */
	public double[] queryInvesSumAndloanSum(String importDate) {
		String sqlString = "SELECT SUM(l.loanAmount) AS loanSum , (SELECT SUM(i.investAmount) FROM invest i WHERE i.status = 1 and i.importDate = ?) AS invesSum FROM loan l WHERE l.status = 1 AND l.importDate = ?";
		Map<String, Object> map = jdbcBaseDao.getJdbcTemplate().queryForMap(
				sqlString, new Object[] { importDate, importDate });

		BigDecimal invesSum = (BigDecimal) map.get("invesSum");
		BigDecimal loanSum = (BigDecimal) map.get("loanSum");
		return new double[] { invesSum.doubleValue(), loanSum.doubleValue() };
		// return invesSum - loanSum;
	}

	public double queryDifferMoney(String importDate) {
		double[] sum = queryInvesSumAndloanSum(importDate);
		return sum[0] - sum[1];
	}

	private List<Invest> loadInvestList(String sqlString, Object[] objs) {
		return (jdbcBaseDao.getJdbcTemplate().query(sqlString, objs,
				new RowMapper<Invest>() {
					public Invest mapRow(ResultSet resultSet, int arg1)
							throws SQLException {
						Invest invest = new Invest();
						invest.setId(resultSet.getLong("id"));
						invest.setInvestTerm(resultSet.getInt("investTerm"));
						invest.setInvestAmount(resultSet
								.getDouble("investAmount"));
						invest.setInvestDateEnd(resultSet
								.getString("investDateEnd"));
						invest.setTotalAmount(resultSet
								.getDouble("totalAmount"));
						return invest;
					}
				}));
	}

	private List<Loan> loadLoanList(String sqlString, Object[] objs) {
		return (jdbcBaseDao.getJdbcTemplate().query(sqlString, objs,
				new RowMapper<Loan>() {
					public Loan mapRow(ResultSet resultSet, int arg1)
							throws SQLException {
						Loan loan = new Loan();
						loan.setId(resultSet.getLong("id"));
						loan.setLoanName(resultSet.getString("loanName"));
						loan.setLoanAmount(resultSet.getInt("loanAmount"));
						loan.setLoanTerm(resultSet.getInt("loanTerm"));
						loan.setLoanDays(resultSet.getInt("loanDays"));
						loan.setLoanDateEnd(resultSet.getString("loandateEnd"));
						loan.setTotalAmount(resultSet.getLong("totalAmount"));
						return loan;
					}
				}));
	}

	/**
	 * 查询错配后投资过期的贷款信息
	 * 
	 * @return
	 */
	private List<Loan> queryExpireLoan() {
		StringBuilder sqlBuilder = new StringBuilder(
				"SELECT i.id AS id, i.loanTerm  as loanTerm ,m.misAmount AS loanAmount, (");
		sqlBuilder
				.append("SELECT SUM(misAmount)  FROM t_mismatch m LEFT JOIN  loan i ON i.id = m.loanId WHERE m.state = 2)   AS totalAmount,");
		sqlBuilder
				.append("m.loanDays-m.invesDays AS loanDays, m.eropDate AS  loandateEnd , ");
		sqlBuilder
				.append("i.loanName  AS loanName FROM t_mismatch m LEFT JOIN  loan i ON i.id = m.loanId WHERE m.state = 2");
		return loadLoanList(sqlBuilder.toString(), null);
	}

	/**
	 * 查询错配后贷款过期的投资信息
	 * 
	 * @return
	 */
	private List<Invest> queryExpireInvest() {
		StringBuilder sqlBuilder = new StringBuilder(
				"SELECT i.id AS id, m.misAmount AS investAmount, ");
		sqlBuilder
				.append("(SELECT SUM(misAmount)  FROM t_mismatch m LEFT JOIN  loan i ON i.id = m.loanId WHERE m.state = 1)  AS totalAmount,");
		sqlBuilder
				.append("m.invesDays-m.loanDays AS invesDays, m.eropDate AS  investDateEnd  ");
		sqlBuilder
				.append(" FROM t_mismatch m LEFT JOIN  invest i ON i.id = m.invesId WHERE m.state = 1");
		return loadInvestList(sqlBuilder.toString(), null);
	}

	@Override
	public boolean updateMismatch(final String investId) {
		Boolean flag = false;
		JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
		try {
			jdbcTemplate.update(  
					"update t_mismatch  set status = 2 where instr(concat(',',?,','),concat(',',invesId,','))",   
	                new PreparedStatementSetter(){  
	                    @Override  
	                    public void setValues(PreparedStatement ps) throws SQLException {  
	                        ps.setObject(1, investId);  
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
