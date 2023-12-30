package com.example.employeedepartment;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping(path="/{id}")
    public @ResponseBody Optional<Employee> getEmployeeByID(@PathVariable Long id) {
        return employeeRepository.findById(id);
    }
    
    @PostMapping(path = "/add")
    public @ResponseBody Employee createEmployee(@RequestBody Employee newEmployee) {
        // Employee newEmployee = new Employee();
        // newEmployee.setName(name);
        // newEmployee.setRole(role);
        // newEmployee.setDepartment(departmentId);
        return employeeRepository.save(newEmployee);
        // return newEmployee;
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        // Validate employee exists
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Validate department_id exists
        Optional<Department> optionalDepartment = departmentRepository.findById(updatedEmployee.getDepartmentId());
        if (optionalDepartment.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Update Employee entity from request
        Employee existingEmployee = employeeRepository.findById(id).get();
        existingEmployee.setId(id);
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setRole(updatedEmployee.getROle());
        existingEmployee.setDepartment(updatedEmployee.getDepartmentId());

        // Save and return the updated employee
        Employee newEmployee = employeeRepository.save(existingEmployee);
        return ResponseEntity.ok(newEmployee);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
