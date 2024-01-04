package com.example.employeedepartment.service.imp;

import java.sql.SQLException;

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
     * @param employee
     */
    @Override
    public void addEmployee(Employee employee) {
        try {
            employeeDao.save(employee);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
