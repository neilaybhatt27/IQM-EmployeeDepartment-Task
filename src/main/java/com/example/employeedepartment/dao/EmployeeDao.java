package com.example.employeedepartment.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.employeedepartment.model.Employee;

@Repository
public class EmployeeDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDao.class);

    public EmployeeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * This function adds employee details to the database.
     *
     * @param employee - Employee object received from client side
     */
    public void save(Employee employee) {
        String query = "INSERT INTO employee (name, role, department_id) VALUES (?, ?, ?)";
        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.update(query, employee.getName(), employee.getRole(), employee.getDepartmentId());
        } catch (Exception ex) {
            logger.error("Error executing SQL query", ex);
            throw ex;        }
    }

    /**
     * This function returns all the employees with pagination to reduce load.
     * Also contains sorting by name and id feature.
     * Also contains filtering by search term.
     *
     * @param page          page number for pagination.
     * @param size          number of rows to be sent for a single page.
     * @param sortField     Sort by id or by name.
     * @param sortDirection Ascending or descending sorting.
     * @param searchTerm    String used for filtering by name or id.
     * @return List of all employees found in the database
     */
    public List<Employee> getAll(int page, int size, String sortField, String sortDirection, String searchTerm) {
        String query = "SELECT * FROM employee";

        if (searchTerm != null) {
            query += " WHERE id = ? OR name LIKE ?";
        }

        query += " ORDER BY " + sortField + " " + sortDirection + " LIMIT ? OFFSET ?";

        BeanPropertyRowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);

        try {
            if (searchTerm != null) {
                logger.info("Executing SQL query: {}", query);
                return jdbcTemplate.query(query, rowMapper, searchTerm, searchTerm + "%", size, page * size);
            }
            else {
                logger.info("Executing SQL query: {}", query);
                return jdbcTemplate.query(query, rowMapper, size, page * size);
            }
        } catch (Exception ex) {
            logger.error("Error executing SQL query", ex);
            throw ex;
        }
    }

    /**
     * This function gets the specific employee by matching the id.
     *
     * @param id - id of the requested employee
     * @return Employee object of the specified id.
     */
    public Employee getById(Long id){
        String query = "SELECT * FROM employee WHERE id = ?";

        try {
            logger.info("Executing SQL query: {}", query);
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Employee.class), id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException("Employee not found with id: " + id);
        }
    }

    /**
     * This function updates the employee details of the specified employee in the database
     *
     * @param id       id of the employee that needs to be updated
     * @param employee Employee object with the updated details
     */
    public void update(long id, Employee employee) {
        String query = "UPDATE employee SET name = ?, role = ?, department_id = ? WHERE id = ?";
        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.update(query, employee.getName(), employee.getRole(), employee.getDepartmentId(), id);
        } catch (Exception e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException(e);
        }
    }

    /**
     * This function deletes the specified employee from the database
     *
     * @param id - id of the employee that needs to be deleted
     */
    public void delete(long id) {
        String query = "DELETE FROM employee WHERE id = ?";
        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.update(query, id);
        } catch (Exception e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException(e);
        }
    }
}
