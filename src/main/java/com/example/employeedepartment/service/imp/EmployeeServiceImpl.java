package com.example.employeedepartment.service.imp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employeedepartment.dao.EmployeeDao;
import com.example.employeedepartment.model.Employee;
import com.example.employeedepartment.service.interfaces.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private final EmployeeDao employeeDao;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    /**
     * This function is a helper function, and it calls the save function of the EmployeeDao.
     *
     * @param employee Employee object
     */
    @Override
    public void addEmployee(Employee employee) {
        employeeDao.save(employee);
    }

    /**
     * This is a helper function which calls the getAll method of the employeeDao
     *
     * @param page          page number which is requested by the controller.
     * @param size          size of the data requested by the controller.
     * @param sortField     sorting parameter
     * @param sortDirection ascending or descending parameter.
     * @param searchTerm    String based ion which data is filtered.
     * @return ArrayList containing all the employees and their details.
     */
    @Override
    public List<Employee> getAllEmployees(int page, int size, String sortField, String sortDirection, String searchTerm) {
        if(page < 0){
            logger.error("Error in passing parameters.");
            throw new IllegalArgumentException("Invalid parameter: Page must be greater than or equal to 0");
        }
        if(size <= 0){
            logger.error("Error in passing parameters.");
            throw new IllegalArgumentException("Invalid parameter: Size must be greater than 0");
        }
        if(!sortField.equals("id") && !sortField.equals("name")){
            logger.error("Error in passing parameters.");
            throw new IllegalArgumentException("Invalid parameter: sortField must be either id or name");
        }
        if (!sortDirection.equals("asc") && !sortDirection.equals("desc") && !sortDirection.equals("ASC") && !sortDirection.equals("DESC")){
            logger.error("Error in passing parameters.");
            throw new IllegalArgumentException("Invalid parameter: sortDirection must be either of these. 1) asc/ASC, 2) desc/DESC");
        }
        logger.info("Processing getAllEmployees request with page={}, size={}, sortField={}, sortDirection={}, searchTerm={}", page, size, sortField, sortDirection, searchTerm);
        try {
            List<Employee> employees = employeeDao.getAll(page, size, sortField, sortDirection, searchTerm);
            logger.info("Finished processing getAllEmployees request with {} employees", employees.size());
            return employees;
        } catch (Exception e) {
            logger.error("Error occurred in database operation.");
            throw new RuntimeException("Some error occurred in the server.",e);
        }
    }

    /**
     * This is a helper function, and it calls the getById(Long id) method of the EmployeeDao.
     *
     * @param id id of the employee of which the details are requested.
     * @return Employee object containing the details of the requested employee id.
     */
    @Override
    public Employee getEmployeeById(Long id) {
        try {
            logger.info("Processing getEmployeeById request with id={}", id);
            Employee employee = employeeDao.getById(id);
            logger.info("Finished processing getEmployeeById request with employee id = {}", id);
            return employee;
        } catch (RuntimeException e) {
            logger.error("Error occurred in database operation.");
            throw e;
        }
    }

    /**
     * This is a helper function which calls the update method of employeeDao
     *
     * @param id       id of the employee whose details needs to be updated
     * @param employee Employee object containing the updated details
     */
    @Override
    public void updateEmployee(Long id, Employee employee) {
        employeeDao.update(id, employee);
    }

    /**
     * This is a helper function which calls the delete method of the employeeDao
     *
     * @param id id of the employee whose details need to be deleted from the database.
     */
    @Override
    public void deleteEmployee(Long id) {
        employeeDao.delete(id);
    }
}
