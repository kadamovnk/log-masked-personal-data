package ru.edme.demo;

import org.springframework.stereotype.Service;
import ru.edme.custom.logger.MaskData;
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
        log.info("First Name: {}", person.getFirstName(), MaskData.with(MaskingPattern.FULL_NAME));
        log.debug("Last Name: {}", person.getLastName(), MaskData.with(MaskingPattern.FULL_NAME));
        log.error("Middle Name: {}", person.getMiddleName(), MaskData.with(MaskingPattern.FULL_NAME));
        log.trace("Birth Date: {}", person.getBirthDate(), MaskData.with(MaskingPattern.DATE_YYYY_MM_DD));
        log.warn("Passport Series: {}", person.getPassportSeries(), MaskData.with(MaskingPattern.PASSPORT_SERIES));
        log.info("Passport Number: {}", person.getPassportNumber(), MaskData.with(MaskingPattern.PASSPORT_NUMBER));
        log.info("Passport Issued By: {}", person.getPassportIssuedBy(), MaskData.with(MaskingPattern.PASSPORT_ISSUED_BY));
        log.info("Passport Issued Date: {}", person.getPassportIssuedDate(), MaskData.with(MaskingPattern.DATE_YYYY_MM_DD));
        log.info("Passport Expiry Date: {}", person.getPassportExpiryDate(), MaskData.with(MaskingPattern.DATE_YYYY_MM_DD));
        log.info("Passport Subdivision Code: {}", person.getPassportSubdivisionCode(), MaskData.with(MaskingPattern.SUBDIVISION_CODE));
        log.info("Address: {}", person.getAddress(), MaskData.with(MaskingPattern.POSTAL_CODE, MaskingPattern.ADDRESS));
        log.info("Phone: {}", person.getPhone(), MaskData.with(MaskingPattern.PHONE));
        log.info("Phone: {}", person.getPhone());
        log.info("Email: {}", person.getEmail(), MaskData.with(MaskingPattern.EMAIL));
        log.info("INN: {}", person.getInn(), MaskData.with(MaskingPattern.INN_10_DIGITS, MaskingPattern.INN_12_DIGITS));
        log.info("SNILS: {}", person.getSnils(), MaskData.with(MaskingPattern.SNILS));
        log.info("Test: {}", "333333333333", MaskData.with(MaskingPattern.INN_10_DIGITS, MaskingPattern.INN_12_DIGITS));
        log.info("Passport: expiry={}",
                new Object[] {person.getPassportExpiryDate()},
                new MaskData[] {
                        MaskData.with(MaskingPattern.DATE_YYYY_MM_DD)
                }
        );
        log.warn("Passport: expiry={}, issued={}, series={}, number={}",
                new Object[] {person.getPassportExpiryDate(), person.getPassportIssuedDate(), person.getPassportSeries(), person.getPassportNumber()},
                new MaskData[] {
                        null,
                        MaskData.with(MaskingPattern.DATE_YYYY_MM_DD),
                        MaskData.with(MaskingPattern.PASSPORT_SERIES),
                        MaskData.with(MaskingPattern.PASSPORT_NUMBER)
                }
        );
        log.debug("Passport: expiry={}, issued={}, series={}, number={}",
                new Object[] {person.getPassportExpiryDate(), person.getPassportIssuedDate(), person.getPassportSeries(), person.getPassportNumber()},
                new MaskData[] {
                        null,
                        MaskData.with(MaskingPattern.DATE_YYYY_MM_DD, MaskingPattern.DATE_YYYY_MM_DD),
                        MaskData.with(MaskingPattern.PASSPORT_SERIES),
                        MaskData.with(MaskingPattern.PASSPORT_NUMBER)
                }
        );
        log.error("Passport: expiry={}",
                new Object[] {person.getPassportExpiryDate()},
                new MaskData[] {
                        null,
                        MaskData.with(MaskingPattern.DATE_YYYY_MM_DD),
                        MaskData.with(MaskingPattern.PASSPORT_SERIES),
                        MaskData.with(MaskingPattern.PASSPORT_NUMBER)
                }
        );
        log.trace("Passport: expiry={}, issued={}, series={}, number={}",
                new Object[] {person.getPassportExpiryDate(), person.getPassportIssuedDate(), person.getPassportSeries(), person.getPassportNumber()},
                new MaskData[] {
                        MaskData.with(MaskingPattern.DATE_YYYY_MM_DD),
                        MaskData.with(MaskingPattern.DATE_YYYY_MM_DD),
                        MaskData.with(MaskingPattern.PASSPORT_SERIES),
                        MaskData.with(MaskingPattern.PASSPORT_NUMBER)
                }
        );
    }
}
