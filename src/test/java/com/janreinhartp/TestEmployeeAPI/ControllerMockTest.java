package com.janreinhartp.TestEmployeeAPI;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.janreinhartp.TestEmployeeAPI.DTO.EmployeeRequest;
import com.janreinhartp.TestEmployeeAPI.Entity.Employee;
import com.janreinhartp.TestEmployeeAPI.Repository.EmployeeRepository;
import com.janreinhartp.TestEmployeeAPI.Service.EmployeeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // need this in Spring Boot test
class ControllerMockTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private EmployeeService employeeService;
	
	@MockBean
	private EmployeeRepository repo;
	
	
	@Autowired
	private ObjectMapper objectMapper;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	

	@BeforeEach
	void setUp() throws Exception {
		
	}
	
	@Test
	void shouldAddEmployee() throws Exception {
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		EmployeeRequest reinRequest =  EmployeeRequest.build("Reinhart", "janreinhartp@gmail.com", "09367254168", "Male", 25, birthday);
		Employee employeeToAdd = Employee.build(1, reinRequest.getName(), reinRequest.getEmail(), reinRequest.getNumber(),
				reinRequest.getGender(), reinRequest.getAge(), reinRequest.getBirthday());
		
		when(repo.save(any(Employee.class))).thenReturn(employeeToAdd);
		
		MvcResult result = mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(reinRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated()).andReturn();
		String resultContent = result.getResponse().getContentAsString();

		Employee bodyFromResponse = objectMapper.readValue(resultContent, Employee.class);

		assertThat(bodyFromResponse.getId()).isNotNull();
		assertThat(bodyFromResponse).usingRecursiveComparison().ignoringFields("id").isEqualTo(employeeToAdd);
		
	}

	@Test
	void shouldGetEmployeeByID() throws Exception {
		
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		EmployeeRequest reinRequest =  EmployeeRequest.build("Reinhart", "janreinhartp@gmail.com", "09367254168", "Male", 25, birthday);
		Employee employeeToAdd = Employee.build(1, reinRequest.getName(), reinRequest.getEmail(), reinRequest.getNumber(),
				reinRequest.getGender(), reinRequest.getAge(), reinRequest.getBirthday());

		
		Optional<Employee> employeeToReturn = Optional.ofNullable(employeeToAdd);
		
		when(repo.findById(anyInt())).thenReturn(employeeToReturn);
		
		//Optional<Employee> returned = service.getEmployee(1);
		
		MvcResult resultFromGet = mockMvc.perform(MockMvcRequestBuilders
				.get("/employee/{id}", 1).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		String resultContentFromGet = resultFromGet.getResponse().getContentAsString();
		Employee bodyFromResponseFromGet = objectMapper.readValue(resultContentFromGet, Employee.class);
		
		assertEquals(employeeToAdd.getId(), bodyFromResponseFromGet.getId());

	}
	
	@Test
	void shouldUpdateEmployee() throws Exception {

		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		EmployeeRequest reinRequest =  EmployeeRequest.build("Reinhart", "janreinhartp@gmail.com", "09367254168", "Male", 25, birthday);
		
		Employee EmployeeToUpdate = Employee.build(1, reinRequest.getName(), reinRequest.getEmail(), reinRequest.getNumber(),
				reinRequest.getGender(), reinRequest.getAge(), reinRequest.getBirthday());
		
		Employee UpdatedEmployee = Employee.build(1, "Reinhart Updated", reinRequest.getEmail(), reinRequest.getNumber(),
				reinRequest.getGender(), reinRequest.getAge(), reinRequest.getBirthday());
		
		when(repo.save(any(Employee.class))).thenReturn(UpdatedEmployee);
		
		
		MvcResult resultFromUpdate = mockMvc
				.perform(MockMvcRequestBuilders.put("/employee/update/{ID}", UpdatedEmployee.getId())
						.content(objectMapper.writeValueAsString(reinRequest))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		String resultContentFromUpdate = resultFromUpdate.getResponse().getContentAsString();

		Employee bodyFromResponseFromUpdate = objectMapper.readValue(resultContentFromUpdate, Employee.class);

		assertThat(bodyFromResponseFromUpdate.getId()).isNotNull();
		assertThat(bodyFromResponseFromUpdate).usingRecursiveComparison().isNotEqualTo(EmployeeToUpdate);
	}
	
	
	@Test
	void shouldDeleteEmployee() throws Exception {
		
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		EmployeeRequest reinRequest =  EmployeeRequest.build("Reinhart", "janreinhartp@gmail.com", "09367254168", "Male", 25, birthday);
		
		Employee EmployeeToDelete = Employee.build(1, reinRequest.getName(), reinRequest.getEmail(), reinRequest.getNumber(),
				reinRequest.getGender(), reinRequest.getAge(), reinRequest.getBirthday());
		
		 mockMvc.perform(MockMvcRequestBuilders.delete("/employee/delete/{id}", EmployeeToDelete.getId())
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		verify(repo, times(1)).deleteById(1);
	}
	
	@Test
	void shouldGetAllEmployee() throws Exception {
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		Employee employeeToA = Employee.build(1, "Ren", "abc@email.com", "09367254168",
				"Male", 25, birthday);
		Employee employeeToB = Employee.build(2, "Ren Ren", "abc@email.com", "09367254168",
				"Male", 25, birthday);
		
		List<Employee> listToReturn = new ArrayList<>();
		
		listToReturn.add(employeeToA);
		listToReturn.add(employeeToB);

		when(repo.findAll()).thenReturn(listToReturn);
	
		MvcResult resultFromGet = mockMvc
				.perform(MockMvcRequestBuilders.get("/employee/fetchAll").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		String resultContentFromGet = resultFromGet.getResponse().getContentAsString();
		List<Employee> employeeList = objectMapper.readValue(resultContentFromGet, new TypeReference<List<Employee>>() {
		});

		assertThat(employeeList).isNotEmpty();
	}
	
	
	@Test
	void shoudTestAddException() throws JsonProcessingException, Exception {
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		EmployeeRequest exceptionRequest = EmployeeRequest.build("", "newemail@gmail.com", "09776771859", "Male", 25,
			birthday);
		
		// Null Name
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();
		
		// Name Less Than 3 Char
		exceptionRequest.setName("aa");
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();

		// Name More Than 30 Char
		exceptionRequest.setName("asdasdasdasdasdasdasdasdasdasdasd");
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();
				
		// Name Invalid Char
		exceptionRequest.setName("Jan Reinhart 823");
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();
				
		// Name Whitespace Only
		exceptionRequest.setName("    ");
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();
				
		exceptionRequest.setName("Reinhart");
		
		// Number Validation 
		exceptionRequest.setNumber("0902531");
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();
		exceptionRequest.setNumber("09367254168");
		
		// Gender Validation
		exceptionRequest.setGender("femalee");
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();
		exceptionRequest.setGender("Male");
		
		// Age Validation
		exceptionRequest.setAge(10);
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();
		exceptionRequest.setAge(80);
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();
		exceptionRequest.setAge(25);
		
		// Email Validation
		exceptionRequest.setEmail("janreinhart*&@");
		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();
		exceptionRequest.setEmail("janreinhartp@gmail.com");
	}
	
	@Test
	void shoudTestUpdateException() throws JsonProcessingException, Exception {
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		EmployeeRequest exceptionRequest = EmployeeRequest.build("Jan Reinhart", "newemail@gmail.com", "09776771859", "Male", 25,
			birthday);
		Employee UpdateexceptionRequest = Employee.build(0, "Jan Reinhart", "newemail@gmail.com", "09776771859", "Male", 25,
				birthday);

		// 0 ID
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();
		
		UpdateexceptionRequest.setId(1);
		exceptionRequest.setName("");
		// Null Name
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();
		
		// Name Less Than 3 Char
		exceptionRequest.setName("aa");
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();

		// Name More Than 30 Char
		exceptionRequest.setName("asdasdasdasdasdasdasdasdasdasdasd");
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();
				
		// Name Invalid Char
		exceptionRequest.setName("Jan Reinhart 823");
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();
				
		// Name Whitespace Only
		exceptionRequest.setName("    ");
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();
				
		exceptionRequest.setName("Reinhart");
		
		// Number Validation 
		exceptionRequest.setNumber("0902531");
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();
		exceptionRequest.setNumber("09367254168");
		
		// Gender Validation
		exceptionRequest.setGender("femalee");
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();
		exceptionRequest.setGender("Male");
		
		// Age Validation
		exceptionRequest.setAge(10);
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();
		
		exceptionRequest.setAge(80);
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();
		exceptionRequest.setAge(25);
		
		// Email Validation
		exceptionRequest.setEmail("janreinhart*&@");
		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{id}", UpdateexceptionRequest.getId())
				.content(objectMapper.writeValueAsString(exceptionRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest()).andReturn();
		exceptionRequest.setEmail("janreinhartp@gmail.com");
	}
	
	@Test
	void shouldAllDeleteData() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/employee/reset").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		
		verify(repo, times(1)).deleteAll();
	}
	
	 @Test
   void main() {
      TestEmployeeApiApplication.main(new String[] {});
   }
	
	
}
