package Spring.EmployeeInfo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @Column(name = "emp_id")
    @Min(value = 1, message = "ID must be a positive value.")
    private int id;

    @Column
    @NotBlank(message = "Invalid name : Name is mandatory")
    @Size(min = 3, max = 30, message = "Invalid Name: Exceeds 30 characters")
    private String firstName;

    @Column
    @NotBlank(message = "Invalid name : Name is mandatory")
    @Size(min = 3, max = 30, message = "Invalid Name: Exceeds 30 characters")
    private String lastName;

    @Column(name = "email_id")
    @Email(message = "Type a valid Email")
    @NotBlank(message = "Email is mandatory")
    private String emailId;

    @ElementCollection(fetch =FetchType.EAGER)
    @NotNull(message = "Enter PhoneNumber")
//    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
    @CollectionTable(name = "phone_number", joinColumns = @JoinColumn(name = "emp_id"))
    private Set<String> phoneNumber;

    @Column(name = "date_of_joining")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfJoining;

    @Column
    @NotBlank(message = "Enter salary per month")
    @Pattern(regexp = "^\\d+$",message = "Invalid salary")
    @Min(value = 3, message = "Salary must be a positive value.")
    private String salary;

}
