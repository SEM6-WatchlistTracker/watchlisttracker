package pl.piomin.services.service.client;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import pl.piomin.services.service.model.Department;

@FeignClient(name = "organization")
public interface OrganizationClient {

	@GetMapping("/organization/{organizationId}")
	public List<Department> findByOrganization(@PathVariable("organizationId") String organizationId);
	
	@GetMapping("/organization/{organizationId}/with-employees")
	public List<Department> findByOrganizationWithEmployees(@PathVariable("organizationId") String organizationId);
	
}