package com.example.employeedepartment;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.employeedepartment.dao.EmployeeDao;
import com.example.employeedepartment.model.Employee;
import com.example.employeedepartment.service.imp.EmployeeServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class EmployeeServiceImplTest {
    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test: Given input as invalid page number, this should return IllegalArgumentException with the message:
     * "Invalid parameter: Page must be greater than or equal to 0"
     */
    @Test
    public void getAllEmployeesInvalidPageTest() {
        int page = -1;
        int size = 5;
        String sortField = "id";
        String sortDirection = "asc";
        String searchTerm = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.getAllEmployees(page, size, sortField, sortDirection, searchTerm));

        String expectedMessage = "Invalid parameter: Page must be greater than or equal to 0";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Test: Given input as invalid size number set to zero, this should return IllegalArgumentException with the message:
     * "Invalid parameter: Size must be greater than to 0"
     */
    @Test
    public void getAllEmployeesInvalidSizeEqualsZeroTest() {
        int page = 0;
        int size = 0;
        String sortField = "id";
        String sortDirection = "asc";
        String searchTerm = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.getAllEmployees(page, size, sortField, sortDirection, searchTerm));

        String expectedMessage = "Invalid parameter: Size must be greater than 0";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Test: Given input as invalid size number set to negative, this should return IllegalArgumentException with the message:
     * "Invalid parameter: Size must be greater than to 0"
     */
    @Test
    public void getAllEmployeesInvalidSizeEqualsNegativeTest() {
        int page = 0;
        int size = -1;
        String sortField = "id";
        String sortDirection = "asc";
        String searchTerm = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.getAllEmployees(page, size, sortField, sortDirection, searchTerm));

        String expectedMessage = "Invalid parameter: Size must be greater than 0";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Test: Given input as invalid sortField, this should return IllegalArgumentException with the message:
     * "Invalid parameter: sortField must be either id or name"
     */
    @Test
    public void getAllEmployeesInvalidSortFieldTest() {
        int page = 0;
        int size = 5;
        String sortField = "abcd";
        String sortDirection = "asc";
        String searchTerm = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.getAllEmployees(page, size, sortField, sortDirection, searchTerm));

        String expectedMessage = "Invalid parameter: sortField must be either id or name";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Test: Given input as invalid sortDirection, this should return IllegalArgumentException with the message:
     * "Invalid parameter: sortDirection must be either of these. 1) asc/ASC, 2) desc/DESC"
     */
    @Test
    public void getAllEmployeesInvalidSortDirectionTest() {
        int page = 0;
        int size = 5;
        String sortField = "name";
        String sortDirection = "abcd";
        String searchTerm = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.getAllEmployees(page, size, sortField, sortDirection, searchTerm));

        String expectedMessage = "Invalid parameter: sortDirection must be either of these. 1) asc/ASC, 2) desc/DESC";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Test: Given input as all valid parameters, this should return List of employees with size 5.
     */
    @Test
    public void getAllEmployeesValidInputTest() {
        int page = 0;
        int size = 5;
        String sortField = "id";
        String sortDirection = "asc";
        String searchTerm = "";

        List<Employee> expectedEmployees = new ArrayList<>();
        when(employeeDao.getAll(page, size, sortField, sortDirection, searchTerm)).thenReturn(expectedEmployees);
        List<Employee> actualEmployees = employeeService.getAllEmployees(page, size, sortField, sortDirection, searchTerm);

        assertEquals(expectedEmployees.size(), actualEmployees.size());
    }
}
