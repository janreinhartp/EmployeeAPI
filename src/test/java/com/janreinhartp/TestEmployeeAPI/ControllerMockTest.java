package com.janreinhartp.TestEmployeeAPI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.janreinhartp.TestEmployeeAPI.Controller.EmployeeController;
import com.janreinhartp.TestEmployeeAPI.DTO.EmployeeRequest;
import com.janreinhartp.TestEmployeeAPI.Entity.Employee;
import com.janreinhartp.TestEmployeeAPI.Service.EmployeeService;


@WebMvcTest(EmployeeController.class)
class ControllerMockTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmployeeService service;
	
	
	@Autowired
	private ObjectMapper objectMapper;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	
	
	@Test
	void shouldAddEmployee() throws Exception {
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		EmployeeRequest reinRequest =  EmployeeRequest.build("Reinhart", "janreinhartp@gmail.com", "09367254168", "Male", 25, birthday);
		Employee employeeToAdd = Employee.build(0, reinRequest.getName(), reinRequest.getEmail(), reinRequest.getNumber(),
				reinRequest.getGender(), reinRequest.getAge(), reinRequest.getBirthday());
		
		when(service.saveEmployee(reinRequest)).thenReturn(employeeToAdd);
		
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(reinRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated()).andReturn();
		
	}

	@Test
	void shouldGetEmployeeByID() throws Exception {
		
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		EmployeeRequest reinRequest =  EmployeeRequest.build("Reinhart", "janreinhartp@gmail.com", "09367254168", "Male", 25, birthday);
		Employee employeeToAdd = Employee.build(1, reinRequest.getName(), reinRequest.getEmail(), reinRequest.getNumber(),
				reinRequest.getGender(), reinRequest.getAge(), reinRequest.getBirthday());

		
		Optional<Employee> employeeToReturn = Optional.ofNullable(employeeToAdd);
		when(service.getEmployee(1)).thenReturn(employeeToReturn);
		
		Optional<Employee> returned = service.getEmployee(1);
		
		MvcResult resultFromGet = mockMvc.perform(MockMvcRequestBuilders
				.get("/employee/{id}", 1).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		String resultContentFromGet = resultFromGet.getResponse().getContentAsString();
		Employee bodyFromResponseFromGet = objectMapper.readValue(resultContentFromGet, Employee.class);
		
		assertEquals(returned.get().getId(), bodyFromResponseFromGet.getId());

	}
	
	@Test
	void shouldGetEmployee() throws Exception {
		
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		Employee employeeToA = Employee.build(1, "Ren", "abc@email.com", "09367254168",
				"Male", 25, birthday);
		Employee employeeToB = Employee.build(2, "Ren Ren", "abc@email.com", "09367254168",
				"Male", 25, birthday);
		
		List<Employee> listOfEmployee = new ArrayList<>();
		
		listOfEmployee.add(employeeToA);
		listOfEmployee.add(employeeToB);
		
		when(service.getAllEmployee()).thenReturn(listOfEmployee);
		
		
		MvcResult resultFromGet = mockMvc.perform(MockMvcRequestBuilders
				.get("/employee/fetchAll", 1).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		
		String resultContentFromGet = resultFromGet.getResponse().getContentAsString();
		
		List<Employee> employeeList = objectMapper.readValue(resultContentFromGet, new TypeReference<List<Employee>>() {
		});
		
		assertThat(employeeList).isNotEmpty();
	}
	
	@Test
	void shouldUpdateEmployee() throws Exception {
		
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		EmployeeRequest reinRequest =  EmployeeRequest.build("Reinhart", "janreinhartp@gmail.com", "09367254168", "Male", 25, birthday);
		
		Employee employeeToA = Employee.build(1, "Ren", "abc@email.com", "09367254168",
				"Male", 25, birthday);
		Employee employeeToB = Employee.build(1, "Ren Ren", "abc@email.com", "09367254168",
				"Male", 25, birthday);
		
		when(service.updateEmployee(any(Employee.class))).thenReturn(employeeToB);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{ID}", employeeToA.getId())
						.content(objectMapper.writeValueAsString(reinRequest))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		assertThat(employeeToA).usingRecursiveComparison().isNotEqualTo(employeeToB);
	}
	
	@Test
	void shouldDeleteEmployee() throws Exception {
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		Employee employeeToA = Employee.build(1, "Ren", "abc@email.com", "09367254168",
				"Male", 25, birthday);
		
		when(service.deleteEmployee(1)).thenReturn("Employee with id " + employeeToA.getId() + " is successfully Deleted");
		
		MvcResult resultFromDelete = mockMvc
				.perform(MockMvcRequestBuilders.delete("/employee/delete/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		String resultContentFromDelete = resultFromDelete.getResponse().getContentAsString();

		assertEquals(resultContentFromDelete,
				"Employee with id " + employeeToA.getId() + " is successfully Deleted");

	}
}
