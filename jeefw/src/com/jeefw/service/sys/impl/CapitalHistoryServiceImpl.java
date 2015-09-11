package com.jeefw.service.sys.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.jeefw.dao.jdbc.JdbcBaseDao;
import com.jeefw.model.sys.CapitalHistory;
import com.jeefw.service.sys.CapitalHistoryService;

import core.service.BaseService;

/**
 * 用户的业务逻辑层的实现
 */
@Service
public class CapitalHistoryServiceImpl extends BaseService<CapitalHistory>
		implements CapitalHistoryService {

	@Resource
	private JdbcBaseDao jdbcBaseDao;

	// 查询历史资金数据
	public List<CapitalHistory> queryCapitalHistoryList(String historyDate) {
		List<CapitalHistory> capitalHistory = null;
		JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
		String sql = "SELECT o.id,o.investdateEnd ,o.investProdut,o.investor,b.misAmount investMisMoney,l.loanAmount loanMisMoney ,l.loandateEnd FROM invest o   JOIN t_mismatch b ON b.invesId =o.id  JOIN  loan l ON l.id=b.loanId WHERE l.loandateEnd = '"
				+ historyDate + "'OR o.investdateEnd = '" + historyDate + "';";
		capitalHistory = jdbcTemplate.query(sql,
				new RowMapper<CapitalHistory>() {
					@Override
					public CapitalHistory mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						CapitalHistory capitalHistory = new CapitalHistory();
						capitalHistory.setInvestor(rs.getString("investor"));
						capitalHistory.setInveSum(Integer.parseInt(rs
								.getString("investMisMoney")));
						capitalHistory.setLoanSum(Integer.parseInt(rs
								.getString("loanMisMoney")));
						capitalHistory.setInveEndDate(rs
								.getString("investdateEnd"));
						capitalHistory.setLoadEndDate(rs
								.getString("loandateEnd"));
						capitalHistory.setInvestProdut(rs
								.getString("investProdut"));
						return capitalHistory;
					}
				});
		return capitalHistory;
	}

}
