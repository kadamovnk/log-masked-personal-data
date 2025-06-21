package ru.edme.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.edme.model.Person;

import java.time.LocalDate;

import static ru.edme.custom.logger.Mask.sensitive;
import static ru.edme.pattern.MaskingPattern.ADDRESS;
import static ru.edme.pattern.MaskingPattern.DATE_YYYY_MM_DD;
import static ru.edme.pattern.MaskingPattern.EMAIL;
import static ru.edme.pattern.MaskingPattern.FULL_NAME;
import static ru.edme.pattern.MaskingPattern.INN_10_DIGITS;
import static ru.edme.pattern.MaskingPattern.INN_12_DIGITS;
import static ru.edme.pattern.MaskingPattern.PASSPORT_ISSUED_BY;
import static ru.edme.pattern.MaskingPattern.PASSPORT_NUMBER;
import static ru.edme.pattern.MaskingPattern.PASSPORT_SERIES;
import static ru.edme.pattern.MaskingPattern.PHONE;
import static ru.edme.pattern.MaskingPattern.POSTAL_CODE;
import static ru.edme.pattern.MaskingPattern.SNILS;
import static ru.edme.pattern.MaskingPattern.SUBDIVISION_CODE;

@Service
@Slf4j
public class Logging {
    public void log(Person person) {
        LocalDate birthDate = LocalDate.of(2000, 12, 20);
        LocalDate issueDate = LocalDate.of(2020, 11, 8);
        LocalDate expiryDate = LocalDate.of(2021, 11, 8);
        // passing object to the logger
        log.info("Logging Person: {}", person);
        // passing fields of the object
        System.out.println("\n\n\n");
        log.error("Logging Person fields");
        log.info("First Name: {}", person.getFirstName());
        log.info("Last Name: {}", person.getLastName());
        log.error("Middle Name: {}", person.getMiddleName());
        log.info("Birth Date: {}", person.getBirthDate());
        log.warn("Passport Series: {}", person.getPassportSeries());
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
        
        System.out.println("\n\n\n");
        // passing sensitive fields with masking patterns
        log.error("Logging Person with sensitive data masking");
        log.info("First Name: {}", sensitive("Галина", FULL_NAME));
        log.info("Last Name: {}", sensitive("Искрева", FULL_NAME));
        log.error("Middle Name: {}", sensitive("Петровна", FULL_NAME));
        log.info("Birth Date: {}", sensitive(birthDate, DATE_YYYY_MM_DD));
        log.warn("Passport Series: {}", sensitive("5657", PASSPORT_SERIES));
        log.info("Passport Number: {}", sensitive("656565", PASSPORT_NUMBER));
        log.info("Passport Issued By: {}", sensitive("ГУ МВД РОССИИ ПО КРАСНОДАРСКОМУ КРАЮ", PASSPORT_ISSUED_BY));
        log.info("Passport Issued Date: {}", sensitive(issueDate, DATE_YYYY_MM_DD));
        log.info("Passport Expiry Date: {}", sensitive(expiryDate, DATE_YYYY_MM_DD));
        log.info("Passport Subdivision Code: {}", sensitive("023-230", SUBDIVISION_CODE));
        log.info("Address: {}", sensitive("123456, Российская Федерация, Краснодарский край, Темрюкский район, ул. Ленина, д. 4, кв. 22", POSTAL_CODE, ADDRESS));
        log.info("Phone: {}", sensitive("+7(918)140-54-69", PHONE));
        log.info("Email: {}", sensitive("boor_yonk@mail.ru", EMAIL));
        log.info("INN: {}", sensitive("607080901000", INN_10_DIGITS, INN_12_DIGITS));
        log.info("SNILS: {}", sensitive("314-565-256-20", SNILS));
        
        System.out.println("\n\n\n");
        log.info("Logging Person with sensitive data masking using sensitive() method");
        log.error("Passport: expiry={}, issued={}, series={}, number={}",
                expiryDate,
                sensitive(person.getPassportIssuedDate(), DATE_YYYY_MM_DD),
                sensitive(person.getPassportSeries(), PASSPORT_SERIES),
                sensitive(person.getPassportNumber(), PASSPORT_NUMBER)
        );
        log.info("Client passport details come form NCI: expiry={}, issued={}, series={}, string={}",
                sensitive(person.getPassportExpiryDate(), DATE_YYYY_MM_DD),
                sensitive(person.getPassportIssuedDate(), DATE_YYYY_MM_DD),
                sensitive(person.getPassportSeries(), PASSPORT_SERIES),
                "last line not masked"
        );
    }
}
