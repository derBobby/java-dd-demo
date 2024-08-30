package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;

@Controller
public class HomeController {

    private final EmployeeMetricsService employeeMetricsService;

    @Autowired
    public HomeController(EmployeeMetricsService employeeMetricsService) {
        this.employeeMetricsService = employeeMetricsService;
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MeterRegistry meterRegistry;

    private Counter successfulRequests;
    private Counter failedRequests;

    @PostConstruct
    public void init() {
        successfulRequests = meterRegistry.counter("custom.http.requests.successful");
        failedRequests = meterRegistry.counter("custom.http.requests.failed");

        // Gauge für die Anzahl der Employee-Objekte hinzufügen
        Gauge.builder("custom.employee.count", employeeRepository, EmployeeRepository::count)
             .description("Anzahl der Employee-Objekte")
             .register(meterRegistry);
    }

    @GetMapping("/dynamic")
    public String dynamic(Model model) {
        model.addAttribute("employees", employeeMetricsService.getEmployees());
        return "dynamic";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/forms")
    public String gettingForm(Model model) {
        model.addAttribute("form", new Form());
        return "form";
    }

    @PostMapping("/forms")
    public String submit(@ModelAttribute Form form, Model model) {
        if (employeeRepository.existsById(form.getId())) {
            model.addAttribute("errorMessage", "Fehler: Die eingegebene ID existiert bereits.");
            model.addAttribute("form", form);
            return "form";
        }

        Employee employee = new Employee(form.getId(), form.getName(), form.getRole());
        employeeRepository.save(employee);
        model.addAttribute("employees", employeeRepository.findAll());
        return "dynamic";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        model.addAttribute("form", new Form(employee.getId(), employee.getName(), employee.getRole()));
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable("id") long id, @ModelAttribute Form form, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        employee.setName(form.getName());
        employee.setRole(form.getRole());
        employeeRepository.save(employee);
        model.addAttribute("employees", employeeRepository.findAll());
        return "dynamic";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") long id, Model model) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            model.addAttribute("errorMessage", "Mitarbeiter mit ID " + id + " existiert nicht.");
        }
        model.addAttribute("employees", employeeRepository.findAll());
        return "dynamic";
    }

    @GetMapping("/success")
    public ResponseEntity<String> successEndpoint() {
        successfulRequests.increment();
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/failure")
    public ResponseEntity<String> failureEndpoint() {
        failedRequests.increment();
        return ResponseEntity.status(500).body("Failure!");
    }
}
