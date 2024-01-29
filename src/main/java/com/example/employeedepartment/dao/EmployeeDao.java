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

import com.example.employeedepartment.model.RequestEmployee;
import com.example.employeedepartment.model.ResponseEmployee;

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
     * @param requestEmployee Employee object received from client side
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
                    preparedStatement.setString(3, requestEmployee.getEmail());
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
    public List<ResponseEmployee> getAll(int page, int size, String sortField, String sortDirection, String searchTerm) {
        List<Object> args = new ArrayList<>();
        String query = "SELECT employee.id, employee.name AS name, employee.role, employee.email, department.name as department_name, region.name AS region, employee_region_department.emp_start_date, employee_region_department.emp_end_date FROM employee JOIN employee_region_department ON employee_region_department.emp_id = employee.id JOIN region_department ON employee_region_department.reg_dept_id = region_department.id JOIN region ON region_department.reg_id = region.id JOIN department ON region_department.dept_id = department.id INNER JOIN (SELECT id FROM employee";

        if (searchTerm != null) {
            query += " WHERE employee.id = ? OR employee.name LIKE ?";
            args.add(searchTerm);
            args.add("%"+searchTerm+"%");
        }

        query += " ORDER BY id LIMIT ? OFFSET ?) AS empId ON employee.id = empId.id ORDER BY employee." + sortField + " " + sortDirection;
        args.add(size);
        args.add(page * size);

        Map<String, ResponseEmployee> empNameObjectMap = new LinkedHashMap<>();
        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.query(query, args.toArray(), (rs, numRow) -> {
                ResponseEmployee responseEmployee;
                String empName = rs.getString("name");

                if(!empNameObjectMap.containsKey(empName)){
                    responseEmployee = new ResponseEmployee();
                    responseEmployee.setId(rs.getLong("id"));
                    responseEmployee.setName(empName);
                    responseEmployee.setRole(rs.getString("role"));
                    responseEmployee.setEmail(rs.getString("email"));
                    List<Map<String, Object>> deptDetailsList = new ArrayList<>();
                    responseEmployee.setDepartmentDetails(deptDetailsList);
                    empNameObjectMap.put(empName, responseEmployee);
                }
                else{
                    responseEmployee = empNameObjectMap.get(empName);
                }

                Map<String, Object> deptDetailsMap = new HashMap<>();
                deptDetailsMap.put("departmentName", rs.getString("department_name"));
                deptDetailsMap.put("region", rs.getString("region"));
                deptDetailsMap.put("empStartDate", rs.getDate("emp_start_date").toLocalDate());

                if(rs.getDate("emp_end_date") != null){
                    deptDetailsMap.put("empEndDate", rs.getDate("emp_end_date").toLocalDate());
                }
                else{
                    deptDetailsMap.put("empEndDate", null);
                }
                responseEmployee.getDepartmentDetails().add(deptDetailsMap);
                return responseEmployee;
            });
        } catch (Exception ex) {
            logger.error("Error executing SQL query", ex);
            throw ex;
        }
        return new ArrayList<>(empNameObjectMap.values());
    }

    /**
     * This function gets the specific employee by matching the id.
     *
     * @param id - id of the requested employee
     * @return Employee object of the specified id.
     */
    public ResponseEmployee getById(Long id){
        String query = "SELECT employee.id, employee.name AS name, employee.role, employee.email, department.name as department_name, region.name AS region, employee_region_department.emp_start_date, employee_region_department.emp_end_date FROM employee JOIN employee_region_department ON employee_region_department.emp_id = employee.id JOIN region_department ON employee_region_department.reg_dept_id = region_department.id JOIN region ON region_department.reg_id = region.id JOIN department ON region_department.dept_id = department.id WHERE employee.id = ?";
        Map<String, ResponseEmployee> empNameObjectMap = new HashMap<>();

        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.query(query, (rs, numRow) -> {
                ResponseEmployee responseEmployee;
                String empName = rs.getString("name");

                if(!empNameObjectMap.containsKey(empName)){
                    responseEmployee = new ResponseEmployee();
                    responseEmployee.setId(rs.getLong("id"));
                    responseEmployee.setName(empName);
                    responseEmployee.setRole(rs.getString("role"));
                    responseEmployee.setEmail(rs.getString("email"));
                    List<Map<String, Object>> deptDetailsList = new ArrayList<>();
                    responseEmployee.setDepartmentDetails(deptDetailsList);
                    empNameObjectMap.put(empName, responseEmployee);
                }
                else{
                    responseEmployee = empNameObjectMap.get(empName);
                }

                Map<String, Object> deptDetailsMap = new HashMap<>();
                deptDetailsMap.put("departmentName", rs.getString("department_name"));
                deptDetailsMap.put("region", rs.getString("region"));
                deptDetailsMap.put("empStartDate", rs.getDate("emp_start_date").toLocalDate());

                if(rs.getDate("emp_end_date") != null){
                    deptDetailsMap.put("empEndDate", rs.getDate("emp_end_date").toLocalDate());
                }
                else{
                    deptDetailsMap.put("empEndDate", null);
                }
                responseEmployee.getDepartmentDetails().add(deptDetailsMap);
                return responseEmployee;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException("Employee not found with id: " + id);
        }
        Map.Entry<String, ResponseEmployee> entry = empNameObjectMap.entrySet().iterator().next();
        return entry.getValue();
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
     * This DAO method interacts with employee_region_department table in the database.
     * It first fetches the reg_dept_id from region department using department id and region id.
     * Then it adds or updates the end date in the employee_region_department table of the corresponding employee id
     * and region department id.
     * @param empId id of the employee.
     * @param updates Map containing region id, department id and end date.
     */
    public void saveEndDate(Long empId, Map<String, Object> updates){
        String queryToFetchRegDeptId = "SELECT reg_dept_id FROM employee_region_department JOIN region_department ON employee_region_department.reg_dept_id = region_department.id WHERE region_department.reg_id = ? AND region_department.dept_id = ? LIMIT 1";
        String queryToUpdateInEmployeeRegionDepartmentTable = "UPDATE employee_region_department SET emp_end_date = ? WHERE reg_dept_id = ? AND emp_id = ?";
        try {
            logger.info("Executing SQL query: {}", queryToFetchRegDeptId);
            Long regDeptId = jdbcTemplate.queryForObject(queryToFetchRegDeptId, Long.class, updates.get("regId"), updates.get("deptId"));
            logger.info("Executing SQL query: {}", queryToUpdateInEmployeeRegionDepartmentTable);
            jdbcTemplate.update(queryToUpdateInEmployeeRegionDepartmentTable, updates.get("empEndDate"), regDeptId, empId);
        } catch (Exception e){
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
