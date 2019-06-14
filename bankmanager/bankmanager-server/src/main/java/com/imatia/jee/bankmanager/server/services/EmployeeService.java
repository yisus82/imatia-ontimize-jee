package com.imatia.jee.bankmanager.server.services;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imatia.jee.bankmanager.common.base.services.IEmployeeService;
import com.imatia.jee.bankmanager.server.dao.EmployeeDao;
import com.imatia.jee.bankmanager.server.dao.EmployeeTypeDao;
import com.ontimize.db.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeeService")
public class EmployeeService implements IEmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private EmployeeTypeDao employeeTypeDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	// ---- EMPLOYEES ----

	@Override
	public EntityResult employeeQuery(Map<String, Object> keysValues, List<String> attributes)
			throws OntimizeJEERuntimeException {

		return this.daoHelper.query(this.employeeDao, keysValues, attributes);

	}

	@Override
	public EntityResult employeeInsert(Map<String, Object> attributes) throws OntimizeJEERuntimeException {
		attributes = (Map<String, Object>) this.adaptBase64ImageField(EmployeeDao.ATTR_EMPLOYEEPHOTO, attributes);
		return this.daoHelper.insert(this.employeeDao, attributes);
	}

	@Override
	public EntityResult employeeUpdate(Map<String, Object> attributes, Map<String, Object> keyValues)
			throws OntimizeJEERuntimeException {
		attributes = (Map<String, Object>) this.adaptBase64ImageField(EmployeeDao.ATTR_EMPLOYEEPHOTO, attributes);
		return this.daoHelper.update(this.employeeDao, attributes, keyValues);
	}

	public Map<String, Object> adaptBase64ImageField(String field, Map<String, Object> attributes) {
		if (attributes.get(field) instanceof String) {
			String objectPhoto = (String) attributes.remove(field);
			Map<String, Object> mapAttr = new HashMap<>();
			mapAttr.putAll((Map<String, Object>) attributes);
			mapAttr.put(field, Base64.getDecoder().decode(objectPhoto));
			return mapAttr;
		} else {
			return attributes;
		}
	}

	@Override
	public EntityResult employeeDelete(Map<String, Object> keyValues) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.employeeDao, keyValues);
	}

	// ---- EMPLOYEESTYPE ----

	@Override
	public EntityResult employeeTypeQuery(Map<String, Object> keysValues, List<String> attributes)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.employeeTypeDao, keysValues, attributes);
	}

	@Override
	public EntityResult employeeTypeInsert(Map<String, Object> attributes) throws OntimizeJEERuntimeException {
		return this.daoHelper.insert(this.employeeTypeDao, attributes);
	}

	@Override
	public EntityResult employeeTypeUpdate(Map<String, Object> attributes, Map<String, Object> keyValues)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.employeeTypeDao, attributes, keyValues);
	}

	@Override
	public EntityResult employeeTypeDelete(Map<String, Object> keyValues) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.employeeTypeDao, keyValues);
	}
}