package com.challenge.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateEmployeeRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive number")
    private Integer salary;

    @NotNull(message = "Age is required")
    @Positive(message = "Age must be a positive number")
    private Integer age;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    private String email;
}
