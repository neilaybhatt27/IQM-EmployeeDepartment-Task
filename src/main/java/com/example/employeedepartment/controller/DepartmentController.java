package com.example.employeedepartment.controller;

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

import com.example.employeedepartment.model.Department;
import com.example.employeedepartment.dao.DepartmentDao;


@RestController
@RequestMapping(path = "/departments")
public class DepartmentController {
    @Autowired
    private DepartmentDao departmentRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody Optional<Department> getDepartmentById(@PathVariable Long id) {
        return departmentRepository.findById(id);
    }

    @PostMapping(path = "/add")
    public @ResponseBody Department createDepartment(@RequestBody String name) {
        Department newDepartment = new Department();
        newDepartment.setName(name);
        departmentRepository.save(newDepartment);
        return newDepartment;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody String name) {
        if (!departmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Department existingDepartment = departmentRepository.findById(id).get();
        existingDepartment.setId(id);
        existingDepartment.setName(name);
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (!departmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        departmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
