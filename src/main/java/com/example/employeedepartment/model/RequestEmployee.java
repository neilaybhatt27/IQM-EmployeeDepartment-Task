package com.example.employeedepartment.model;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Details about an employee")
public class RequestEmployee {
    @Id
    @ApiModelProperty(notes = "The unique identifier for an employee", example = "1")
    private long id;

    @ApiModelProperty(notes = "The name of the employee", example = "Naruto Uzumaki")
    private String name;

    @ApiModelProperty(notes = "The role/designation of the employee", example = "Engineer")
    private String role;

    @ApiModelProperty(notes = "The email address of the employee", example = "abc@gmail.com")
    private String email;

    @ApiModelProperty(notes = "Region-Department id in which the employee works", example = "1")
    private Long regDeptId;

    @ApiModelProperty(notes = "Employee start date in the department", example = "2013-01-01")
    private LocalDate empStartDate;

    @ApiModelProperty(notes = "Employee end date in the department", example = "2013-01-01")
    private LocalDate empEndDate;
}
