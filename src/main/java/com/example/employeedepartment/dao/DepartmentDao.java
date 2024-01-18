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


import com.example.employeedepartment.model.Department;

@Repository
public class DepartmentDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(DepartmentDao.class);

    public DepartmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * This function returns all the departments with pagination to reduce load.
     * Also contains sorting by name and id feature.
     * Also contains filtering by search term.
     *
     * @param page          page number for pagination.
     * @param size          number of rows to be sent for a single page.
     * @param sortField     Sort by id or by name.
     * @param sortDirection Ascending or descending sorting.
     * @param searchTerm    String used for filtering by name or id.
     * @return List of all departments found in the database.
     */
    public List<Department> getAll(int page, int size, String sortField, String sortDirection, String searchTerm) {
        String query = "SELECT * FROM department";

        if (searchTerm != null) {
            query += " WHERE id = ? OR name LIKE ?";
        }

        query += " ORDER BY " + sortField + " " + sortDirection + " LIMIT ? OFFSET ?";

        BeanPropertyRowMapper<Department> rowMapper = new BeanPropertyRowMapper<>(Department.class);

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
     * This method interacts with the database and fetches the department whose id matches the id in the input.
     * @param id id of the department which needs to be fetched.
     * @return Department object containing the requested department details.
     */
    public Department getById(Long id) {
        String query = "SELECT * FROM department WHERE id = ?";

        try {
            logger.info("Executing SQL query: {}", query);
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Department.class), id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException("Department not found with id: " + id);
        }
    }

    /**
     * This method adds a new department to the database.
     * Adds Department name in the department table.
     * Maps its id to the given region id in the input and adds both of them along with start date
     * into the region_department table.
     * @param department Department object containing the info to be added in the database.
     */
    public void save(Department department) {
        String queryToAddInDepartmentTable = "INSERT INTO department (name) VALUES (?)";
        String queryToAddInRegionDepartmentTable = "INSERT INTO region_department (reg_id, dept_id, dept_start_date) VALUES (?, ? ,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        try {
            logger.info("Executing SQL query: {}", queryToAddInDepartmentTable);
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(queryToAddInDepartmentTable, new String[]{"id"});
                preparedStatement.setString(1, department.getName());
                return preparedStatement;
            }, holder);
            long departmentId = holder.getKey().longValue();

            logger.info("Executing SQL query: {}", queryToAddInRegionDepartmentTable);
            jdbcTemplate.update(queryToAddInRegionDepartmentTable, department.getRegId(), departmentId, department.getDeptStartDate());
        } catch (Exception ex) {
            logger.error("Error executing SQL query", ex);
            throw ex;
        }
    }

    /**
     * This method updates the department details in the database.
     * @param id id of the department that needs to be updated.
     * @param department Department object containing new details.
     */
    public void update(Long id, Department department) {
        String query = "UPDATE department SET name = ? WHERE id = ?";
        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.update(query, department.getName(), id);
        } catch (Exception e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException(e);
        }
    }

    /**
     * This method deletes the department from the database.
     * @param id id of the department that needs to be deleted.
     */
    public void delete(Long id) {
        String query = "DELETE FROM department WHERE id = ?";
        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.update(query, id);
        } catch (Exception e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException(e);
        }
    }
}
