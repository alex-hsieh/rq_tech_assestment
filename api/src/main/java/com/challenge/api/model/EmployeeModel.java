package com.challenge.api.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class EmployeeModel implements Employee {

    private UUID uuid;
    private String firstName;
    private String lastName;

    /**
     * Derived by the service layer as {@code firstName + " " + lastName}.
     * Not settable by API callers; any value provided externally will be ignored.
     */
    private String fullName;

    private Integer salary;
    private Integer age;
    private String jobTitle;
    private String email;
    private Instant contractHireDate;
    private Instant contractTerminationDate;
}
