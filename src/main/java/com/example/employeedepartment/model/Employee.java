package com.example.employeedepartment.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ApiModel(description = "Details about an employee")
public class Employee {
    @Id
    @ApiModelProperty(notes = "The unique identifier for an employee", example = "1")
    private long id;
    @ApiModelProperty(notes = "The name of the employee", example = "Naruto Uzumaki")
    private String name;
    @ApiModelProperty(notes = "The role/designation of the employee", example = "Engineer")
    private String role;
    @ApiModelProperty(notes = "The id of the department the employee works in", example = "1")
    private long departmentId;
}
