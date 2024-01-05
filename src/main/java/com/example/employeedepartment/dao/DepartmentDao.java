package com.example.employeedepartment.dao;

import java.sql.Connection;
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
}
