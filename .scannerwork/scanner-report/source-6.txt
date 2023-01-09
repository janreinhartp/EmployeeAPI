package com.janreinhartp.TestEmployeeAPI.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.janreinhartp.TestEmployeeAPI.Entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
