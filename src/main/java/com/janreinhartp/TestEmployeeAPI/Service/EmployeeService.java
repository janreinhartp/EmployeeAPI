package com.janreinhartp.TestEmployeeAPI.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.janreinhartp.TestEmployeeAPI.DTO.EmployeeRequest;
import com.janreinhartp.TestEmployeeAPI.Entity.Employee;
import com.janreinhartp.TestEmployeeAPI.Exception.EmployeeException;
import com.janreinhartp.TestEmployeeAPI.Repository.EmployeeRepository;

@Service
public class EmployeeService {
	private static final Logger log =
			LoggerFactory.getLogger(EmployeeService.class);
	
	public static final int MINAGE = 18;
	public static final int MAXAGE = 65;
	public static final int MINCHARNAME = 2;
	public static final int MAXCHARNAME = 30;
	public static final int ZEROID = 0;
	
	@Autowired
	private EmployeeRepository repository;

	public Employee saveEmployee(EmployeeRequest employeeRequest) {
		Employee employee = Employee.build(0, employeeRequest.getName(),
				employeeRequest.getEmail(), employeeRequest.getNumber(),
				employeeRequest.getGender(), employeeRequest.getAge(),
				employeeRequest.getBirthday());
		
		Employee savedEmployee = repository.save(employee);

		log.info(savedEmployee.toString());
		return savedEmployee;
	}

	public List<Employee> getAllEmployee() {
		return repository.findAll();
	}

	public Optional<Employee> getEmployee(int id) {
		return repository.findById(id);
	}

	public String deleteEmployee(int id) {
		repository.deleteById(id);
		return "Employee with id " + id + " is successfully Deleted";
	}

	public Employee updateEmployee(Employee employeeRequestWithID) {

		return repository.save(employeeRequestWithID);

	}

	public EmployeeRequest validateAddRequest(
			EmployeeRequest employeeRequest) throws EmployeeException {

		// Name Validation
		// Not Empty
		if (employeeRequest.getName().isEmpty()) {
			throw new EmployeeException(EmployeeException.INVALID_NAME);
		}
		// More Than 3 Characters
		if (employeeRequest.getName().length() <= MINCHARNAME) {
			throw new EmployeeException(EmployeeException.SHORT_NAME);
		}
		// Less Than 30 Characters
		if (employeeRequest.getName().length() > MAXCHARNAME) {
			throw new EmployeeException(EmployeeException.LONG_NAME);
		}
		// A-Z a-z with whitespace
		if (!Pattern.matches("[a-zA-Z\\s]*", employeeRequest.getName())) {
			throw new EmployeeException(EmployeeException.INVALID_CHAR_NAME);
		}
		// check if only contains space
		if (Pattern.matches("[\\s]*", employeeRequest.getName())) {
			throw new EmployeeException(EmployeeException.INVALID_CHAR_NAME);
		}

		// Number Validation
		// Regex PH Mobile Number 0-9 and 11 Digits

		if (!Pattern.matches("[0-9]{11}", employeeRequest.getNumber())) {
			throw new EmployeeException(EmployeeException.INVALID_NUMBER);
		}

		// Gender Validation
		// Regex Male or Female
		if (!Pattern.matches("Male|Female", employeeRequest.getGender())) {
			throw new EmployeeException(EmployeeException.INVALID_GENDER);
		}

		// Age Validation
		// Age Must be 10 to 100
		if (!(employeeRequest.getAge() >= MINAGE &&
				employeeRequest.getAge() <= MAXAGE)) {
			throw new EmployeeException(EmployeeException.INVALID_AGE);
		}

		// Email Validation
		// Regex A-Z a-z 0-9 _ . - and with @ .
		if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", employeeRequest.getEmail())) {
			throw new EmployeeException(EmployeeException.INVALID_EMAIL);
		}

		return employeeRequest;
	}

	public Employee validateUpdateRequest(
			Employee employeeRequestWithID) throws EmployeeException {

		// Name Validation
		// Not Empty
		if (employeeRequestWithID.getId() == ZEROID) {
			throw new EmployeeException(EmployeeException.INVALID_NAME);
		}

		// Name Validation
		// Not Empty
		if (employeeRequestWithID.getName().isEmpty()) {
			throw new EmployeeException(EmployeeException.INVALID_NAME);
		}
		// More Than 3 Characters
		if (employeeRequestWithID.getName().length() <= MINCHARNAME) {
			throw new EmployeeException(EmployeeException.SHORT_NAME);
		}
		// Less Than 30 Characters
		if (employeeRequestWithID.getName().length() > MAXCHARNAME) {
			throw new EmployeeException(EmployeeException.LONG_NAME);
		}
		// A-Z a-z with whitespace
		if (!Pattern.matches("[a-zA-Z\\s]*", employeeRequestWithID.getName())) {
			throw new EmployeeException(EmployeeException.INVALID_CHAR_NAME);
		}
		// check if only contains space
		if (Pattern.matches("[\\s]*", employeeRequestWithID.getName())) {
			throw new EmployeeException(EmployeeException.INVALID_CHAR_NAME);
		}

		// Number Validation
		// Regex PH Mobile Number 0-9 and 11 Digits

		if (!Pattern.matches("[0-9]{11}", employeeRequestWithID.getNumber())) {
			throw new EmployeeException(EmployeeException.INVALID_NUMBER);
		}

		// Gender Validation
		// Regex Male or Female
		if (!Pattern.matches("Male|Female", employeeRequestWithID.getGender())) {
			throw new EmployeeException(EmployeeException.INVALID_GENDER);
		}

		// Age Validation
		// Age Must be 10 to 100
		if (!(employeeRequestWithID.getAge() >= MINAGE &&
				employeeRequestWithID.getAge() <= MAXAGE)) {
			throw new EmployeeException(EmployeeException.INVALID_AGE);
		}

		// Email Validation
		// Regex A-Z a-z 0-9 _ . - and with @ .
		if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$",
				employeeRequestWithID.getEmail()) ) {
			throw new EmployeeException(EmployeeException.INVALID_EMAIL);
		}

		return employeeRequestWithID;
	}

	public String resetDBS() {
		repository.deleteAll();
		return "All Data is Deleted";
	}
}
