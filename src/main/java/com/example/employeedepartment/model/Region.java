package com.example.employeedepartment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    @Id
    private long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
