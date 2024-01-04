package com.example.employeedepartment.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String role;
    private Long departmentId;

//    public Long getId(){
//        return id;
//    }
//
//    public void setId(Long id){
//        this.id = id;
//    }
//
//    public String getName(){
//        return name;
//    }
//
//    public void setName(String name){
//        this.name = name;
//    }
//
//    public String getROle(){
//        return role;
//    }
//
//    public void setRole(String role){
//        this.role = role;
//    }
//
//    public Long getDepartmentId() {
//        return departmentId;
//    }
//
//    public void setDepartment(Long departmentId) {
//        this.departmentId = departmentId;
//    }
}
