package com.imatia.jee.bankmanager.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imatia.jee.bankmanager.common.base.services.IEmployeeService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/employees")
public class EmployeeRestController extends ORestController<IEmployeeService> {
	@Autowired
	private IEmployeeService employeeService;

	@Override
	public IEmployeeService getService() {
		return this.employeeService;
	}
}