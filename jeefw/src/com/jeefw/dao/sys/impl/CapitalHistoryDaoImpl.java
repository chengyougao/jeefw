package com.jeefw.dao.sys.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeefw.dao.sys.CapitalHistoryDao;
import com.jeefw.model.sys.CapitalHistory;
import com.jeefw.model.sys.InvestMismatchDetail;

import core.dao.BaseDao;

/**
 * 用户的数据持久层的实现类
 */
@Repository
public class CapitalHistoryDaoImpl extends BaseDao<CapitalHistory> implements
		CapitalHistoryDao {

	public CapitalHistoryDaoImpl() {
		super(CapitalHistory.class);
	}

	@Override
	public List getInvestMisByCapitalHistoryId(Long CapitalHistoryId) {
		Query query = this
				.getSession()
				.createSQLQuery(
						"select id, misMoney from t_investmismatchdetail where invesId=:capitalHistoryId ");// where
																											// invesId=:capitalHistoryId
		query.setParameter("capitalHistoryId", CapitalHistoryId);
		List roleValue = query.list();
		InvestMismatchDetail investMismatchDetail = new InvestMismatchDetail();
		if (query.list().size() != 0 && query.list() != null) {
			for (int i = 0; i < query.list().size(); i++) {
				for (int j = 0; j < query.list().size(); j++) {
					// investMismatchDetail.setId(roleValue[i].);
					// investMismatchDetail.setMisMoney(misMoney);

				}
			}
		}
		// String roleValue = (String) query.uniqueResult() == null ? "" :
		// (String) query.uniqueResult();
		return roleValue;
	}

}
