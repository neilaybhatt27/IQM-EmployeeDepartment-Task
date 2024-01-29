package com.example.employeedepartment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.employeedepartment.model.RequestDepartment;
import com.example.employeedepartment.model.ResponseDepartment;
import com.example.employeedepartment.service.imp.DepartmentServiceImpl;


@RestController
@RequestMapping(path = "/departments")
public class DepartmentController {
    @Autowired
    private final DepartmentServiceImpl departmentService;

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    public DepartmentController(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * This GET API request gets all the departments and their details and sends the response to the client.
     * It has pagination which allows the API to fetch only certain entries per page to reduce load.
     * It also has sorting by name and sorting by id feature. (Optional)
     * It also has search term available so that you can get details according to the search term. (Optional)
     *
     * @param page          (Optional) Page input for the pagination. Default value is 0
     * @param size          (Optional) Size input for the pagination. Refers the size of data to be fetched per page. Default value is 5.
     * @param sortField     (Optional) parameter based on which sorting happens. Must be either name or id. Default value is id.
     * @param sortDirection (Optional) decides whether the sorting would be ascending or descending. Default value is asc.
     * @param searchTerm    (Optional) Fetches data according to the input. It would happen either by name or id.
     * @return ArrayList containing all the departments and their details.
     */
    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAllDepartments(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                    @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                                    @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                                    @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
                                                    @RequestParam(value = "searchTerm", required = false) String searchTerm) {
        logger.info("Received GET /employees/all request with page={}, size={}, sortField={}, sortDirection={}, searchTerm={}", page, size, sortField, sortDirection, searchTerm);
        try {
            List<ResponseDepartment> responseDepartments = departmentService.getAllDepartments(page, size, sortField, sortDirection, searchTerm);
            logger.info("Sent GET /departments/all response with {} departments", responseDepartments.size());
            return new ResponseEntity<>(responseDepartments, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            logger.error("Error in passing parameters.");
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ex) {
            logger.error("Some error occurred in the server");
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This GET API request gets the details of the department whose id has been passed as the parameter.
     *
     * @param id id of the requested department.
     * @return Department object containing the necessary details.
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getDepartmentById(@PathVariable Long id) {
        logger.info("Received GET /employees/{} request with employee id = {}", id, id);
        try {
            ResponseDepartment requestedResponseDepartment = departmentService.getDepartmentById(id);
            logger.info("Sent GET /employees/{} response with employee id = {}", id, id);
            return new ResponseEntity<>(requestedResponseDepartment, HttpStatus.OK);
        } catch (RuntimeException ex) {
            logger.error("Some error occurred in the server");
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This POST API request adds a new Department to the system.
     *
     * @param newRequestDepartment This request body contains details of the new Department. It must have a name, region id and start date.
     * @return A success message string or error message.
     */
    @PostMapping(path = "/add")
    public ResponseEntity<Object> createDepartment(@RequestBody RequestDepartment newRequestDepartment) {
        try {
            departmentService.addDepartment(newRequestDepartment);
            logger.info("Sent POST /employees/add response with success message.");
            return new ResponseEntity<>("Department created successfully", HttpStatus.OK);
        } catch (RuntimeException e){
            logger.error("Some error occurred in the server");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This PUT API request updates the department details and sends back a success message to the client.
     * @param id id of the department that needs to be updated.
     * @param updatedRequestDepartment Department object with the updated details.
     * @return String containing success message.
     */
    @PutMapping("/{id}")
    public @ResponseBody String updateDepartment(@PathVariable Long id, @RequestBody RequestDepartment updatedRequestDepartment) {
        departmentService.updateDepartment(id, updatedRequestDepartment);
        return "Department updated successfully";
    }

    /**
     * This DELETE API request deletes the department from the system and sends back a success message to the client.
     * @param id id of the department that needs to be deleted.
     * @return String containing the success message.
     */
    @DeleteMapping("/{id}")
    public @ResponseBody String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "Department deleted successfully";
    }
}
