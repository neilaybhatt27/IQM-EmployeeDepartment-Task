package com.example.employeedepartment.service.imp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employeedepartment.dao.RegionDao;
import com.example.employeedepartment.model.Region;
import com.example.employeedepartment.service.interfaces.RegionService;

@Service
public class RegionServiceImpl implements RegionService {
    @Autowired
    private final RegionDao regionDao;

    private static final Logger logger = LoggerFactory.getLogger(RegionServiceImpl.class);

    public RegionServiceImpl(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    /**
     * This function is a helper function, and it calls the save function of the RegionDao.
     *
     * @param region Region object
     */
    @Override
    public void addRegion(Region region) {
        regionDao.save(region);
    }

    /**
     * This is a helper function which calls the getAll method of the RegionDao
     *
     * @param page          page number which is requested by the controller.
     * @param size          size of the data requested by the controller.
     * @param sortField     sorting parameter
     * @param sortDirection ascending or descending parameter.
     * @param searchTerm    String based ion which data is filtered.
     * @return ArrayList containing all the regions and their details.
     */
    @Override
    public List<Region> getAllRegions(int page, int size, String sortField, String sortDirection, String searchTerm) {
        if(page < 0){
            logger.error("Error in passing parameters.");
            throw new IllegalArgumentException("Invalid parameter: Page must be greater than or equal to 0");
        }
        if(size <= 0){
            logger.error("Error in passing parameters.");
            throw new IllegalArgumentException("Invalid parameter: Size must be greater than 0");
        }
        if(!sortField.equals("id") && !sortField.equals("name")){
            logger.error("Error in passing parameters.");
            throw new IllegalArgumentException("Invalid parameter: sortField must be either id or name");
        }
        if (!sortDirection.equals("asc") && !sortDirection.equals("desc") && !sortDirection.equals("ASC") && !sortDirection.equals("DESC")){
            logger.error("Error in passing parameters.");
            throw new IllegalArgumentException("Invalid parameter: sortDirection must be either of these. 1) asc/ASC, 2) desc/DESC");
        }
        logger.info("Processing getAllRegions request with page={}, size={}, sortField={}, sortDirection={}, searchTerm={}", page, size, sortField, sortDirection, searchTerm);
        try {
            List<Region> regions = regionDao.getAll(page, size, sortField, sortDirection, searchTerm);
            logger.info("Finished processing getAllRegions request with {} employees", regions.size());
            return regions;
        } catch (Exception e) {
            logger.error("Error occurred in database operation.");
            throw new RuntimeException("Some error occurred in the server.",e);
        }
    }

    /**
     * This is a helper function, and it calls the getById(Long id) method of the RegionDao.
     *
     * @param id id of the region of which the details are requested.
     * @return Region object containing the details of the requested region id.
     */
    @Override
    public Region getRegionById(Long id) {
        try {
            logger.info("Processing getRegionById request with id={}", id);
            Region region = regionDao.getById(id);
            logger.info("Finished processing getRegionById request with region id = {}", id);
            return region;
        } catch (RuntimeException e) {
            logger.error("Error occurred in database operation.");
            throw e;
        }
    }

    /**
     * This is a helper function which calls the update method of RegionDao
     *
     * @param id       id of the region whose details needs to be updated
     * @param region Region object containing the updated details
     */
    @Override
    public void updateRegion(Long id, Region region) {
        regionDao.update(id, region);
    }

    /**
     * This is a helper function which calls the delete method of the RegionDao
     *
     * @param id id of the region whose details need to be deleted from the database.
     */
    @Override
    public void deleteRegion(Long id) {
        regionDao.delete(id);
    }
}
