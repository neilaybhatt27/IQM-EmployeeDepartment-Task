package com.example.employeedepartment.service.interfaces;

import java.util.List;


import com.example.employeedepartment.model.RequestEmployee;
import com.example.employeedepartment.model.ResponseEmployee;

public interface EmployeeService {
    void addEmployee(RequestEmployee requestEmployee);

    List<ResponseEmployee> getAllEmployees(int page, int size, String sortField, String sortDirection, String searchTerm);

    ResponseEmployee getEmployeeById(Long id);

    void updateEmployee(Long id, RequestEmployee requestEmployee);

    void deleteEmployee(Long id);
}
