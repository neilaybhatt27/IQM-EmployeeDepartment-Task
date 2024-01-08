package com.example.employeedepartment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public void save(Employee employee) {
        try(Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee (name, role, department_id) VALUES (?, ?, ?)");
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getRole());
            pstmt.setLong(3, employee.getDepartmentId());
            pstmt.executeUpdate();
        } catch (SQLException ex){
            throw new RuntimeException();
        }
    }

    /**
     * This function returns all the employees with pagination to reduce load.
     * @return List of all employees found in the database
     */
    public List<Employee> getAll(int page, int size, String sortField, String sortDirection) {
        List<Employee> employees = new ArrayList<>();

        try(Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM employee ORDER BY " + sortField + " " + sortDirection + " " + "LIMIT ? OFFSET ?");
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()){
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("id"));
                employee.setName(resultSet.getString("name"));
                employee.setRole(resultSet.getString("role"));
                employee.setDepartmentId(resultSet.getLong("department_id"));
                employees.add(employee);
            }
        } catch (SQLException ex){
            throw new RuntimeException();
        }

        return employees;
    }

    /**
     * This function gets the specific employee by matching the id.
     * @param id - id of the requested employee
     * @return Employee object of the specified id.
     */
    public Employee getById(Long id) {
        Employee employee = new Employee();

        try(Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM employee WHERE id = ?");
            pstmt.setLong(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                employee.setId(resultSet.getLong("id"));
                employee.setName(resultSet.getString("name"));
                employee.setRole(resultSet.getString("role"));
                employee.setRole(resultSet.getString("role"));
                employee.setDepartmentId(resultSet.getLong("department_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employee;
    }

    /**
     * This function updates the employee details of the specified employee in the database
     * @param id id of the employee that needs to be updated
     * @param employee Employee object with the updated details
     */
    public void update(long id, Employee employee) {
        try(Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE employee SET name = ?, role = ?, department_id = ? WHERE id = ?");
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getRole());
            pstmt.setLong(3, employee.getDepartmentId());
            pstmt.setLong(4, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This function deletes the specified employee from the database
     * @param id - id of the employee that needs to be deleted
     */
    public void delete(long id) {
        try(Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM employee WHERE id = ?");
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
