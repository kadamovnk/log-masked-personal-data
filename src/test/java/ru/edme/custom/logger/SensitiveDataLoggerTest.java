package ru.edme.custom.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ru.edme.model.Person;
import ru.edme.pattern.MaskingPattern;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.edme.custom.logger.MaskArg.value;
import static ru.edme.custom.logger.MaskData.pattern;

public class SensitiveDataLoggerTest {
    private SensitiveDataLogger sensitiveLogger;
    private ListAppender<ILoggingEvent> listAppender;
    private Person testPerson;

    @BeforeEach
    public void setUp() throws Exception {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(SensitiveDataLoggerTest.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        rootLogger.addAppender(listAppender);
        sensitiveLogger = SensitiveDataLoggerFactory.getLogger(SensitiveDataLoggerTest.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try (InputStream inputStream = getClass().getResourceAsStream("/test-person.json")) {
            testPerson = objectMapper.readValue(inputStream, Person.class);
        }
    }

    @Test
    public void testAnnotationBasedMasking() {
        sensitiveLogger.info("Person: {}", testPerson);
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
        sensitiveLogger.info("First Name: {}", value(testPerson.getFirstName(), pattern(MaskingPattern.FULL_NAME)));
        String msg = listAppender.list.get(0).getFormattedMessage();
        assertTrue(msg.contains("***"));
    }

    @Test
    public void testNoMaskPattern() {
        String email = testPerson.getEmail();
        sensitiveLogger.info("Unmasked: {}", value(email, null));
        String msg = listAppender.list.get(0).getFormattedMessage();
        assertTrue(msg.contains(email));
    }

    @Test
    public void testMultiplePatterns() {
        String address = testPerson.getAddress();
        sensitiveLogger.info("Address: {}", value(address, pattern(MaskingPattern.POSTAL_CODE, MaskingPattern.ADDRESS)));
        String msg = listAppender.list.get(0).getFormattedMessage();
        assertTrue(msg.contains("******"));
        assertTrue(msg.contains("*** р-н"));
    }

    @Test
    public void testAllLogLevels() {
        sensitiveLogger.info("Info: {}", testPerson.getEmail());
        sensitiveLogger.warn("Warn: {}", testPerson.getPhone());
        sensitiveLogger.error("Error: {}", testPerson.getSnils());

        List<ILoggingEvent> events = listAppender.list;
        assertEquals(Level.INFO, events.get(0).getLevel());
        assertEquals(Level.WARN, events.get(1).getLevel());
        assertEquals(Level.ERROR, events.get(2).getLevel());
    }

    @Test
    public void testNullAndEmptyValues() {
        sensitiveLogger.info("Null: {}", (Object) null);
        sensitiveLogger.info("Empty: {}", "");
        String msgNull = listAppender.list.get(0).getFormattedMessage();
        String msgEmpty = listAppender.list.get(1).getFormattedMessage();
        assertTrue(msgNull.contains("Null: null"));
        assertTrue(msgEmpty.contains("Empty: "));
    }

    @Test
    public void testMoreArgsThanPatterns() {
        sensitiveLogger.info("id={}, email={}, phone={}, info={}",
                value("12345", pattern(MaskingPattern.MASK)),
                value(testPerson.getEmail(), pattern(MaskingPattern.EMAIL)),
                value(testPerson.getPhone(), pattern(MaskingPattern.PHONE)),
                value("Additional info", null)
        );
        String msg = listAppender.list.get(0).getFormattedMessage();
        assertTrue(msg.contains("id=***"));
        assertTrue(msg.contains("email=***@mail.ru"));
        assertTrue(msg.contains("phone=+7(999)***-**-67"));
    }
}