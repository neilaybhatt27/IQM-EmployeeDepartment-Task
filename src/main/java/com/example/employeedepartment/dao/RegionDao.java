package com.example.employeedepartment.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.employeedepartment.model.Region;

@Repository
public class RegionDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RegionDao.class);

    public RegionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * This function adds region details to the database.
     *
     * @param region - Region object received from service method
     */
    public void save(Region region) {
        String query = "INSERT INTO region (name, start_date) VALUES (?, ?)";
        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.update(query, region.getName(), region.getStartDate());
        } catch (Exception ex) {
            logger.error("Error executing SQL query", ex);
            throw ex;
        }
    }

    /**
     * This function returns all the regions with pagination to reduce load.
     * Also contains sorting by name and id feature.
     * Also contains filtering by search term.
     *
     * @param page          page number for pagination.
     * @param size          number of rows to be sent for a single page.
     * @param sortField     Sort by id or by name.
     * @param sortDirection Ascending or descending sorting.
     * @param searchTerm    String used for filtering by name or id.
     * @return List of all regions found in the database
     */
    public List<Region> getAll(int page, int size, String sortField, String sortDirection, String searchTerm) {
        String query = "SELECT * FROM region";

        if (searchTerm != null) {
            query += " WHERE id = ? OR name LIKE ?";
        }

        query += " ORDER BY " + sortField + " " + sortDirection + " LIMIT ? OFFSET ?";

        BeanPropertyRowMapper<Region> rowMapper = new BeanPropertyRowMapper<>(Region.class);

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
     * This function gets the specific region by matching the id.
     *
     * @param id - id of the requested employee
     * @return Region object of the specified id.
     */
    public Region getById(Long id){
        String query = "SELECT * FROM region WHERE id = ?";

        try {
            logger.info("Executing SQL query: {}", query);
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Region.class), id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException("Employee not found with id: " + id);
        }
    }

    /**
     * This function updates the region details of the specified region in the database
     *
     * @param id       id of the region that needs to be updated
     * @param region Region object with the updated details
     */
    public void update(long id, Region region) {
        String query = "UPDATE region SET name = ?, start_date = ?, end_date = ? WHERE id = ?";
        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.update(query, region.getName(), region.getStartDate(), region.getEndDate(), id);
        } catch (Exception e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException(e);
        }
    }

    /**
     * This function deletes the specified region from the database
     *
     * @param id - id of the region that needs to be deleted
     */
    public void delete(long id) {
        String query = "DELETE FROM region WHERE id = ?";
        try {
            logger.info("Executing SQL query: {}", query);
            jdbcTemplate.update(query, id);
        } catch (Exception e) {
            logger.error("Error executing SQL query");
            throw new RuntimeException(e);
        }
    }
}
