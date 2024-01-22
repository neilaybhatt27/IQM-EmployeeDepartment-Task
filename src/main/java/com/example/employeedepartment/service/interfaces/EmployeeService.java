package com.example.employeedepartment.service.interfaces;

import java.util.List;


import com.example.employeedepartment.model.RequestEmployee;

public interface EmployeeService {
    void addEmployee(RequestEmployee requestEmployee);

    List<RequestEmployee> getAllEmployees(int page, int size, String sortField, String sortDirection, String searchTerm);

    RequestEmployee getEmployeeById(Long id);

    void updateEmployee(Long id, RequestEmployee requestEmployee);

    void deleteEmployee(Long id);
}
