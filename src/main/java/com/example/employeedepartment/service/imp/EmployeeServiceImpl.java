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
     * @return
     */
    @Override
    public List<Employee> getAllEmployees() {
        return employeeDao.getAll();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Employee getEmployeeById(Long id) {
        return employeeDao.getById(id);
    }


}
