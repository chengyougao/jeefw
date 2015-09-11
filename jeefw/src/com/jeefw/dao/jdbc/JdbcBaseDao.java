package com.jeefw.dao.jdbc;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbcBaseDao")
public class JdbcBaseDao {
	
	private JdbcTemplate jdbcTemplate;
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Resource(name = "jdbcTemplate")
	public void setJT(JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}

}
