package eu.planlos.datadogdemo.repository;

import eu.planlos.datadogdemo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsById(long id);
    void deleteById(long id);
}