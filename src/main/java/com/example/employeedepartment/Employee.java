package com.example.employeedepartment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String role;
    private Long departmentId;
    
    // @ManyToOne
    // @JoinColumn(name = "department_id")
    // private Department department;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getROle(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartment(Long departmentId) {
        this.departmentId = departmentId;
    }
}
