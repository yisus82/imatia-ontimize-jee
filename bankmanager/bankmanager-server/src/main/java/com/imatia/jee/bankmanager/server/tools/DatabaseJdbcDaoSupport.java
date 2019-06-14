package com.imatia.jee.bankmanager.server.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;
import javax.xml.bind.JAXB;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

import com.imatia.jee.bankmanager.server.dao.RepositoryDao;
import com.ontimize.db.EntityResult;
import com.ontimize.db.handler.SQLStatementHandler;
import com.ontimize.jee.common.naming.I18NNaming;
import com.ontimize.jee.common.tools.CheckingTools;
import com.ontimize.jee.common.tools.streamfilter.ReplaceTokensFilterReader;
import com.ontimize.jee.server.dao.common.INameConverter;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import com.ontimize.jee.server.dao.jdbc.setup.JdbcEntitySetupType;
import com.ontimize.jee.server.dao.jdbc.setup.QueryType;

@Repository
@Lazy
public class DatabaseJdbcDaoSupport extends OntimizeJdbcDaoSupport implements BeanNameAware{
	@Autowired
	protected RepositoryDao repositoryDao;
	
	protected String beanName;
	
	public DatabaseJdbcDaoSupport() {
		System.out.println("DatabaseJdbcDaoSupport");
	}
	
	
	protected InputStream getInputStream(String name) throws Exception{
		Map<String, Object> keysValues = new HashMap<String, Object>();
		keysValues.put(RepositoryDao.ATTR_NAME, name);
		List<String> attributes = new ArrayList<String>();
		attributes.add(RepositoryDao.ATTR_NAME);
		attributes.add(RepositoryDao.ATTR_DEFINITION);
		
		EntityResult repositories = repositoryDao.query(keysValues, attributes, null, OntimizeJdbcDaoSupport.DEFAULT_QUERY_KEY);
		
		if (repositories.calculateRecordNumber()==1 && repositories.containsKey(RepositoryDao.ATTR_DEFINITION)){
			String definition = (String)((List)repositories.get(RepositoryDao.ATTR_DEFINITION)).get(0);
			ByteArrayInputStream input = new ByteArrayInputStream(definition.getBytes());
			return input;
		}else{
			throw new Exception("No existe la definici�n para ese bean");
		}
	}
	
	protected void loadConfigurationFile(final String path, final String pathToPlaceHolder) throws InvalidDataAccessApiUsageException {

		try{
			InputStream is = getInputStream(this.beanName);

			Reader reader = null;
			if (pathToPlaceHolder != null) {
				try (InputStream isPlaceHolder = Thread.currentThread().getContextClassLoader().getResourceAsStream(pathToPlaceHolder);) {
					final Properties prop = new Properties();
					if (isPlaceHolder != null) {
						prop.load(isPlaceHolder);
					}
					reader = new ReplaceTokensFilterReader(new InputStreamReader(is), new HashMap<String, String>((Map) prop));
				}
			} else {
				reader = new InputStreamReader(is);
			}

			final JdbcEntitySetupType setup = JAXB.unmarshal(reader, JdbcEntitySetupType.class);
			this.setTableName(setup.getTable());
			this.setSchemaName(setup.getSchema());
			this.setCatalogName(setup.getCatalog());
			this.setDeleteKeys(setup.getDeleteKeys().getColumn());
			this.setUpdateKeys(setup.getUpdateKeys().getColumn());
			if (setup.getQueries() != null) {
				for (final QueryType query : setup.getQueries().getQuery()) {//
					this.addQueryTemplateInformation(query.getId(), query.getSentence().getValue(), //
							query.getAmbiguousColumns() == null ? null : query.getAmbiguousColumns().getAmbiguousColumn(), //
									query.getFunctionColumns() == null ? null : query.getFunctionColumns().getFunctionColumn(), //
											query.getValidColumns() != null ? query.getValidColumns().getColumn() : new ArrayList<String>());
				}
			}
			this.setGeneratedKeyName(setup.getGeneratedKey());
			this.setDataSource((DataSource) this.getApplicationContext().getBean(setup.getDatasource()));
			this.setStatementHandler((SQLStatementHandler) this.getApplicationContext().getBean(setup.getSqlhandler()));
			final String nameConverter = setup.getNameconverter();
			if (!CheckingTools.isStringEmpty(nameConverter)) {
				this.setNameConverter((INameConverter)this.getApplicationContext().getBean(nameConverter));
			}
		} catch (final Exception e) {
				throw new InvalidDataAccessApiUsageException(I18NNaming.M_ERROR_LOADING_CONFIGURATION_FILE, e);
			}
	}


	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}
}
