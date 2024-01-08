package com.example.employeedepartment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.employeedepartment.model.Employee;
import com.example.employeedepartment.service.imp.EmployeeServiceImpl;


@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {
    @Autowired
    private final EmployeeServiceImpl employeeService;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * This GET API request gets all the employees and their details and sends the response to the client.
     * It has pagination which allows the API to fetch only 5 entries per page to reduce load.
     * @return ArrayList containing all the employees and their details.
     */
    @GetMapping(path = "/all")
    public @ResponseBody List<Employee> getAllEmployees(@RequestParam("page") int page, @RequestParam("size") int size) {
        return employeeService.getAllEmployees(page, size);
    }

    /**
     * This GET API request gets the details of the employee whose id has been passed as the parameter.
     * @param id id of the requested employee.
     * @return Employee object containing the necessary details.
     */
    @GetMapping(path="/{id}")
    public @ResponseBody Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    /**
     * This POST API request adds a new Employee to the system.
     * @param newEmployee This request body contains details of the new Employee. It must have name, role and departmentId.
     * @return A success message string.
     */
    @PostMapping(path = "/add")
    public @ResponseBody String createEmployee(@RequestBody Employee newEmployee) {
        employeeService.addEmployee(newEmployee);
        return "Employee added successfully";
    }

    /**
     * This PUT API request updates the employee details according to the input from the client.
     * @param id the id which is passed as a parameter in the API endpoint. Refers to the id in the Employee table in the database.
     * @param updatedEmployee Contains the updates required from the request body. Needs to have name, role and departmentId in the request.
     * @return A success message String
     */
    @PutMapping(path = "/{id}")
    public @ResponseBody String updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        employeeService.updateEmployee(id, updatedEmployee);
        return "Employee updated successfully";
    }

    /**
     * This API request to delete the specified employee from the database.
     * @param id - refers to the id in the Employee table in the database. Parameter in the API endpoint.
     * @return A success message string
     */
    @DeleteMapping(path = "/{id}")
    public @ResponseBody String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "Employee deleted successfully";
    }
}
