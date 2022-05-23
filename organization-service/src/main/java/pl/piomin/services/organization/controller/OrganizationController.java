package pl.piomin.services.organization.controller;

import org.bson.types.ObjectId;
import java.util.Optional;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.organization.client.DepartmentClient;
import pl.piomin.services.organization.client.EmployeeClient;
import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.repository.OrganizationRepository;

@RestController
public class OrganizationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired
	OrganizationRepository repository;
	@Autowired
	DepartmentClient departmentClient;
	@Autowired
	EmployeeClient employeeClient;
	
	@GetMapping()
	public List<Organization> findAll() {
		LOGGER.info("Organization find");
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public Organization findById(@PathVariable("id") ObjectId id) {
		LOGGER.info("Organization find pathvar: id={}", id);
		return repository.findById(id).get();
	}

	@GetMapping() // /organization?id={id}
	public Organization findById(@RequestParam("id") ObjectId id) {
		LOGGER.info("Organization find requestvar: id={}", id);
		return repository.findById(id).get();
	}

	@PostMapping
	public Organization add(@RequestBody Organization organization) {
		LOGGER.info("Organization add: {}", organization);
		return repository.save(organization);
	}
}
