package com.imatia.jee.bankmanager.server.tools;

import org.apache.commons.dbcp.BasicDataSource;

public class HSQLDBDataSource extends BasicDataSource {

	private String dbname = null;
	private String urlPrefix = null;
	private String urlDir = null;
	private String driverClassName = null;

	public HSQLDBDataSource(String driverClassName, String urlPrefix, String urlDir, String dbName, String user, String password) {
		this.driverClassName = driverClassName;
		this.urlPrefix = urlPrefix;
		this.urlDir = urlDir;
		this.dbname = dbName;

		StringBuilder builder = new StringBuilder();
		builder.append(urlPrefix).append(":");
		builder.append(urlDir).append("/");
		builder.append(dbName);

		this.url = builder.toString();
		this.setUrl(this.url);
		this.setDriverClassName(this.driverClassName);
		this.setUsername(user);
		this.setPassword(password);

	}

}
