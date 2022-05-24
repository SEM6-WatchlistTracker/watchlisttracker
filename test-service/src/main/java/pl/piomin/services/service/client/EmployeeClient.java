package pl.piomin.services.service.client;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import pl.piomin.services.service.model.Employee;

@FeignClient(name = "employee")
public interface EmployeeClient {

	@GetMapping("/organization/{organizationId}")
	List<Employee> findByOrganization(@PathVariable("organizationId") String organizationId);
	
}
