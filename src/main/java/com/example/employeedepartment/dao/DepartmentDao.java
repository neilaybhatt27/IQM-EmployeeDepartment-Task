package com.example.employeedepartment.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.employeedepartment.model.RequestDepartment;
import com.example.employeedepartment.model.ResponseDepartment;

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
    public List<ResponseDepartment> getAll(int page, int size, String sortField, String sortDirection, String searchTerm) {
        List<Object> args = new ArrayList<>();
        String query = "SELECT department.id, department.name AS name, region.name AS region, region_department.dept_start_date, region_department.dept_end_date FROM department JOIN region_department ON department.id = region_department.dept_id JOIN region ON region_department.reg_id = region.id INNER JOIN (SELECT id FROM department";

        if (searchTerm != null) {
            query += " WHERE department.id = ? OR department.name LIKE ?";
            args.add(searchTerm);
            args.add("%"+searchTerm+"%");
        }

        query += " ORDER BY id LIMIT ? OFFSET ?) AS deptId ON department.id = deptId.id ORDER BY department." + sortField + " " + sortDirection;
        args.add(size);
        args.add(page * size);
        Map<String, ResponseDepartment> deptNameObjectMap = new LinkedHashMap<>();
        try {
            logger.info("Executing SQL query: {}", query);
             jdbcTemplate.query(query, args.toArray(), (rs, rowNum) -> {
                 ResponseDepartment responseDepartment;
                 String deptName = rs.getString("name");

                 // Retrieve or create the ResponseDepartment object
                 if (!deptNameObjectMap.containsKey(deptName)) {
                     responseDepartment = new ResponseDepartment();
                     responseDepartment.setId(rs.getLong("id"));
                     responseDepartment.setName(deptName);
                     List<Map<String, Object>> regionDetailsList = new ArrayList<>();
                     responseDepartment.setRegionDetails(regionDetailsList);
                     deptNameObjectMap.put(deptName, responseDepartment);
                 } else {
                     responseDepartment = deptNameObjectMap.get(deptName);
                 }

                 // Create a new region details map for each row
                 Map<String, Object> regionDetailsMap = new HashMap<>();
                 regionDetailsMap.put("region", rs.getString("region"));
                 regionDetailsMap.put("deptStartDate", rs.getDate("dept_start_date").toLocalDate());

                 // Check if "dept_end_date" column is null
                 if (rs.getObject("dept_end_date") != null) {
                     regionDetailsMap.put("deptEndDate", rs.getDate("dept_end_date").toLocalDate());
                 } else {
                     // Handle null value
                     regionDetailsMap.put("deptEndDate", null);
                 }

                 // Add the current region detail to the list
                 responseDepartment.getRegionDetails().add(regionDetailsMap);
                 return responseDepartment;
             });
        } catch (Exception ex) {
            logger.error("Error executing SQL query", ex);
            throw ex;
        }
        return new ArrayList<>(deptNameObjectMap.values());
    }

    /**
     * This method interacts with the database and fetches the department whose id matches the id in the input.
     * @param id id of the department which needs to be fetched.
     * @return Department object containing the requested department details.
     */
    public ResponseDepartment getById(Long id) {
        String query = "SELECT department.id, department.name AS name, region_department.reg_id, region.name AS region, region_department.dept_start_date, region_department.dept_end_date FROM department JOIN region_department ON department.id = region_department.dept_id JOIN region ON region_department.reg_id = region.id WHERE department.id = ?";
        Map<String, ResponseDepartment> deptNameObjectMap = new HashMap<>();

        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.query(query, (rs, numRow) -> {
                ResponseDepartment responseDepartment;
                String deptName = rs.getString("name");

                // Retrieve or create the ResponseDepartment object
                if (!deptNameObjectMap.containsKey(deptName)) {
                    responseDepartment = new ResponseDepartment();
                    responseDepartment.setId(rs.getLong("id"));
                    responseDepartment.setName(deptName);
                    List<Map<String, Object>> regionDetailsList = new ArrayList<>();
                    responseDepartment.setRegionDetails(regionDetailsList);
                    deptNameObjectMap.put(deptName, responseDepartment);
                } else {
                    responseDepartment = deptNameObjectMap.get(deptName);
                }

                // Create a new region details map for each row
                Map<String, Object> regionDetailsMap = new HashMap<>();
                regionDetailsMap.put("region", rs.getString("region"));
                regionDetailsMap.put("deptStartDate", rs.getDate("dept_start_date").toLocalDate());

                // Check if "dept_end_date" column is null
                if (rs.getObject("dept_end_date") != null) {
                    regionDetailsMap.put("deptEndDate", rs.getDate("dept_end_date").toLocalDate());
                } else {
                    // Handle null value
                    regionDetailsMap.put("deptEndDate", null);
                }

                // Add the current region detail to the list
                responseDepartment.getRegionDetails().add(regionDetailsMap);
                return responseDepartment;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException("Department not found with id: " + id);
        }
        Map.Entry<String, ResponseDepartment> entry = deptNameObjectMap.entrySet().iterator().next();
        return entry.getValue();
    }

    /**
     * This method adds a new department to the database.
     * Checks if the department already exists in department table and region_department table. If yes then it only adds the same department in new region.
     * If no, it adds Department name in the department table.
     * Maps its id to the given region id in the input and adds both of them along with start date
     * into the region_department table.
     * @param requestDepartment Department object containing the info to be added in the database.
     */
    public void save(RequestDepartment requestDepartment) {
        String queryToCheckIfDepartmentExists = "SELECT * FROM department WHERE name = ?";
        String queryToAddInDepartmentTable = "INSERT INTO department (name) VALUES (?)";
        String queryToAddInRegionDepartmentTable = "INSERT INTO region_department (reg_id, dept_id, dept_start_date) VALUES (?, ? ,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        long departmentId;
        try {
            logger.info("Executing SQL query: {}", queryToCheckIfDepartmentExists);
            try{
                RequestDepartment exisitngRequestDepartment = jdbcTemplate.queryForObject(queryToCheckIfDepartmentExists, new BeanPropertyRowMapper<>(RequestDepartment.class), requestDepartment.getName());
                departmentId = exisitngRequestDepartment.getId();
            } catch (EmptyResultDataAccessException e){
                logger.info("Executing SQL query: {}", queryToAddInDepartmentTable);
                jdbcTemplate.update(connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queryToAddInDepartmentTable, new String[]{"id"});
                    preparedStatement.setString(1, requestDepartment.getName());
                    return preparedStatement;
                }, holder);
                departmentId = holder.getKey().longValue();
            }

            logger.info("Executing SQL query: {}", queryToAddInRegionDepartmentTable);
            jdbcTemplate.update(queryToAddInRegionDepartmentTable, requestDepartment.getRegId(), departmentId, requestDepartment.getDeptStartDate());
        } catch (Exception ex) {
            logger.error("Error executing SQL query", ex);
            throw ex;
        }
    }

    /**
     * This method updates the department details in the database.
     * @param id id of the department that needs to be updated.
     * @param requestDepartment Department object containing new details.
     */
    public void update(Long id, RequestDepartment requestDepartment) {
        String queryToUpdateInDepartmentTable = "UPDATE department SET name = ? WHERE id = ?";
        String queryToUpdateInRegionDepartmentTable = "UPDATE region_department SET reg_id = ?, dept_start_date = ?, dept_end_date = ? WHERE dept_id = ?";
        try {
            logger.info("Executing SQL query: {}", queryToUpdateInDepartmentTable);
            jdbcTemplate.update(queryToUpdateInDepartmentTable, requestDepartment.getName(), id);
            logger.info("Executing SQL query: {}", queryToUpdateInRegionDepartmentTable);
            jdbcTemplate.update(queryToUpdateInRegionDepartmentTable,requestDepartment.getRegId(), requestDepartment.getDeptStartDate(), requestDepartment.getDeptEndDate(), id);
        } catch (Exception e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException(e);
        }
    }

    /**
     * This DAO method interacts with region_department and employee_region_department tables of the database.
     * It first adds the end date to the employees currently working in the department.
     * Then it adds the end date to the region_department so that department becomes closed.
     * @param deptId id of the department that needs to be closed.
     * @param updates Map containing region id and end date as values.
     */
    public void saveEndDate(Long deptId, Map<String, Object> updates){
        String queryToFetchRegDeptId = "SELECT reg_dept_id FROM employee_region_department JOIN region_department ON employee_region_department.reg_dept_id = region_department.id WHERE region_department.reg_id = ? AND region_department.dept_id = ? LIMIT 1";
        String queryToUpdateEmployeeRegionDepartmentTable = "UPDATE employee_region_department SET emp_end_date = ? WHERE reg_dept_id = ? AND emp_end_date IS NULL";
        String queryToUpdateRegionDepartmentTable = "UPDATE region_department SET dept_end_date = ? WHERE reg_id = ? AND dept_id = ?";
        try {
            logger.info("Executing SQL query: {}", queryToFetchRegDeptId);
            Long regDeptId = jdbcTemplate.queryForObject(queryToFetchRegDeptId, Long.class, updates.get("regId"), deptId);
            logger.info("Executing SQL query: {}", queryToUpdateEmployeeRegionDepartmentTable);
            jdbcTemplate.update(queryToUpdateEmployeeRegionDepartmentTable, updates.get("deptEndDate"), regDeptId);
            logger.info("Executing SQL query: {}", queryToUpdateRegionDepartmentTable);
            jdbcTemplate.update(queryToUpdateRegionDepartmentTable, updates.get("deptEndDate"), updates.get("regId"), deptId);
        } catch (Exception e){
            logger.error("Error executing SQL query");
            throw new RuntimeException(e);
        }
    }

    /**
     * This method deletes the department from the database.
     * @param id id of the department that needs to be deleted.
     */
    public void delete(Long id) {
        String queryToDeleteFromRegionDepartmentTable = "DELETE FROM region_department WHERE dept_id = ?";
        String queryToDeleteFromDepartmentTable = "DELETE FROM department WHERE id = ?";
        try {
            logger.info("Executing SQL query: {}", queryToDeleteFromRegionDepartmentTable);
            jdbcTemplate.update(queryToDeleteFromRegionDepartmentTable, id);
            logger.info("Executing SQL query: {}", queryToDeleteFromDepartmentTable);
            jdbcTemplate.update(queryToDeleteFromDepartmentTable, id);
        } catch (Exception e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException(e);
        }
    }
}
