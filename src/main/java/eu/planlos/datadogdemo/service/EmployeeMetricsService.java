package eu.planlos.datadogdemo.service;

import eu.planlos.datadogdemo.model.Employee;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeMetricsService {

    private final List<Employee> employees = new ArrayList<>();

    public EmployeeMetricsService(MeterRegistry meterRegistry) {

        // Erstellen eines Gauges, der die Größe der Employee-Liste misst
        Gauge.builder("employee.count", employees, List::size)
                .description("Number of employees")
                .register(meterRegistry);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }
}
