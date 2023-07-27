package Spring.EmployeeInfo.service;

import Spring.EmployeeInfo.dto.EmployeeTaxDTO;
import Spring.EmployeeInfo.model.Employee;
import Spring.EmployeeInfo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
public class EmployeeService {


    @Autowired
    private EmployeeRepository employeeRepo;


    public ResponseEntity<?> createEmployee(Employee employee){
        Employee existingEmployee = employeeRepo.findByEmailId(employee.getEmailId());
        Employee existingEmployeeId = employeeRepo.findById(employee.getId());

        if (existingEmployee != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee with this Email already existed.........!");
            }
        if (existingEmployeeId != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee with this Id already existed.........!");
        }
            Set<String> phoneNumbers = employee.getPhoneNumber();
            if(phoneNumbers != null){
                for(String phoneNumber : phoneNumbers){
                    if(!phoneNumber.matches("^\\d{10}$")){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid PhoneNumber...!");
                    }
                }
            }

        Employee createdEmployee = employeeRepo.save(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }



    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = employeeRepo.findAll();
        return employeeList;
    }

    public ResponseEntity<?> taxDeductionForCurrentFinancialYear(int id){
        List<Employee> employeeList = employeeRepo.findAll();
        List<EmployeeTaxDTO> taxDeductionList = new ArrayList<>();
            Employee employee = employeeRepo.findById(id);
            if(employee == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id not found");
            }
//        for(Employee employee : employeeList){
            double yearlySalary = Integer.parseInt(employee.getSalary()) * 12;
            double financialSalary = financialYearSalary(employee) * Integer.parseInt(employee.getSalary())/30;
            double taxAmount = taxCalculation(yearlySalary);
            double cessAmount = calculateCess(yearlySalary);

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
         return  ResponseEntity.ok(taxDeductionList);
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

        LocalDate currentDate = LocalDate.now();
        LocalDate doj = employee.getDateOfJoining().toLocalDate();
        int startYear = currentDate.getYear();
        int startMonth = 4;
        int startDay = 1;
        int endYear = startYear+1;
        int endMonth = 3;
        int endDay = 31;
        LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
        LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);

        if (currentDate.getMonthValue() < Calendar.APRIL) {
            startYear -= 1;
        }

        double daysInFinancialYear = 0.0;
        if(doj.getYear() < startYear) {
            daysInFinancialYear = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }else if(doj.getYear() == startYear && doj.getMonthValue() < startMonth){
            daysInFinancialYear = ChronoUnit.DAYS.between(startDate, endDate)+1;
        }else if(doj.getYear() == startYear && doj.getMonthValue() >= startMonth){
            daysInFinancialYear = ChronoUnit.DAYS.between(doj,endDate)+1;
        }else if(doj.getYear() == startYear && doj.getMonthValue() > startMonth){
            daysInFinancialYear = ChronoUnit.DAYS.between(doj,endDate)+1;
        }else if(doj.getYear() == endYear && doj.getMonthValue() < startMonth){
            daysInFinancialYear = ChronoUnit.DAYS.between(doj,endDate)+1;
        }
        return daysInFinancialYear;
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
