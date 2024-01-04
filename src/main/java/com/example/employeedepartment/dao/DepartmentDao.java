package com.example.employeedepartment.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.employeedepartment.model.Department;

public interface DepartmentDao extends CrudRepository<Department, Long>{
    
}
