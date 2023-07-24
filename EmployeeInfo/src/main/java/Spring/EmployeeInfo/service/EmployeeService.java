package Spring.EmployeeInfo.service;

import Spring.EmployeeInfo.dto.EmployeeTaxDTO;
import Spring.EmployeeInfo.model.Employee;
import Spring.EmployeeInfo.repository.EmployeeRepository;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

//import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

@Service
public class EmployeeService {


    @Autowired
    private EmployeeRepository employeeRepo;


    public ResponseEntity<?> createEmployee(Employee employee){
        int existingEmployee = employee.getId();
        List<Employee> listEmployees =employeeRepo.findAll();
        for(Employee emp : listEmployees){
            if(existingEmployee==(emp.getId()))
            return ResponseEntity.badRequest().body("Employee with this Id already existed.........!");

        }
        Employee createdEmployee = employeeRepo.save(employee);
            return ResponseEntity.ok(createdEmployee);

    }

    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = employeeRepo.findAll();
        return employeeList;
    }

    public List<EmployeeTaxDTO> taxDeductionForCurrentFinancialYear(int id){
        List<Employee> employeeList = employeeRepo.findAll();
        List<EmployeeTaxDTO> taxDeductionList = new ArrayList<>();
            Employee employee = employeeRepo.findById(id);
//        for(Employee employee : employeeList){
            double yearlySalary = Integer.parseInt(employee.getSalary()) * 12;
            double financialSalary = financialYearSalary(employee);
          double taxAmount = taxCalculation(financialSalary);
          double cessAmount = calculateCess(financialSalary);

          EmployeeTaxDTO employeeDTO = new EmployeeTaxDTO();
          employeeDTO.setId(employee.getId());
          employeeDTO.setFirstName(employee.getFirstName());
          employeeDTO.setLastName(employee.getLastName());
          employeeDTO.setSalary(yearlySalary);
          employeeDTO.setFinancialyearsalary(financialSalary);
          employeeDTO.setTaxAmount(taxAmount);
          employeeDTO.setCessAmount(cessAmount);
          taxDeductionList.add(employeeDTO);
//        }
         return  taxDeductionList;
    }

    public double taxCalculation( double salary ){
        double taxAmount = 0.0;
        double totalSalary = salary;
        if(totalSalary <= 250000){
             taxAmount = 0.0;
        } else if (totalSalary > 250000 && totalSalary <= 500000) {
             taxAmount = (totalSalary - 250000)*0.05;
        } else if (totalSalary>500000 && totalSalary <= 1000000) {
             taxAmount = (500000-250000)*0.05 + (totalSalary-500000)*0.10;
        } else if (totalSalary > 1000000) {
             taxAmount = (500000-250000)*0.05 + (1000000-500000)*0.10 +(totalSalary-1000000)*0.20;
        }
        return taxAmount;
    }

    public  double financialYearSalary(Employee employee) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        Calendar joiningDate = Calendar.getInstance();
        joiningDate.setTime(employee.getDateOfJoining());

        int financialYearStartMonth = Calendar.APRIL;
        int financialYearStartYear = currentDate.get(Calendar.YEAR);
        if (currentDate.get(Calendar.MONTH) < Calendar.APRIL) {
            financialYearStartYear -= 1;
        }
        joiningDate.set(Calendar.YEAR, financialYearStartYear);
        joiningDate.set(Calendar.MONTH, financialYearStartMonth);

        int monthsWorked = (currentDate.get(Calendar.YEAR) - joiningDate.get(Calendar.YEAR)) * 12 +
                currentDate.get(Calendar.MONTH) - joiningDate.get(Calendar.MONTH);

        double totalSalary = Integer.parseInt(employee.getSalary()) * monthsWorked;

        return totalSalary;

    }

    public static double calculateCess(double yearlySalary) {
        double cessAmount = 0.0;

        double cessLimit = 2500000;
        double cessRate = 0.02;

        if (yearlySalary > cessLimit) {
            double amountExceedingLimit = yearlySalary - cessLimit;
            cessAmount = amountExceedingLimit * cessRate;
        }

        return cessAmount;
    }

}
