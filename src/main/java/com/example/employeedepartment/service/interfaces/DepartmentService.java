package com.example.employeedepartment.service.interfaces;

import java.util.List;

import com.example.employeedepartment.model.Department;

public interface DepartmentService {
    List<Department> getAllDepartments(int page, int size, String sortField, String sortDirection, String searchTerm);

    Department getDepartmentById(Long id);

    void addDepartment(Department department);

    void updateDepartment(Long id, Department department);

    void deleteDepartment(Long id);
}
