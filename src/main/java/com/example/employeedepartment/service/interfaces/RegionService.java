package com.example.employeedepartment.service.interfaces;

import java.util.List;

import com.example.employeedepartment.model.Region;

public interface RegionService {
    void addRegion(Region region);

    List<Region> getAllRegions(int page, int size, String sortField, String sortDirection, String searchTerm);

    Region getRegionById(Long id);

    void updateRegion(Long id, Region region);

    void deleteRegion(Long id);
}
