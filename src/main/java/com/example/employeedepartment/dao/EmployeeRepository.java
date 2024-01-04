package com.example.employeedepartment.dao;

import java.util.List;

import com.example.employeedepartment.model.Employee;

public interface EmployeeRepository {
    int save(Employee employee);

    List<Employee> getAll();

    Employee getById(long id);

    Employee update(long id, Employee employee);

    long delete(long id);
}
