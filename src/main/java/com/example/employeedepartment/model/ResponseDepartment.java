package com.example.employeedepartment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDepartment {
    private Long id;
    private String name;
    private List<Map<String, Object>> regionDetails;

}
