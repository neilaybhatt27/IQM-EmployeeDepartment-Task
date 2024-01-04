package com.example.employeedepartment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import com.example.employeedepartment.model.Employee;

@Repository
public class EmployeeDao {
    private final DataSource dataSource;

    public EmployeeDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    /**
     * This function adds employee details to the database.
     *
     * @param employee - Employee object received from client side
     */
    public void save(Employee employee) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee (name, role, department_id) VALUES (?, ?, ?)");
        pstmt.setString(1, employee.getName());
        pstmt.setString(2, employee.getRole());
        pstmt.setLong(3, employee.getDepartmentId());
        pstmt.executeUpdate();
    }

    /**
     * This function returns all the employees
     * @return List of all employees found in the database
     */
    public List<Employee> getAll() {
//        return jdbcTemplate.query("SELECT * FROM Employee", new BeanPropertyRowMapper<Employee>(Employee.class));
        return null;
    }

    /**
     * This function gets the specific employee by matching the id.
     * @param id - id of the requested employee
     * @return Employee object of the specified id.
     */
    public Employee getById(long id) {
//        return jdbcTemplate.queryForObject("SELECT * FROM Employee WHERE id = ?", new BeanPropertyRowMapper<Employee>(Employee.class), id);
        return null;
    }

    /**
     * This function updates the employee details of the specified employee in the database
     * @param id - id of the employee that needs to be updated
     * @param employee - Employee object with the updated details
     * @return updated Employee object
     */
    public Employee update(long id, Employee employee) {
        return null;
    }

    /**
     * This function deletes the specified employee from the database
     * @param id - id of the employee that needs to be deleted
     * @return - id of the deleted employee
     */
    public long delete(long id) {
        return 0;
    }
}
