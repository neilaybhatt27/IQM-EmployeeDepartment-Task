package com.example.employeedepartment.service.imp;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employeedepartment.dao.EmployeeDao;
import com.example.employeedepartment.model.Employee;
import com.example.employeedepartment.service.interfaces.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;

    /**
     * This function is a helper function, and it calls the save function of the EmployeeDao.
     * @param employee Employee object
     */
    @Override
    public void addEmployee(Employee employee) {
        employeeDao.save(employee);
    }

    /**
     * This is a helper function which calls the getAll method of the employeeDao
     * @return ArrayList containing all the employees and their details
     */
    @Override
    public List<Employee> getAllEmployees() {
        return employeeDao.getAll();
    }

    /**
     * This is a helper function, and it calls the getById(Long id) method of the EmployeeDao.
     * @param id id of the employee of which the details are requested.
     * @return Employee object containing the details of the requested employee id.
     */
    @Override
    public Employee getEmployeeById(Long id) {
        return employeeDao.getById(id);
    }

    /**
     * This is a helper function which calls the update method of employeeDao
     * @param id id of the employee whose details needs to be updated
     * @param employee Employee object containing the updated details
     */
    @Override
    public void updateEmployee(Long id, Employee employee) {
        employeeDao.update(id, employee);
    }


}
