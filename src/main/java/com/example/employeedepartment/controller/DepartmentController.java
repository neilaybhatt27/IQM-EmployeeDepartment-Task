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

    /**
     * This GET API request fetches all the entries of departments and sends it to the client.
     * @return List of all the departments and its details.
     */
    @GetMapping(path = "/all")
    public @ResponseBody List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    /**
     * This GET API request gets the department information specified by the id and sends it to the client.
     * @param id id of the department whose information needs to be fetched.
     * @return Department object of the requested department.
     */
    @GetMapping(path = "/{id}")
    public @ResponseBody Department getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    /**
     * This POST API request adds a new department in the system and sends a success message back to the client.
     * @param newDepartment Department object containing the details of new department. Must contain the name.
     * @return String containing the success message.
     */
    @PostMapping(path = "/add")
    public @ResponseBody String createDepartment(@RequestBody Department newDepartment) {
        departmentService.addDepartment(newDepartment);
        return "Department created successfully";
    }

    /**
     * This PUT API request updates the department details and sends back a success message to the client.
     * @param id id of the department that needs to be updated.
     * @param updatedDepartment Department object with the updated details.
     * @return String containing success message.
     */
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
