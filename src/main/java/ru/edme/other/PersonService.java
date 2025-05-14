package ru.edme.other;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.edme.annotation.LogMasked;
import ru.edme.model.Person;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class PersonService {
    
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
        log.info("Phone: {}", person.getPhone());
        log.info("Email: {}", person.getEmail());
        log.info("INN: {}", person.getInn());
        log.info("SNILS: {}", person.getSnils());
    }
}
