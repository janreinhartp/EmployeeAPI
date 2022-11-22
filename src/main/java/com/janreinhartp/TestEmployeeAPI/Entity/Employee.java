package com.janreinhartp.TestEmployeeAPI.Entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("deprecation")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor

@Entity
@Table(name = "EMPLOYEE_TBL")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private String email;
	private String number;
	private String gender;
	private int age;

	@JsonFormat(pattern = "MM-dd-yyyy")
	private LocalDate birthday;
}
