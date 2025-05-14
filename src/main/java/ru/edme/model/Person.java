package ru.edme.model;

import lombok.Data;
import ru.edme.annotation.SensitiveField;

import java.time.LocalDate;

@Data
public class Person {
    @SensitiveField
    private String firstName;
    
    @SensitiveField
    private String lastName;
    
    @SensitiveField
    private String middleName;
    
    @SensitiveField
    private LocalDate birthDate;
    
    @SensitiveField
    private String passportSeries;
    
    @SensitiveField
    private String passportNumber;
    
    @SensitiveField
    private String passportIssuedBy;
    
    @SensitiveField
    private LocalDate passportIssuedDate;
    
    @SensitiveField
    private LocalDate passportExpiryDate;
    
    @SensitiveField
    private String passportSubdivisionCode;
    
    @SensitiveField
    private String address;
    
    @SensitiveField
    private String phone;
    
    @SensitiveField
    private String email;
    
    @SensitiveField
    private String inn;
    
    @SensitiveField
    private String snils;
}

