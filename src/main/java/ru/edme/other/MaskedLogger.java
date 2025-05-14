package ru.edme.other;

import org.slf4j.*;

public class MaskedLogger {
    private final Logger logger;
    
    public MaskedLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }
    
    public void info(String message) {
        logger.info(Masking.mask(message));
    }
    
    public void error(String message) {
        logger.error(Masking.mask(message));
    }
}
