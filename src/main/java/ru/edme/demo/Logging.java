package ru.edme.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.edme.model.Person;
import ru.edme.pattern.MaskingPattern;

import static ru.edme.custom.logger.MaskArg.value;
import static ru.edme.custom.logger.MaskData.pattern;

@Service
@Slf4j
public class Logging {
    //private static final SensitiveDataLogger log = SensitiveDataLoggerFactory.getLogger(Logging.class);
    
    public void log(Person person) {
        log.info("Logging Person: {}", person);
        log.info("First Name: {}", value(person.getFirstName(), pattern(MaskingPattern.FULL_NAME)));
        log.info("Last Name: {}", value(person.getLastName(), pattern(MaskingPattern.FULL_NAME)));
        log.error("Middle Name: {}", value(person.getMiddleName(), pattern(MaskingPattern.FULL_NAME)));
        log.info("Birth Date: {}", value(person.getBirthDate(), pattern(MaskingPattern.DATE_YYYY_MM_DD)));
        log.warn("Passport Series: {}", value(person.getPassportSeries(), pattern(MaskingPattern.PASSPORT_SERIES)));
        log.info("Passport Number: {}", value(person.getPassportNumber(), pattern(MaskingPattern.PASSPORT_NUMBER)));
        log.info("Passport Issued By: {}", value(person.getPassportIssuedBy(), pattern(MaskingPattern.PASSPORT_ISSUED_BY)));
        log.info("Passport Issued Date: {}", value(person.getPassportIssuedDate(), pattern(MaskingPattern.DATE_YYYY_MM_DD)));
        log.info("Passport Expiry Date: {}", value(person.getPassportExpiryDate(), pattern(MaskingPattern.DATE_YYYY_MM_DD)));
        log.info("Passport Subdivision Code: {}", value(person.getPassportSubdivisionCode(), pattern(MaskingPattern.SUBDIVISION_CODE)));
        log.info("Address: {}", value(person.getAddress(), pattern(MaskingPattern.POSTAL_CODE, MaskingPattern.ADDRESS)));
        log.info("Phone: {}", value(person.getPhone(), pattern(MaskingPattern.PHONE)));
        log.info("Phone: {}", person.getPhone());
        log.info("Email: {}", value(person.getEmail(), pattern(MaskingPattern.EMAIL)));
        log.info("INN: {}", value(person.getInn(), pattern(MaskingPattern.INN_10_DIGITS, MaskingPattern.INN_12_DIGITS)));
        log.info("SNILS: {}", value(person.getSnils(), pattern(MaskingPattern.SNILS)));
        log.info("Test: {}", value( "333333333333", pattern(MaskingPattern.INN_10_DIGITS, MaskingPattern.INN_12_DIGITS)));
        log.error("Passport: expiry={}, issued={}, series={}, number={}",
                value(person.getPassportExpiryDate(), null),
                value(person.getPassportIssuedDate(), pattern(MaskingPattern.DATE_YYYY_MM_DD)),
                value(person.getPassportSeries(), pattern(MaskingPattern.PASSPORT_SERIES)),
                value(person.getPassportNumber(), pattern(MaskingPattern.PASSPORT_NUMBER))
        );
        log.error("Passport: expiry={}",
                value(person.getPassportExpiryDate(), pattern(MaskingPattern.DATE_YYYY_MM_DD))
        );
        log.warn("Passport: expiry={}, issued={}, series={}, number={}",
                value(person.getPassportExpiryDate(), pattern(MaskingPattern.DATE_YYYY_MM_DD)),
                value(person.getPassportIssuedDate(), pattern(MaskingPattern.DATE_YYYY_MM_DD)),
                value(person.getPassportSeries(), pattern(MaskingPattern.PASSPORT_SERIES)),
                value(person.getPassportNumber(), pattern(MaskingPattern.PASSPORT_NUMBER))
        );
        log.debug("Debugging Person");
        log.trace("Tracing Person");
        
        log.info("Client passport details come form NCC: expiry={}, issued={}, series={}, number={}",
                value(person.getPassportExpiryDate(), pattern(MaskingPattern.DATE_YYYY_MM_DD)),
                value(person.getPassportIssuedDate(), pattern(MaskingPattern.DATE_YYYY_MM_DD)),
                value(person.getPassportSeries(), pattern(MaskingPattern.PASSPORT_SERIES)),
                value(person.getPassportNumber(), pattern(MaskingPattern.PASSPORT_NUMBER))
        );
    }
}
