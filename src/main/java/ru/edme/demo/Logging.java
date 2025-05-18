package ru.edme.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.edme.annotation.LogMasked;
import ru.edme.model.Person;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class Logging {
    
    @LogMasked
    public void log(Person person) {
        log.info("First Name: {}", person.getFirstName());
        log.info("Last Name: {}", person.getLastName());
        log.info("Middle Name: {}", person.getMiddleName());
        log.info("Birth Date: {}", person.getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        log.info("Passport Series: {}", person.getPassportSeries());
        log.info("Passport Number: {}", person.getPassportNumber());
        log.info("Passport Issued By: {}", person.getPassportIssuedBy());
        log.info("Passport Issued Date: {}", person.getPassportIssuedDate());
        log.info("Passport Expiry Date: {}", person.getPassportExpiryDate());
        log.info("Passport Subdivision Code: {}", person.getPassportSubdivisionCode());
        log.info("Address: {}", person.getAddress());
        log.warn("Phone: {}", person.getPhone());
        log.error("Email: {}", person.getEmail());
        log.warn("INN: {}", person.getInn());
        log.error("SNILS: {}", person.getSnils());
        log.info("String Date: {}", person.getStringDate());
        System.out.println("First Name: " + person.getFirstName());
        System.out.println("Last Name: " + person);
    }
}
