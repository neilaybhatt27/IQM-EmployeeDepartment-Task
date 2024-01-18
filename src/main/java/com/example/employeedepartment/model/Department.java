package com.example.employeedepartment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    private Long id;
    private String name;
}
