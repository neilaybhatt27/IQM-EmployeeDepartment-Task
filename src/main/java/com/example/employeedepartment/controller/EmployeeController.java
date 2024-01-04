package com.example.employeedepartment.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.employeedepartment.model.Employee;
import com.example.employeedepartment.dao.DepartmentDao;
import com.example.employeedepartment.service.imp.EmployeeServiceImpl;


@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {
    @Autowired
    private EmployeeServiceImpl employeeService;
    @Autowired
    private DepartmentDao departmentDao;

    /**
     * This GET API request gets all the employees and their details and sends the response to the client.
     * @return ArrayList containing all the employees and their details.
     */
    @GetMapping(path = "/all")
    public @ResponseBody List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

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
     * This API request updates the employee details according to the input from the client.
     * @param id - the id which is passed as a parameter in the API endpoint. Refers to the id in the Employee table in the database.
     * @param updatedEmployee = Contains the updates required from the request body. Needs to have name, role and departmentId in the request.
     * @return ResponseEntity<Employee> - JSON object of the updated employee details along with status code.
     */
//    @PutMapping(path = "/{id}")
//    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
//        // Validate employee exists
//        if (!employeeDao.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        // Validate department_id exists
//        Optional<Department> optionalDepartment = departmentDao.findById(updatedEmployee.getDepartmentId());
//        if (!optionalDepartment.isPresent()) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        // Update Employee entity from request
//        Employee existingEmployee = employeeDao.findById(id).get();
//        existingEmployee.setId(id);
//        existingEmployee.setName(updatedEmployee.getName());
//        existingEmployee.setRole(updatedEmployee.getROle());
//        existingEmployee.setDepartment(updatedEmployee.getDepartmentId());
//
//        // Save and return the updated employee
//        Employee newEmployee = employeeDao.save(existingEmployee);
//        return ResponseEntity.ok(newEmployee);
//    }

    /**
     * This API request deleted the specified employee from the database.
     * @param id - refers to the id in the Employee table in the database. Parameter in the API endpoint.
     * @return ResponseEntity<Void> </>Void Response Entity
     */
//    @DeleteMapping(path = "/{id}")
//    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
//        if (!employeeDao.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        employeeDao.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
}
