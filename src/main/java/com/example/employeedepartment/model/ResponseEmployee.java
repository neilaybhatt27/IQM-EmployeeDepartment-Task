package com.example.employeedepartment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEmployee {
    private Long id;
    private String name;
    private String role;
    private String email;
    private List<Map<String,Object>> departmentDetails;
}
