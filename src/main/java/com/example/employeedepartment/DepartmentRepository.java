package com.example.employeedepartment;

import org.springframework.data.repository.CrudRepository;
import com.example.employeedepartment.Department;

public interface DepartmentRepository extends CrudRepository<Department, Long>{
    
}
