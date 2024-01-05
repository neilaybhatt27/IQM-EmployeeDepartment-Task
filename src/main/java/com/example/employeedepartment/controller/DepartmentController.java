package com.example.employeedepartment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.employeedepartment.service.imp.DepartmentServiceImpl;


@RestController
@RequestMapping(path = "/departments")
public class DepartmentController {
    @Autowired
    private DepartmentServiceImpl departmentService;

    @GetMapping(path = "/all")
    public @ResponseBody List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody Department getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping(path = "/add")
    public @ResponseBody String createDepartment(@RequestBody Department newDepartment) {
        departmentService.addDepartment(newDepartment);
        return "Department created successfully";
    }

    @PutMapping("/{id}")
    public @ResponseBody String updateDepartment(@PathVariable Long id, @RequestBody Department updatedDepartment) {
        departmentService.updateDepartment(id, updatedDepartment);
        return "Department updated successfully";
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "Department deleted successfully";
    }
}
