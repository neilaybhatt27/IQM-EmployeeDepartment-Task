package com.example.employeedepartment;

import org.springframework.data.repository.CrudRepository;
import com.example.employeedepartment.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long>{
    
}
