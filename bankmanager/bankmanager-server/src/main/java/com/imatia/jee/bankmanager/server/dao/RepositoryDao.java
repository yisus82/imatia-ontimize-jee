package com.imatia.jee.bankmanager.server.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository(value = "RepositoryDao")
@Lazy
@ConfigurationFile(configurationFile = "base-dao/RepositoryDao.xml", configurationFilePlaceholder = "base-dao/placeholders.properties")
public class RepositoryDao extends OntimizeJdbcDaoSupport {
	
	public static final String ATTR_NAME = "REPOSITORY_NAME";
	public static final String ATTR_DEFINITION = "REPOSITORY_DEFINITION";
	
	public RepositoryDao() {
		super();
	}
}