package com.example.employeedepartment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import com.example.employeedepartment.model.Department;

@Repository
public class DepartmentDao {
    private final DataSource dataSource;

    public DepartmentDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * This function interacts with the database to fetch all the departments and its details.
     * @return List of departments
     */
    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();

        try(Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM department");

            while (resultSet.next()){
                Department department = new Department();
                department.setId(resultSet.getLong("id"));
                department.setName(resultSet.getString("name"));
                departments.add(department);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return departments;
    }

    /**
     * This method interacts with the database and fetches the department whose id matches the id in the input.
     * @param id id of the department which needs to be fetched.
     * @return Department object containing the requested department details.
     */
    public Department getById(Long id) {
        Department department = new Department();

        try(Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM department WHERE id = ?");
            pstmt.setLong(1, id);

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()){
                department.setId(resultSet.getLong("id"));
                department.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return department;
    }

    /**
     * This method adds a new department to the database.
     * @param department Department object containing the info to be added in the database.
     */
    public void save(Department department) {
        try(Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO department (name) VALUES (?)");
            pstmt.setString(1, department.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
