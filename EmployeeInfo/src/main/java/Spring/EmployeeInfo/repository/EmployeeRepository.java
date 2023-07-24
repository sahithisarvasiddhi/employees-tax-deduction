package Spring.EmployeeInfo.repository;

import Spring.EmployeeInfo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    Employee findById(int id);


//    @Query("From Employee where emailId = :emailid")
//    Employee findByEmail(String emailid);

//    @Query("From Employee where id = :id")
//    Employee findById(int id);
}
