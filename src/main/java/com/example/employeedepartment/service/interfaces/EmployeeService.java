package com.example.employeedepartment.service.interfaces;

import java.util.List;

import com.example.employeedepartment.model.Employee;

public interface EmployeeService {
    void addEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);
}
