package com.imatia.jee.bankmanager.server.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imatia.jee.bankmanager.common.base.services.IBranchService;
import com.imatia.jee.bankmanager.server.dao.AccountDao;
import com.imatia.jee.bankmanager.server.dao.BranchDao;
import com.ontimize.db.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BranchService")
public class BranchService implements IBranchService {

	private static final Logger logger = LoggerFactory.getLogger(BranchService.class);
	@Autowired
	private BranchDao branchDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult branchQuery(Map<String, Object> keysValues, List<String> attributes)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.branchDao, keysValues, attributes);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public EntityResult branchInsert(Map<String, Object> attributes) throws OntimizeJEERuntimeException {
		return this.daoHelper.insert(this.branchDao, attributes);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public EntityResult branchUpdate(Map<String, Object> attributes, Map<String, Object> keyValues)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.branchDao, attributes, keyValues);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public EntityResult branchDelete(Map<String, Object> keyValues) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.branchDao, keyValues);
	}

	// ---- ACCOUNTS ----

	@Override
	public EntityResult accountQuery(Map<String, Object> keysValues, List<String> attributes)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.accountDao, keysValues, attributes);
	}

	@Override
	public EntityResult accountInsert(Map<String, Object> attributes) throws OntimizeJEERuntimeException {

		attributes.put(AccountDao.ATTR_ENTITYID, 2095);
		attributes.remove(AccountDao.ATTR_ANID);
		attributes.remove(AccountDao.ATTR_CDID);
		EntityResult toRet = this.daoHelper.insert(this.accountDao, attributes);

		if (toRet.getCode() == EntityResult.OPERATION_WRONG) {
			throw new OntimizeJEERuntimeException(toRet.getMessage());
		}

		StringBuilder builderfDC = new StringBuilder();
		builderfDC.append("00");
		builderfDC.append(attributes.get(AccountDao.ATTR_ENTITYID));
		builderfDC.append(attributes.get(AccountDao.ATTR_OFFICEID));

		Object accountKey = toRet.get(AccountDao.ATTR_ID);
		int accountNumber = this.accountDao.createAccountNumber((int) accountKey);
		int accountDC = this.accountDao.calculateCDID(builderfDC.toString(), accountNumber);

		Map<String, Object> mapAccountData = new HashMap<String, Object>();
		mapAccountData.put(AccountDao.ATTR_CDID, accountDC);
		mapAccountData.put(AccountDao.ATTR_ANID, accountNumber);

		Map<String, Object> mapAccountKey = new HashMap<String, Object>();
		mapAccountKey.put(AccountDao.ATTR_ID, accountKey);

		EntityResult accoutUpdate = this.daoHelper.update(this.accountDao, mapAccountData, mapAccountKey);

		if (accoutUpdate.getCode() == EntityResult.OPERATION_WRONG) {
			throw new OntimizeJEERuntimeException(accoutUpdate.getMessage());
		}

		return toRet;
	}

	@Override
	public EntityResult accountUpdate(Map<String, Object> attributes, Map<String, Object> keyValues)
			throws OntimizeJEERuntimeException {
		attributes.remove(AccountDao.ATTR_ENTITYID);
		attributes.remove(AccountDao.ATTR_OFFICEID);
		attributes.remove(AccountDao.ATTR_CDID);
		attributes.remove(AccountDao.ATTR_ANID);
		return this.daoHelper.update(this.accountDao, attributes, keyValues);
	}

	@Override
	public EntityResult accountDelete(Map<String, Object> keyValues) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.accountDao, keyValues);
	}
}
