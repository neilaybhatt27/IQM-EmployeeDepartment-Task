package com.example.employeedepartment.service.interfaces;

import java.util.List;

import com.example.employeedepartment.model.RequestDepartment;
import com.example.employeedepartment.model.ResponseDepartment;

public interface DepartmentService {
    List<ResponseDepartment> getAllDepartments(int page, int size, String sortField, String sortDirection, String searchTerm);

    ResponseDepartment getDepartmentById(Long id);

    void addDepartment(RequestDepartment requestDepartment);

    void updateDepartment(Long id, RequestDepartment requestDepartment);

    void deleteDepartment(Long id);
}
