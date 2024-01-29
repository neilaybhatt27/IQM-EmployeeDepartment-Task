package com.example.employeedepartment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.employeedepartment.model.RequestEmployee;
import com.example.employeedepartment.model.ResponseEmployee;
import com.example.employeedepartment.service.imp.EmployeeServiceImpl;

@Api(value = "Employee Management System", tags = "Operations pertaining to employee in Employee Management System")
@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {
    @Autowired
    private final EmployeeServiceImpl employeeService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * This GET API request gets all the employees and their details and sends the response to the client.
     * It has pagination which allows the API to fetch only certain entries per page to reduce load.
     * It also has sorting by name and sorting by id feature. (Optional)
     * It also has search term available so that you can get details according to the search term. (Optional)
     *
     * @param page          (Optional) Page input for the pagination. Default value is 0
     * @param size          (Optional) Size input for the pagination. Refers the size of data to be fetched per page. Default value is 5.
     * @param sortField     (Optional) parameter based on which sorting happens. Must be either name or id. Default value is id.
     * @param sortDirection (Optional) decides whether the sorting would be ascending or descending. Default value is asc.
     * @param searchTerm    (Optional) Fetches data according to the input. It would happen either by name or id.
     * @return ArrayList containing all the employees and their details.
     */
    @ApiOperation(value = "View a list of available employees which matches the criteria given in the parameters.", response = List.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved the list of employees."),
            @ApiResponse(code = 400, message = "Invalid Parameters."),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource."),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden."),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAllEmployees(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                                  @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                                  @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
                                                  @RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        logger.info("Received GET /employees/all request with page={}, size={}, sortField={}, sortDirection={}, searchTerm={}", page, size, sortField, sortDirection, searchTerm);
        try {
            List<ResponseEmployee> responseEmployees = employeeService.getAllEmployees(page, size, sortField, sortDirection, searchTerm);
            logger.info("Sent GET /employees/all response with {} employees", responseEmployees.size());
            return new ResponseEntity<>(responseEmployees, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            logger.error("Error in passing parameters.");
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ex) {
            logger.error("Some error occurred in the server");
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This GET API request gets the details of the employee whose id has been passed as the parameter.
     *
     * @param id id of the requested employee.
     * @return Employee object containing the necessary details.
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable Long id) {
        logger.info("Received GET /employees/{} request with employee id = {}", id, id);
        try {
            ResponseEmployee requestedResponseEmployee = employeeService.getEmployeeById(id);
            logger.info("Sent GET /employees/{} response with employee id = {}", id, id);
            return new ResponseEntity<>(requestedResponseEmployee, HttpStatus.OK);
        } catch (RuntimeException ex) {
            logger.error("Some error occurred in the server");
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This POST API request adds a new Employee to the system.
     *
     * @param newRequestEmployee This request body contains details of the new Employee. It must have name, role and departmentId.
     * @return A success message string.
     */
    @ApiOperation(value = "Add a new employee in the system", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully added a new employee."),
            @ApiResponse(code = 400, message = "Invalid data types in the input."),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource."),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden.")})
    @PostMapping(path = "/add")
    public @ResponseBody String createEmployee(@RequestBody RequestEmployee newRequestEmployee) {
        employeeService.addEmployee(newRequestEmployee);
        return "Employee added successfully";
    }

    /**
     * This PUT API request updates the employee details according to the input from the client.
     *
     * @param id              the id which is passed as a parameter in the API endpoint. Refers to the id in the Employee table in the database.
     * @param updatedRequestEmployee Contains the updates required from the request body. Needs to have name, role and departmentId in the request.
     * @return A success message String
     */
    @PutMapping(path = "/{id}")
    public @ResponseBody String updateEmployee(@PathVariable Long id, @RequestBody RequestEmployee updatedRequestEmployee) {
        employeeService.updateEmployee(id, updatedRequestEmployee);
        return "Employee updated successfully";
    }

    /**
     * This PATCH API adds or updates the end date of an employee in a department of a region.
     * @param empId id of the employee whose end date needs to be added or updated.
     * @param updates - Map containing region id, department id and end date as values.
     * @return Success message string
     */
    @PatchMapping(path = "/{empId}")
    public ResponseEntity<Object> updateEmployeeEndDate(@PathVariable Long empId, @RequestBody Map<String, Object> updates){
        logger.info("Received PATCH /employees/{} request with employee id = {}, department id = {}, regionId = {} and end date = {}", empId, empId, updates.get("deptId"), updates.get("regId"), updates.get("empEndDate"));
        try {
            employeeService.updateEmployeeEndDate(empId, updates);
            logger.info("Sent PATCH /departments/{} response with updated employee.", empId);
            return new ResponseEntity<>("Added or updated end date successfully", HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This API request to delete the specified employee from the database.
     *
     * @param id - refers to the id in the Employee table in the database. Parameter in the API endpoint.
     * @return A success message string
     */
    @DeleteMapping(path = "/{id}")
    public @ResponseBody String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "Employee deleted successfully";
    }
}
