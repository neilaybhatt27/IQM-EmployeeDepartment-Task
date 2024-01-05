package com.example.employeedepartment.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employeedepartment.dao.DepartmentDao;
import com.example.employeedepartment.model.Department;
import com.example.employeedepartment.service.interfaces.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentDao departmentDao;

    /**
     * This is a service function which calls the update getAll() method of the DepartmentDao.
     * @return List of all departments and its details present in the database.
     */
    @Override
    public List<Department> getAllDepartments() {
        return departmentDao.getAll();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Department getDepartmentById(Long id) {
        return null;
    }

    /**
     * @param department
     */
    @Override
    public void addDepartment(Department department) {

    }

    /**
     * @param id
     * @param department
     */
    @Override
    public void updateDepartment(Long id, Department department) {

    }

    /**
     * @param id
     */
    @Override
    public void deleteDepartment(Long id) {

    }
}
