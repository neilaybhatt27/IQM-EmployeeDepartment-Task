package com.example.employeedepartment.service.interfaces;

import java.util.List;

import com.example.employeedepartment.model.Department;

public interface DepartmentService {
    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    void addDepartment(Department department);

    void updateDepartment(Long id, Department department);

    void deleteDepartment(Long id);
}
