package pl.piomin.services.service.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.piomin.services.organization.client.DepartmentClient;
import pl.piomin.services.organization.client.EmployeeClient;
import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.repository.OrganizationRepository;

@RestController
@RequestMapping()
public class OrganizationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired
	OrganizationRepository repository;
	@Autowired
	OrganizationClient organizationClient;
	@Autowired
	EmployeeClient employeeClient;
	
	@GetMapping("/organization/{organizationId}")
    public List<Department> findByOrganization(@PathVariable("organizationId") String organizationId) {
        LOGGER.info("Department find: organizationId={}", organizationId);
        return organizationClient.findByOrganizationId(organizationId);
    }
}
