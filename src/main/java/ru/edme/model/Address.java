package ru.edme.model;

import lombok.Data;
import ru.edme.annotation.SensitiveField;
import ru.edme.annotation.SensitiveObject;

import static ru.edme.pattern.MaskingPattern.ADDRESS;

@Data
@SensitiveObject
public class Address {
    @SensitiveField(patterns = ADDRESS)
    private String street;
    @SensitiveField(patterns = ADDRESS)
    private String house;
    @SensitiveField(patterns = ADDRESS)
    private String apartment;
}
