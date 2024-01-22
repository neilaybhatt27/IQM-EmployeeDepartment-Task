package com.example.employeedepartment.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.employeedepartment.model.RequestEmployee;

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
     * @param requestEmployee - Employee object received from client side
     */
    public void save(RequestEmployee requestEmployee) {
        String queryToCheckIfEmployeeExists = "SELECT * FROM employee WHERE email = ?";
        String queryToInsertInEmployeeTable = "INSERT INTO employee (name, role, email) VALUES (?, ?, ?)";
        String queryToInsertInEmployeeRegionDepartmentTable = "INSERT INTO employee_region_department (emp_id, reg_dept_id, emp_start_date, emp_end_date) VALUES (?, ?, ?, ?)";
        KeyHolder holder = new GeneratedKeyHolder();
        long employeeId;
        try {
            try {
                logger.info("Executing SQL query: {}", queryToCheckIfEmployeeExists);
                RequestEmployee existingRequestEmployee = jdbcTemplate.queryForObject(queryToCheckIfEmployeeExists, new BeanPropertyRowMapper<>(RequestEmployee.class), requestEmployee.getEmail());
                employeeId = existingRequestEmployee.getId();
            } catch (EmptyResultDataAccessException e){
                logger.info("Executing SQL query: {}", queryToInsertInEmployeeTable);
                jdbcTemplate.update(connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queryToInsertInEmployeeTable, new String[]{"id"});
                    preparedStatement.setString(1, requestEmployee.getName());
                    preparedStatement.setString(2, requestEmployee.getRole());
                    return preparedStatement;
                }, holder);
                employeeId = holder.getKey().longValue();
            }
            logger.info("Executing SQL query: {}", queryToInsertInEmployeeRegionDepartmentTable);
            jdbcTemplate.update(queryToInsertInEmployeeRegionDepartmentTable, employeeId, requestEmployee.getRegDeptId(), requestEmployee.getEmpStartDate(), requestEmployee.getEmpEndDate());
        } catch (Exception ex) {
            logger.error("Error executing SQL query", ex);
            throw ex;
        }
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
    public List<RequestEmployee> getAll(int page, int size, String sortField, String sortDirection, String searchTerm) {
        String query = "SELECT * FROM employee";

        if (searchTerm != null) {
            query += " WHERE id = ? OR name LIKE ?";
        }

        query += " ORDER BY " + sortField + " " + sortDirection + " LIMIT ? OFFSET ?";

        BeanPropertyRowMapper<RequestEmployee> rowMapper = new BeanPropertyRowMapper<>(RequestEmployee.class);

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
    public RequestEmployee getById(Long id){
        String query = "SELECT * FROM employee WHERE id = ?";

        try {
            logger.info("Executing SQL query: {}", query);
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(RequestEmployee.class), id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException("Employee not found with id: " + id);
        }
    }

    /**
     * This function updates the employee details of the specified employee in the database
     *
     * @param id       id of the employee that needs to be updated
     * @param requestEmployee Employee object with the updated details
     */
    public void update(long id, RequestEmployee requestEmployee) {
        String queryToUpdateInEmployeeTable = "UPDATE employee SET name = ?, role = ?, email = ? WHERE id = ?";
        String queryToUpdateInEmployeeRegionDepartmentTable = "UPDATE employee_region_department SET emp_id = ?, reg_dept_id = ?, emp_start_date = ?, emp_end_date = ? WHERE ID = ?";
        try {
            logger.info("Executing SQL query: {}", queryToUpdateInEmployeeTable);
            jdbcTemplate.update(queryToUpdateInEmployeeTable, requestEmployee.getName(), requestEmployee.getRole(), requestEmployee.getEmail(), id);
            logger.info("Executing SQL query: {}", queryToUpdateInEmployeeRegionDepartmentTable);
            jdbcTemplate.update(queryToUpdateInEmployeeRegionDepartmentTable, id, requestEmployee.getRegDeptId(), requestEmployee.getEmpStartDate(), requestEmployee.getEmpEndDate(), id);
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
        String queryToDeleteFromEmployeeRegionDepartmentTable = "DELETE FROM employee_region_department WHERE id = ?";
        String queryToDeleteFromEmployeeTable = "DELETE FROM employee WHERE id = ?";
        try {
            logger.info("Executing SQL query: {}", queryToDeleteFromEmployeeRegionDepartmentTable);
            jdbcTemplate.update(queryToDeleteFromEmployeeRegionDepartmentTable, id);
            logger.info("Executing SQL query: {}", queryToDeleteFromEmployeeTable);
            jdbcTemplate.update(queryToDeleteFromEmployeeTable, id);
        } catch (Exception e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException(e);
        }
    }
}
