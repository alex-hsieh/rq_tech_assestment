package com.challenge.api.service;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.challenge.api.model.CreateEmployeeRequest;
import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeModel;

@Service
public class EmployeeService {

    // LinkedHashMap wrapped for thread safety; preserves insertion order on getAllEmployees()
    private final Map<UUID, Employee> employeeStore =
            Collections.synchronizedMap(new LinkedHashMap<>());

    @PostConstruct
    public void seedEmployees() {
        store(buildEmployee("John", "Martin", 75000, 30, "Software Engineer", "john.martin@reliaquest.com"));
        store(buildEmployee("Sebas", "Garcia", 95000, 35, "Engineering Manager", "sebas.garcia@reliaquest.com"));
        store(buildEmployee("Dwayne", "Johnson", 65000, 28, "QA Engineer", "dwayne.johnson@reliaquest.com"));
    }

    public List<Employee> getAllEmployees() {
        return List.copyOf(employeeStore.values());
    }

    public Optional<Employee> getEmployeeByUuid(UUID uuid) {
        return Optional.ofNullable(employeeStore.get(uuid));
    }

    public Employee createEmployee(CreateEmployeeRequest request) {
        EmployeeModel employee = buildEmployee(
                request.getFirstName(),
                request.getLastName(),
                request.getSalary(),
                request.getAge(),
                request.getJobTitle(),
                request.getEmail());
        return store(employee);
    }

    /**
     * Single source of truth for constructing an {@link EmployeeModel}.
     * Derives {@code fullName} from {@code firstName} and {@code lastName} so
     * no caller has to repeat that concatenation logic.
     */
    private EmployeeModel buildEmployee(
            String firstName, String lastName, Integer salary, Integer age, String jobTitle, String email) {
        EmployeeModel employee = new EmployeeModel();
        employee.setUuid(UUID.randomUUID());
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setFullName(firstName + " " + lastName);
        employee.setSalary(salary);
        employee.setAge(age);
        employee.setJobTitle(jobTitle);
        employee.setEmail(email);
        employee.setContractHireDate(Instant.now());
        return employee;
    }

    /** Persists the employee to the store and returns it for call-chaining convenience. */
    private EmployeeModel store(EmployeeModel employee) {
        employeeStore.put(employee.getUuid(), employee);
        return employee;
    }
}
