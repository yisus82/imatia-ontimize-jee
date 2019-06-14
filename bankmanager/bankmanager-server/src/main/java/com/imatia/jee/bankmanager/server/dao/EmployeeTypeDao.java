package com.imatia.jee.bankmanager.server.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository(value = "EmployeeTypeDao")
@Lazy
@ConfigurationFile(configurationFile = "base-dao/EmployeeTypeDao.xml", configurationFilePlaceholder = "base-dao/placeholders.properties")
public class EmployeeTypeDao extends OntimizeJdbcDaoSupport {
	public static final String ATTR_ID = "EMPLOYEETYPEID";
	public static final String ATTR_EMPLOYEETYPENAME = "EMPLOYEETYPENAME";
	public static final String ATTR_EMPLOYEETYPEDESC = "EMPLOYEETYPEDESC";

	public EmployeeTypeDao() {
		super();
	}
}