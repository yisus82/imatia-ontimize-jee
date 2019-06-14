package com.imatia.jee.bankmanager.common.base.services;

import java.util.List;
import java.util.Map;

import com.ontimize.db.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IBranchService {
	// ---- BRANCHES ----
	public EntityResult branchQuery(Map<String, Object> keysValues, List<String> attributes)
			throws OntimizeJEERuntimeException;

	public EntityResult branchInsert(Map<String, Object> attributes) throws OntimizeJEERuntimeException;

	public EntityResult branchUpdate(Map<String, Object> attributes, Map<String, Object> keyValues)
			throws OntimizeJEERuntimeException;

	public EntityResult branchDelete(Map<String, Object> keyValues) throws OntimizeJEERuntimeException;

	// ---- ACCOUNTS ----

	public EntityResult accountQuery(Map<String, Object> keysValues, List<String> attributes)
			throws OntimizeJEERuntimeException;

	public EntityResult accountInsert(Map<String, Object> attributes) throws OntimizeJEERuntimeException;

	public EntityResult accountUpdate(Map<String, Object> attributes, Map<String, Object> keyValues)
			throws OntimizeJEERuntimeException;

	public EntityResult accountDelete(Map<String, Object> keyValues) throws OntimizeJEERuntimeException;
}
