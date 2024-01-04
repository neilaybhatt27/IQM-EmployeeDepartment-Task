package com.example.employeedepartment.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.employeedepartment.model.Department;

public interface DepartmentRepository extends CrudRepository<Department, Long>{
    
}
