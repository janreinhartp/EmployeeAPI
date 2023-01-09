package com.janreinhartp.TestEmployeeAPI.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class EmployeeRequest {
	private String name;
	private String email;
	private String number;
	private String gender;
	private int age;

	@JsonFormat(pattern = "MM-dd-yyyy")
	private LocalDate birthday;
}
