package com.janreinhartp.TestEmployeeAPI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.janreinhartp.TestEmployeeAPI.DTO.EmployeeRequest;
import com.janreinhartp.TestEmployeeAPI.Entity.Employee;
import com.janreinhartp.TestEmployeeAPI.Service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // need this in Spring Boot test
class TestEmployeeApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private EmployeeService employeeService;

	@Autowired
	private ObjectMapper objectMapper;

	private Employee rein;
	private EmployeeRequest reinRequest;
	private EmployeeRequest reinRequestUpdate;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

	@BeforeEach
	void init() {
		LocalDate birthday = LocalDate.parse("02-10-1997", formatter);
		// For Add
		reinRequest = EmployeeRequest.build("Reinhart", "janreinhartp@gmail.com", "09367254168", "Male", 25, birthday);
		rein = Employee.build(0, reinRequest.getName(), reinRequest.getEmail(), reinRequest.getNumber(),
				reinRequest.getGender(), reinRequest.getAge(), reinRequest.getBirthday());

		// For Update
		reinRequestUpdate = EmployeeRequest.build("Reinhart Perez", "newemail@gmail.com", "09776771859", "Male", 25,
				birthday);
	}

	@Test
	@Transactional
	void shouldAddTheEmployee() throws Exception {

		MvcResult result = mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(reinRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();

		Employee bodyFromResponse = objectMapper.readValue(resultContent, Employee.class);

		assertThat(bodyFromResponse.getId()).isNotNull();
		assertThat(bodyFromResponse).usingRecursiveComparison().ignoringFields("id").isEqualTo(rein);
	}

	@Test
	@Transactional
	void shouldUpdateEmployee() throws Exception {

		MvcResult resultFromAdd = mockMvc.perform(post("/employee/add")
				.content(objectMapper.writeValueAsString(reinRequest)).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContentFromAdd = resultFromAdd.getResponse().getContentAsString();

		Employee bodyFromResponseFromAdd = objectMapper.readValue(resultContentFromAdd, Employee.class);

		MvcResult resultFromUpdate = mockMvc
				.perform(MockMvcRequestBuilders.put("/employee/update/{id}", bodyFromResponseFromAdd.getId())
						.content(objectMapper.writeValueAsString(reinRequestUpdate))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		String resultContentFromUpdate = resultFromUpdate.getResponse().getContentAsString();

		Employee bodyFromResponseFromUpdate = objectMapper.readValue(resultContentFromUpdate, Employee.class);

		assertThat(bodyFromResponseFromUpdate.getId()).isNotNull();
		assertThat(bodyFromResponseFromUpdate).usingRecursiveComparison().isNotEqualTo(bodyFromResponseFromAdd);
	}

	@Test
	@Transactional
	void shouldDeleteEmployee() throws Exception {

		MvcResult resultFromAdd = mockMvc.perform(post("/employee/add")
				.content(objectMapper.writeValueAsString(reinRequest)).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContentFromAdd = resultFromAdd.getResponse().getContentAsString();

		Employee bodyFromResponseFromAdd = objectMapper.readValue(resultContentFromAdd, Employee.class);

		MvcResult resultFromDelete = mockMvc
				.perform(MockMvcRequestBuilders.delete("/employee/delete/{id}", bodyFromResponseFromAdd.getId())
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		String resultContentFromDelete = resultFromDelete.getResponse().getContentAsString();

		assertEquals(resultContentFromDelete,
				"Employee with id " + bodyFromResponseFromAdd.getId() + " is successfully Deleted");
	}

	@Test
	@Transactional
	void shouldGetEmployeeByID() throws Exception {

		MvcResult resultFromAdd = mockMvc.perform(post("/employee/add")
				.content(objectMapper.writeValueAsString(reinRequest)).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContentFromAdd = resultFromAdd.getResponse().getContentAsString();

		Employee bodyFromResponseFromAdd = objectMapper.readValue(resultContentFromAdd, Employee.class);

		MvcResult resultFromGet = mockMvc.perform(MockMvcRequestBuilders
				.get("/employee/{id}", bodyFromResponseFromAdd.getId()).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		String resultContentFromGet = resultFromGet.getResponse().getContentAsString();
		Employee bodyFromResponseFromGet = objectMapper.readValue(resultContentFromGet, Employee.class);
		assertThat(bodyFromResponseFromAdd).usingRecursiveComparison().isEqualTo(bodyFromResponseFromGet);
	}

	@Test
	@Transactional
	void shouldGetAllEmployee() throws Exception {

		mockMvc.perform(post("/employee/add").content(objectMapper.writeValueAsString(reinRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();

		MvcResult resultFromGet = mockMvc
				.perform(MockMvcRequestBuilders.get("/employee/fetchAll").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

		String resultContentFromGet = resultFromGet.getResponse().getContentAsString();
		List<Employee> employeeList = objectMapper.readValue(resultContentFromGet, new TypeReference<List<Employee>>() {
		});

		assertThat(employeeList).isNotEmpty();
	}

}
