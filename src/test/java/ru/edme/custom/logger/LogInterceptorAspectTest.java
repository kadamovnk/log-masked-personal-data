package ru.edme.custom.logger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ru.edme.model.Person;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.edme.custom.logger.Mask.sensitive;
import static ru.edme.pattern.MaskingPattern.ADDRESS;
import static ru.edme.pattern.MaskingPattern.EMAIL;
import static ru.edme.pattern.MaskingPattern.FULL_NAME;
import static ru.edme.pattern.MaskingPattern.MASK;
import static ru.edme.pattern.MaskingPattern.PHONE;
import static ru.edme.pattern.MaskingPattern.POSTAL_CODE;

@Slf4j
public class LogInterceptorAspectTest {
    private ListAppender<ILoggingEvent> listAppender;
    private Person testPerson;

    @BeforeEach
    public void setUp() throws Exception {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(LogInterceptorAspectTest.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        rootLogger.addAppender(listAppender);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try (InputStream inputStream = getClass().getResourceAsStream("/test-person.json")) {
            if (inputStream == null) {
                throw new IllegalStateException("Resource '/test-person.json' not found in test resources.");
            }
            testPerson = objectMapper.readValue(inputStream, Person.class);
        }
    }

    @Test
    public void testAnnotationBasedMasking() {
        log.info("Person: {}", testPerson);
        String msg = listAppender.list.get(0).getFormattedMessage();
        assertTrue(msg.contains("firstName=***"));
        assertTrue(msg.contains("email=***@mail.ru"));
        assertTrue(msg.contains("phone=+7(999)***-**-67"));
        assertTrue(msg.contains("passportSeries=45**"));
        assertTrue(msg.contains("passportNumber=******"));
        assertTrue(msg.contains("snils=123-***-***-**"));
        assertTrue(msg.contains("address=******, Российская Федерация, *** ***, *** р-н, ул. ***, д. ***, кв. ***"));
        assertTrue(msg.contains("passportSubdivisionCode=770-***"));
    }

    @Test
    public void testExplicitMaskArg() {
        log.info("First Name: {}", sensitive(testPerson.getFirstName(), FULL_NAME));
        String msg = listAppender.list.get(0).getFormattedMessage();
        assertTrue(msg.contains("***"));
    }

    @Test
    public void testNoPattern() {
        String email = testPerson.getEmail();
        log.info("Unmasked: {}", sensitive(email));
        String msg = listAppender.list.get(0).getFormattedMessage();
        assertTrue(msg.contains(email));
    }

    @Test
    public void testMultiplePatterns() {
        String address = testPerson.getAddress();
        log.info("Address: {}", sensitive(address, POSTAL_CODE, ADDRESS));
        String msg = listAppender.list.get(0).getFormattedMessage();
        assertTrue(msg.contains("******"));
        assertTrue(msg.contains("*** р-н"));
    }

    @Test
    public void testMultipleArgs() {
        log.info("id={}, email={}, phone={}, info={}",
                sensitive("12345", MASK),
                sensitive(testPerson.getEmail(), EMAIL),
                sensitive(testPerson.getPhone(), PHONE),
                sensitive("Additional info")
        );
        String msg = listAppender.list.get(0).getFormattedMessage();
        assertTrue(msg.contains("id=***"));
        assertTrue(msg.contains("email=***@mail.ru"));
        assertTrue(msg.contains("phone=+7(999)***-**-67"));
    }
}