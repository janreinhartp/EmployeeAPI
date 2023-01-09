package com.janreinhartp.TestEmployeeAPI;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
		
		when(service.SaveEmployee(reinRequest)).thenReturn(employeeToAdd);
		
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(reinRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated()).andReturn();
		
	}

	@Test
	void shouldGetEmployeeByID() {
//		
//		
//		mockMvc.perform(post("/employee/add")
//				.content(objectMapper.writeValueAsString(reinRequest)).contentType(MediaType.APPLICATION_JSON_VALUE))
//				.andExpect(status().isCreated()).andReturn();
//		
//		mockMvc.perform(MockMvcRequestBuilders
//				.get("/employee/{id}", bodyFromResponseFromAdd.getId()).contentType(MediaType.APPLICATION_JSON_VALUE))
//				.andExpect(status().isOk()).andReturn();

	}
	
	


}
