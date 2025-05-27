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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SensitiveDataLoggerTest {
    private SensitiveDataLogger sensitiveLogger;
    private ListAppender<ILoggingEvent> listAppender;
    private Person testPerson;
    
    @BeforeEach
    public void setUp() throws IOException {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(SensitiveDataLoggerTest.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        rootLogger.addAppender(listAppender);
        sensitiveLogger = SensitiveDataLoggerFactory.getLogger(SensitiveDataLoggerTest.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For handling LocalDate
        
        try (InputStream inputStream = getClass().getResourceAsStream("/test-person.json")) {
            testPerson = objectMapper.readValue(inputStream, Person.class);
        }
    }
    
    @Test
    public void testBasicLogging() {
        // Test standard logging with automatic masking
        sensitiveLogger.info("Email is {}", testPerson.getEmail());
        
        List<ILoggingEvent> logEvents = listAppender.list;
        assertEquals(1, logEvents.size());
        ILoggingEvent logEvent = logEvents.get(0);
        
        assertEquals(Level.INFO, logEvent.getLevel());
        assertTrue(logEvent.getFormattedMessage().contains("***@mail.ru"));
        assertFalse(logEvent.getFormattedMessage().contains("galina.petrovna@mail.ru"));
    }
    
    @Test
    public void testSingleArgumentWithPattern() {
        sensitiveLogger.info("Phone: {}", testPerson.getPhone(), MaskData.with(MaskingPattern.PHONE));
        
        List<ILoggingEvent> logEvents = listAppender.list;
        assertEquals(1, logEvents.size());
        
        String logMessage = logEvents.get(0).getFormattedMessage();
        assertTrue(logMessage.contains("+7(999)***-**-67"));
        assertFalse(logMessage.contains("+7(999)123-45-67"));
    }
    
    @Test
    public void testMultipleArgumentsWithPatterns() {
        sensitiveLogger.info("Passport: series={}, number={}",
                new Object[] {testPerson.getPassportSeries(), testPerson.getPassportNumber()},
                new MaskData[] {
                        MaskData.with(MaskingPattern.PASSPORT_SERIES),
                        MaskData.with(MaskingPattern.PASSPORT_NUMBER)
                }
        );
        
        List<ILoggingEvent> logEvents = listAppender.list;
        assertEquals(1, logEvents.size());
        
        String logMessage = logEvents.get(0).getFormattedMessage();
        assertTrue(logMessage.contains("series=45**"));
        assertTrue(logMessage.contains("number=******"));
    }
    
    @Test
    public void testMultipleArgumentsWithNullPatterns() {
        // Test how logger handles null patterns in the patterns array
        sensitiveLogger.warn("Data: id={}, phone={}, email={}",
                new Object[] {"ABC123", testPerson.getPhone(), testPerson.getEmail()},
                new MaskData[] {
                        null,
                        MaskData.with(MaskingPattern.PHONE),
                        null
                }
        );
        
        List<ILoggingEvent> logEvents = listAppender.list;
        assertEquals(1, logEvents.size());
        
        String logMessage = logEvents.get(0).getFormattedMessage();
        assertEquals(Level.WARN, logEvents.get(0).getLevel());
        assertTrue(logMessage.contains("id=A***3")); // Not masked (null pattern defaults to standard masking)
        assertTrue(logMessage.contains("+7(999)***-**-67")); // Masked with a PHONE pattern
        assertTrue(logMessage.contains("***@mail.ru")); // Default masking for email
    }
    
    @Test
    public void testMultiplePatterns() {
        // Test applying multiple patterns to a single value
        String address = "123456, г. Москва, ул. Ленина, д. 10, кв. 15";
        sensitiveLogger.info("Address: {}", address,
                MaskData.with(MaskingPattern.POSTAL_CODE, MaskingPattern.ADDRESS));
        
        List<ILoggingEvent> logEvents = listAppender.list;
        assertEquals(1, logEvents.size());
        
        String logMessage = logEvents.get(0).getFormattedMessage();
        assertTrue(logMessage.contains("******"));  // Masked postal code
        assertTrue(logMessage.contains("г. ******"));  // Masked region
        assertFalse(logMessage.contains("123456"));  // Original postal code should be masked
    }
    
    @Test
    public void testDifferentLogLevels() {
        // Test that all log levels apply masking with explicit patterns
        sensitiveLogger.info("Info with email: {}", testPerson.getEmail(),
                MaskData.with(MaskingPattern.EMAIL));
        
        sensitiveLogger.warn("Warn with INN: {}", testPerson.getInn(),
                MaskData.with(MaskingPattern.INN_10_DIGITS));
        
        sensitiveLogger.error("Error with SNILS: {}", testPerson.getSnils(),
                MaskData.with(MaskingPattern.SNILS));
        
        List<ILoggingEvent> logEvents = listAppender.list;
        
        int index = 0;
        
        // Check info level
        assertEquals(Level.INFO, logEvents.get(index).getLevel());
        assertTrue(logEvents.get(index).getFormattedMessage().contains("***@mail.ru"));
        index++;
        
        // Check warn level with INN
        assertEquals(Level.WARN, logEvents.get(index).getLevel());
        assertTrue(logEvents.get(index).getFormattedMessage().contains("12********90"));
        index++;
        
        // Check error level with SNILS
        assertEquals(Level.ERROR, logEvents.get(index).getLevel());
        assertTrue(logEvents.get(index).getFormattedMessage().contains("123-***-***-**"));
    }
    
    @Test
    public void testLocalDateMasking() {
        // Test masking of LocalDate objects
        sensitiveLogger.info("Birth date: {}", testPerson.getBirthDate(),
                MaskData.with(MaskingPattern.DATE_YYYY_MM_DD));
        
        List<ILoggingEvent> logEvents = listAppender.list;
        assertEquals(1, logEvents.size());
        
        String logMessage = logEvents.get(0).getFormattedMessage();
        assertTrue(logMessage.contains("1990-**-**"));
        assertFalse(logMessage.contains("1990-05-15"));
    }
    
    @Test
    public void testMoreArgumentsThanPatterns() {
        // Test behavior when more arguments than patterns are provided
        sensitiveLogger.info("User: id={}, email={}, phone={}, address={}",
                new Object[] {"12345", testPerson.getEmail(), testPerson.getPhone(), testPerson.getAddress()},
                new MaskData[] {
                        null,
                        MaskData.with(MaskingPattern.EMAIL),
                        MaskData.with(MaskingPattern.PHONE)
                        // No pattern for address - should use default masking
                }
        );
        
        List<ILoggingEvent> logEvents = listAppender.list;
        assertEquals(1, logEvents.size());
        
        String logMessage = logEvents.get(0).getFormattedMessage();
        assertTrue(logMessage.contains("id=1***5"));
        assertTrue(logMessage.contains("email=***@mail.ru"));
        assertTrue(logMessage.contains("phone=+7(999)***-**-67"));
        // Address should be masked with default masking
        assertFalse(logMessage.contains("ул. Ленина, д. 10, кв. 15"));
    }
    
    @Test
    public void testComplexObjectMasking() {
        // Test masking of complex objects with annotated fields
        sensitiveLogger.info("Person object: {}", testPerson);
        
        List<ILoggingEvent> logEvents = listAppender.list;
        assertEquals(1, logEvents.size());
        
        String logMessage = logEvents.get(0).getFormattedMessage();
        assertTrue(logMessage.contains("firstName=***"));
        assertTrue(logMessage.contains("lastName=***"));
        assertTrue(logMessage.contains("email=***@mail.ru"));
        assertTrue(logMessage.contains("phone=+7(999)***-**-67"));
        assertTrue(logMessage.contains("inn=12********90"));
        assertTrue(logMessage.contains("passportNumber=******"));
    }
    
    @Test
    public void testNoMaskPattern() {
        // Test that sensitive data with NO_MASK pattern is not masked
        String sensitiveEmail = "galina.petrovna@mail.ru";
        String sensitivePhone = "+7(999)123-45-67";

        // Log with NO_MASK pattern
        sensitiveLogger.info("Unmasked email: {}", sensitiveEmail,
                MaskData.with(MaskingPattern.NO_MASK));

        sensitiveLogger.info("Regular vs unmasked: {} vs {}",
                new Object[] {sensitiveEmail, sensitiveEmail},
                new MaskData[] {null, MaskData.with(MaskingPattern.NO_MASK)});

        sensitiveLogger.info("Multiple sensitive items unmasked: email={}, phone={}",
                new Object[] {sensitiveEmail, sensitivePhone},
                new MaskData[] {MaskData.with(MaskingPattern.NO_MASK), MaskData.with(MaskingPattern.NO_MASK)});

        List<ILoggingEvent> logEvents = listAppender.list;
        assertEquals(3, logEvents.size());

        // First log should contain the original email (unmasked)
        assertTrue(logEvents.get(0).getFormattedMessage().contains(sensitiveEmail));
        assertFalse(logEvents.get(0).getFormattedMessage().contains("***@mail.ru"));

        // Second log should have one masked and one unmasked email
        String secondLogMessage = logEvents.get(1).getFormattedMessage();
        assertTrue(secondLogMessage.contains("***@mail.ru"));  // Auto masked
        assertTrue(secondLogMessage.contains(sensitiveEmail));    // NO_MASK applied

        // Third log should have both values unmasked
        String thirdLogMessage = logEvents.get(2).getFormattedMessage();
        assertTrue(thirdLogMessage.contains(sensitiveEmail));
        assertTrue(thirdLogMessage.contains(sensitivePhone));
        assertFalse(thirdLogMessage.contains("***@mail.ru"));
        assertFalse(thirdLogMessage.contains("+7(999)***-**-67"));
    }
}