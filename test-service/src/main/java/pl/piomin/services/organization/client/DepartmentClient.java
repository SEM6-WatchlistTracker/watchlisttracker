package pl.piomin.services.organization.client;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import pl.piomin.services.organization.model.Department;

@FeignClient(name = "department")
public interface DepartmentClient {

	@GetMapping("/organization/{organizationId}")
	public List<Department> findByOrganization(@PathVariable("organizationId") ObjectId organizationId);
	
	@GetMapping("/organization/{organizationId}/with-employees")
	public List<Department> findByOrganizationWithEmployees(@PathVariable("organizationId") ObjectId organizationId);
	
}
