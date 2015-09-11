package com.jeefw.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.jeefw.dao.sys.DepartmentDao;
import com.jeefw.model.sys.Department;

import core.dao.BaseDao;

/**
 * 部门的数据持久层的实现类
 */
@Repository
public class DepartmentDaoImpl extends BaseDao<Department> implements DepartmentDao {

	public DepartmentDaoImpl() {
		super(Department.class);
	}

}
