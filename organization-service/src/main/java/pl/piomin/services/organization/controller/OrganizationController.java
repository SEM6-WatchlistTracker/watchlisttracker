package pl.piomin.services.organization.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.organization.model.Department;
import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.repository.OrganizationRepository;

@RestController
public class OrganizationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired OrganizationRepository repository;
	
	@GetMapping // organizations/
	public List<Organization> findAll() {
		LOGGER.info("Organization find");
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public Organization findById(@PathVariable("id") String id) {
		LOGGER.info("Organization find pathvar: id={}", id);
		return repository.findById(id).get();
	}

	@PostMapping
	public Organization add(@RequestBody Organization organization) {
		LOGGER.info("Organization add: {}", organization);
		List<Department> departments = new ArrayList<>();
		Department dept = new Department("deptName;");
		departments.add(dept);
		organization.setDepartments(departments);
		return repository.save(organization);
	}


	// test
	@GetMapping("/testing")
	public String testing() {
		LOGGER.info("Organization testing");
		return "org/testing";
	}
}
