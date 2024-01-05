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
     * This is a service function which calls the getById() method of the DepartmentDao.
     * @param id id of the department which needs to be fetched.
     * @return Department object of the requested department.
     */
    @Override
    public Department getDepartmentById(Long id) {
        return departmentDao.getById(id);
    }

    /**
     * This service function calls the save() method of the DepartmentDao and works to add details in the system.
     * @param department Department object containing details of the new department.
     */
    @Override
    public void addDepartment(Department department) {
        departmentDao.save(department);
    }

    /**
     * This service function calls the update() method of the DepartmentDao and works to update the details in the system.
     * @param id id of the department which needs to be updated.
     * @param department Department object containing the new details.
     */
    @Override
    public void updateDepartment(Long id, Department department) {
        departmentDao.update(id, department);
    }

    /**
     * This service function deletes the department from the system by calling the delete() method of the DepartmentDao.
     * @param id id of the department that needs to be deleted.
     */
    @Override
    public void deleteDepartment(Long id) {
        departmentDao.delete(id);
    }
}
