package com.example.employeedepartment.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.employeedepartment.dao.EmployeeRepository;
import com.example.employeedepartment.model.Employee;

@Repository
public class EmployeeRepoImpl implements EmployeeRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * This function adds employee details to the database.
     *
     * @param employee - Employee object received from client side
     * @return newly added Employee
     */
    @Override
    public int save(Employee employee) {
        return jdbcTemplate.update("INSERT INTO Employee (name, role, departmentId) VALUES (?,?,?)", new Object[] {employee.getName(), employee.getRole(), employee.getDepartmentId()});
    }

    /**
     * This function returns all the employees
     * @return List of all employees found in the database
     */
    @Override
    public List<Employee> getAll() {
        return jdbcTemplate.query("SELECT * FROM Employee", new BeanPropertyRowMapper<Employee>(Employee.class));
    }

    /**
     * This function gets the specific employee by matching the id.
     * @param id - id of the requested employee
     * @return Employee object of the specified id.
     */
    @Override
    public Employee getById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM Employee WHERE id = ?", new BeanPropertyRowMapper<Employee>(Employee.class), id);
    }

    /**
     * This function updates the employee details of the specified employee in the database
     * @param id - id of the employee that needs to be updated
     * @param employee - Employee object with the updated details
     * @return updated Employee object
     */
    @Override
    public Employee update(long id, Employee employee) {
        return null;
    }

    /**
     * This function deletes the specified employee from the database
     * @param id - id of the employee that needs to be deleted
     * @return - id of the deleted employee
     */
    @Override
    public long delete(long id) {
        return 0;
    }
}
