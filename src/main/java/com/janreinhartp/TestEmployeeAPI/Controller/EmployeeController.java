package com.janreinhartp.TestEmployeeAPI.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.janreinhartp.TestEmployeeAPI.DTO.EmployeeRequest;
import com.janreinhartp.TestEmployeeAPI.Entity.Employee;
import com.janreinhartp.TestEmployeeAPI.Exception.EmployeeException;
import com.janreinhartp.TestEmployeeAPI.Service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	private EmployeeService service;

	@PostMapping("/add")
	public Employee saveEmployee(@RequestBody EmployeeRequest employeeRequest) throws EmployeeException {
		return service.SaveEmployee(service.validateAddRequest(employeeRequest));
	}

	@GetMapping("/fetchAll")
	public ResponseEntity<List<Employee>> getAllEmployee() {
		return ResponseEntity.ok(service.GetAllEmployee());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Employee>> getEmployee(@PathVariable int id) {
		return ResponseEntity.ok(service.GetEmployee(id));
	}

	@PutMapping("/update/{ID}")
	public Employee updateEmployee(@PathVariable int ID, @RequestBody EmployeeRequest employeeRequest)
			throws EmployeeException {
		Employee toUpdate = Employee.build(ID, employeeRequest.getName(), employeeRequest.getEmail(),
				employeeRequest.getNumber(), employeeRequest.getGender(), employeeRequest.getAge(),
				employeeRequest.getBirthday());

		// return service.updateEmployee(service.validateUpdateRequest(toUpdate));

		return service.updateEmployee(service.validateUpdateRequest(toUpdate));
	}

	@DeleteMapping("/delete/{id}")
	public String deleteEmployee(@PathVariable int id) {
		try {
			return service.deleteEmployee(id);
		} catch (Exception e) {
			return e.toString();
		}
	}

	@GetMapping("/reset")
	public String resetDBS() {
		return service.resetDBS();
	}

}