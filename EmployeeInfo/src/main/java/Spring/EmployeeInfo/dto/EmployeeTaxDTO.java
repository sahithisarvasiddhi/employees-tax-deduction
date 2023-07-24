package Spring.EmployeeInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmployeeTaxDTO {

    private int id;

    private String firstName;

    private String lastName;

    private double salary;

    private double taxAmount;

    private double cessAmount;

    private double financialyearsalary;




}
