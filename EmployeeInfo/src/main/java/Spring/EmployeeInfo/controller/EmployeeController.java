package Spring.EmployeeInfo.controller;

import Spring.EmployeeInfo.dto.EmployeeTaxDTO;
import Spring.EmployeeInfo.model.Employee;
import Spring.EmployeeInfo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add-employees")
    public ResponseEntity<?> addEmployee(@RequestBody@Valid Employee employee){

      return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @GetMapping("/get-Employees")
    public List<Employee> getEmployee(Employee employee){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/get-employees-tax-deduction/{id}")
    public ResponseEntity<?> employeesList(@PathVariable int id){
        return  employeeService.taxDeductionForCurrentFinancialYear(id);
    }
}
