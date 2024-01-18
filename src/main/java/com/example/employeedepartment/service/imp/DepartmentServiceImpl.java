package com.example.employeedepartment.service.imp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employeedepartment.dao.DepartmentDao;
import com.example.employeedepartment.model.Department;
import com.example.employeedepartment.model.Employee;
import com.example.employeedepartment.service.interfaces.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentDao departmentDao;

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    /**
     * This is a helper function which calls the getAll method of the DepartmentDao
     *
     * @param page          page number which is requested by the controller.
     * @param size          size of the data requested by the controller.
     * @param sortField     sorting parameter
     * @param sortDirection ascending or descending parameter.
     * @param searchTerm    String based ion which data is filtered.
     * @return ArrayList containing all the departments and their details.
     */
    @Override
    public List<Department> getAllDepartments(int page, int size, String sortField, String sortDirection, String searchTerm) {
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
        logger.info("Processing getAllDepartments request with page={}, size={}, sortField={}, sortDirection={}, searchTerm={}", page, size, sortField, sortDirection, searchTerm);
        try {
            List<Department> departments = departmentDao.getAll(page, size, sortField, sortDirection, searchTerm);
            logger.info("Finished processing getAllDepartments request with {} departments", departments.size());
            return departments;
        } catch (Exception e) {
            logger.error("Error occurred in database operation.");
            throw new RuntimeException("Some error occurred in the server.",e);
        }    }

    /**
     * This is a service function which calls the getById() method of the DepartmentDao.
     * @param id id of the department which needs to be fetched.
     * @return Department object of the requested department.
     */
    @Override
    public Department getDepartmentById(Long id) {
        try {
            logger.info("Processing getDepartmentById request with id={}", id);
            Department department = departmentDao.getById(id);
            logger.info("Finished processing getDepartmentById request with department id = {}", id);
            return department;
        } catch (RuntimeException e) {
            logger.error("Error occurred in database operation.");
            throw e;
        }
    }

    /**
     * This service function calls the save() method of the DepartmentDao and works to add details in the system.
     * @param department Department object containing details of the new department such as name, region id and start date.
     */
    @Override
    public void addDepartment(Department department) {
        try {
            logger.info("Processing addDepartment request with department name = {}, region id = {} and start date = {}", department.getName(), department.getRegId(), department.getDeptStartDate());
            departmentDao.save(department);
            logger.info("Finished processing addDepartment successfully.");

        } catch (Exception e){
            logger.error("Error occurred in database operation.");
            throw new RuntimeException("Some error occurred in the server.", e);
        }
    }

    /**
     * This service function calls the update() method of the DepartmentDao and works to update the details in the system.
     * @param id id of the department which needs to be updated.
     * @param department Department object containing the new details.
     */
    @Override
    public void updateDepartment(Long id, Department department) {
        departmentDao.update(id, department);
    }

    /**
     * This service function deletes the department from the system by calling the delete() method of the DepartmentDao.
     * @param id id of the department that needs to be deleted.
     */
    @Override
    public void deleteDepartment(Long id) {
        departmentDao.delete(id);
    }
}
