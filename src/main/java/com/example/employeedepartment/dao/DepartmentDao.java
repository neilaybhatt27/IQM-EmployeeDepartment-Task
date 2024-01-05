package com.example.employeedepartment.dao;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import com.example.employeedepartment.model.Department;

@Repository
public class DepartmentDao {
    private final DataSource dataSource;


    public DepartmentDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


}
