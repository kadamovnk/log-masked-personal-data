package ru.edme.demo;

import org.springframework.stereotype.Service;
import ru.edme.custom.logger.Mask;
import ru.edme.custom.logger.SensitiveDataLogger;
import ru.edme.custom.logger.SensitiveDataLoggerFactory;
import ru.edme.pattern.MaskingPattern;
import ru.edme.model.Person;

@Service
public class Logging {
    
    private static final SensitiveDataLogger log = SensitiveDataLoggerFactory.getLogger(Logging.class);
    
//    @LogMasked
    public void log(Person person) {
        log.info("Logging Person: {}", person);
        log.info("First Name: {}", person.getFirstName(), Mask.with(MaskingPattern.FULL_NAME));
        log.info("Last Name: {}", person.getLastName(), Mask.with(MaskingPattern.FULL_NAME));
        log.info("Middle Name: {}", person.getMiddleName(), Mask.with(MaskingPattern.FULL_NAME));
        log.info("Birth Date: {}", person.getBirthDate(), Mask.with(MaskingPattern.DATE_YYYY_MM_DD));
        log.info("Passport Series: {}", person.getPassportSeries(), Mask.with(MaskingPattern.PASSPORT_SERIES));
        log.info("Passport Number: {}", person.getPassportNumber(), Mask.with(MaskingPattern.PASSPORT_NUMBER));
        log.info("Passport Issued By: {}", person.getPassportIssuedBy(), Mask.with(MaskingPattern.PASSPORT_ISSUED_BY));
        log.info("Passport Issued Date: {}", person.getPassportIssuedDate(), Mask.with(MaskingPattern.DATE_YYYY_MM_DD));
        log.info("Passport Expiry Date: {}", person.getPassportExpiryDate(), Mask.with(MaskingPattern.DATE_YYYY_MM_DD));
        log.info("Passport Subdivision Code: {}", person.getPassportSubdivisionCode(), Mask.with(MaskingPattern.SUBDIVISION_CODE));
        log.info("Address: {}", person.getAddress(), Mask.with(MaskingPattern.POSTAL_CODE, MaskingPattern.ADDRESS));
        log.info("Phone: {}", person.getPhone(), Mask.with(MaskingPattern.PHONE));
        log.info("Email: {}", person.getEmail(), Mask.with(MaskingPattern.EMAIL));
        log.info("INN: {}", person.getInn(), Mask.with(MaskingPattern.INN_10_DIGITS, MaskingPattern.INN_12_DIGITS));
        log.info("SNILS: {}", person.getSnils(), Mask.with(MaskingPattern.SNILS));
    }
}
