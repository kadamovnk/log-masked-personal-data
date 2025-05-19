package ru.edme.custom.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import ru.edme.pattern.MaskingPattern;

import java.time.LocalDate;

public class SensitiveDataLogger implements Logger {
    private final Logger delegate;
    
    public SensitiveDataLogger(Class<?> clazz) {
        this.delegate = LoggerFactory.getLogger(clazz);
    }
    
    /**
     * Helper method to mask arguments with no specific pattern
     */
    private Object[] maskArgs(Object... args) {
        if (args == null || args.length == 0) return args;
        
        Object[] maskedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            maskedArgs[i] = SensitiveDataMasker.mask(args[i]);
        }
        
        return maskedArgs;
    }
    
    /**
     * Helper method to mask an argument with a specific pattern
     */
    private Object maskArg(Object arg, Mask patternHolder) {
        if (patternHolder != null) {
            MaskingPattern[] patterns = patternHolder.patterns();
            if (patterns.length == 1) {
                return SensitiveDataMasker.mask(arg, patterns[0]);
            } else if (patterns.length > 1) {
                return maskWithMultiplePatterns(arg, patterns);
            }
        }
        return SensitiveDataMasker.mask(arg);
    }
    
    /**
     * Helper method to mask an argument with multiple patterns
     */
    private Object maskWithMultiplePatterns(Object arg, MaskingPattern[] patterns) {
        if (arg == null) return "null";
        
        if (arg instanceof String) {
            String result = (String) arg;
            for (MaskingPattern pattern : patterns) {
                result = pattern.applyTo(result);
            }
            return result;
        } else if (arg instanceof LocalDate) {
            LocalDate date = (LocalDate) arg;
            for (MaskingPattern pattern : patterns) {
                if (pattern == MaskingPattern.DATE_YYYY_MM_DD) {
                    return SensitiveDataMasker.mask(date, pattern);
                }
            }
            return SensitiveDataMasker.mask(date, MaskingPattern.DATE_YYYY_MM_DD);
        } else {
            return patterns.length > 0 ?
                    SensitiveDataMasker.mask(arg, patterns[0]) :
                    SensitiveDataMasker.mask(arg);
        }
    }
    
    private Object[] processMaskingArgs(Object[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return arguments;
        }
        
        if (arguments[arguments.length - 1] instanceof Mask) {
            Mask pattern = (Mask) arguments[arguments.length - 1];
            Object[] actualArgs = new Object[arguments.length - 1];
            System.arraycopy(arguments, 0, actualArgs, 0, actualArgs.length);
            
            if (actualArgs.length > 0) {
                actualArgs[0] = maskArg(actualArgs[0], pattern);
                for (int i = 1; i < actualArgs.length; i++) {
                    actualArgs[i] = SensitiveDataMasker.mask(actualArgs[i]);
                }
            }
            return actualArgs;
        } else {
            return maskArgs(arguments);
        }
    }
    
    // --- INFO ---
    @Override
    public void info(String msg) { delegate.info(msg); }
    
    public void info(String format, Object arg, Mask pattern) {
        delegate.info(format, maskArg(arg, pattern));
    }
    
    @Override
    public void info(String format, Object arg) {
        delegate.info(format, SensitiveDataMasker.mask(arg));
    }
    
    public void info(String format, Object arg1, Object arg2, Mask pattern) {
        if (pattern != null) {
            delegate.info(format, maskArg(arg1, pattern), SensitiveDataMasker.mask(arg2));
        } else {
            delegate.info(format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
        }
    }
    
    @Override
    public void info(String format, Object arg1, Object arg2) {
        delegate.info(format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
    }
    
    @Override
    public void info(String format, Object... arguments) {
        delegate.info(format, processMaskingArgs(arguments));
    }
    
    @Override
    public void info(String msg, Throwable t) { delegate.info(msg, t); }
    @Override
    public void info(Marker marker, String msg) { delegate.info(marker, msg); }
    @Override
    public void info(Marker marker, String format, Object arg) {
        delegate.info(marker, format, SensitiveDataMasker.mask(arg));
    }
    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        delegate.info(marker, format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
    }
    @Override
    public void info(Marker marker, String format, Object... arguments) {
        delegate.info(marker, format, maskArgs(arguments));
    }
    @Override
    public void info(Marker marker, String msg, Throwable t) { delegate.info(marker, msg, t); }
    
    // --- ERROR ---
    @Override
    public void error(String msg) { delegate.error(msg); }
    public void error(String format, Object arg, Mask pattern) {
        delegate.error(format, maskArg(arg, pattern));
    }
    @Override
    public void error(String format, Object arg) {
        delegate.error(format, SensitiveDataMasker.mask(arg));
    }
    public void error(String format, Object arg1, Object arg2, Mask pattern) {
        if (pattern != null) {
            delegate.error(format, maskArg(arg1, pattern), SensitiveDataMasker.mask(arg2));
        } else {
            delegate.error(format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
        }
    }
    @Override
    public void error(String format, Object arg1, Object arg2) {
        delegate.error(format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
    }
    @Override
    public void error(String format, Object... arguments) {
        delegate.error(format, processMaskingArgs(arguments));
    }
    @Override
    public void error(String msg, Throwable t) { delegate.error(msg, t); }
    @Override
    public void error(Marker marker, String msg) { delegate.error(marker, msg); }
    @Override
    public void error(Marker marker, String format, Object arg) {
        delegate.error(marker, format, SensitiveDataMasker.mask(arg));
    }
    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        delegate.error(marker, format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
    }
    @Override
    public void error(Marker marker, String format, Object... arguments) {
        delegate.error(marker, format, maskArgs(arguments));
    }
    @Override
    public void error(Marker marker, String msg, Throwable t) { delegate.error(marker, msg, t); }
    
    // --- WARN ---
    @Override
    public void warn(String msg) { delegate.warn(msg); }
    public void warn(String format, Object arg, Mask pattern) {
        delegate.warn(format, maskArg(arg, pattern));
    }
    @Override
    public void warn(String format, Object arg) {
        delegate.warn(format, SensitiveDataMasker.mask(arg));
    }
    public void warn(String format, Object arg1, Object arg2, Mask pattern) {
        if (pattern != null) {
            delegate.warn(format, maskArg(arg1, pattern), SensitiveDataMasker.mask(arg2));
        } else {
            delegate.warn(format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
        }
    }
    @Override
    public void warn(String format, Object arg1, Object arg2) {
        delegate.warn(format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
    }
    @Override
    public void warn(String format, Object... arguments) {
        delegate.warn(format, processMaskingArgs(arguments));
    }
    @Override
    public void warn(String msg, Throwable t) { delegate.warn(msg, t); }
    @Override
    public void warn(Marker marker, String msg) { delegate.warn(marker, msg); }
    @Override
    public void warn(Marker marker, String format, Object arg) {
        delegate.warn(marker, format, SensitiveDataMasker.mask(arg));
    }
    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        delegate.warn(marker, format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
    }
    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        delegate.warn(marker, format, maskArgs(arguments));
    }
    @Override
    public void warn(Marker marker, String msg, Throwable t) { delegate.warn(marker, msg, t); }
    
    // --- DEBUG ---
    @Override
    public void debug(String msg) { delegate.debug(msg); }
    public void debug(String format, Object arg, Mask pattern) {
        delegate.debug(format, maskArg(arg, pattern));
    }
    @Override
    public void debug(String format, Object arg) {
        delegate.debug(format, SensitiveDataMasker.mask(arg));
    }
    public void debug(String format, Object arg1, Object arg2, Mask pattern) {
        if (pattern != null) {
            delegate.debug(format, maskArg(arg1, pattern), SensitiveDataMasker.mask(arg2));
        } else {
            delegate.debug(format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
        }
    }
    @Override
    public void debug(String format, Object arg1, Object arg2) {
        delegate.debug(format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
    }
    @Override
    public void debug(String format, Object... arguments) {
        delegate.debug(format, processMaskingArgs(arguments));
    }
    @Override
    public void debug(String msg, Throwable t) { delegate.debug(msg, t); }
    @Override
    public void debug(Marker marker, String msg) { delegate.debug(marker, msg); }
    @Override
    public void debug(Marker marker, String format, Object arg) {
        delegate.debug(marker, format, SensitiveDataMasker.mask(arg));
    }
    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        delegate.debug(marker, format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
    }
    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        delegate.debug(marker, format, maskArgs(arguments));
    }
    @Override
    public void debug(Marker marker, String msg, Throwable t) { delegate.debug(marker, msg, t); }
    
    // --- TRACE ---
    @Override
    public void trace(String msg) { delegate.trace(msg); }
    public void trace(String format, Object arg, Mask pattern) {
        delegate.trace(format, maskArg(arg, pattern));
    }
    @Override
    public void trace(String format, Object arg) {
        delegate.trace(format, SensitiveDataMasker.mask(arg));
    }
    public void trace(String format, Object arg1, Object arg2, Mask pattern) {
        if (pattern != null) {
            delegate.trace(format, maskArg(arg1, pattern), SensitiveDataMasker.mask(arg2));
        } else {
            delegate.trace(format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
        }
    }
    @Override
    public void trace(String format, Object arg1, Object arg2) {
        delegate.trace(format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
    }
    @Override
    public void trace(String format, Object... arguments) {
        delegate.trace(format, processMaskingArgs(arguments));
    }
    @Override
    public void trace(String msg, Throwable t) { delegate.trace(msg, t); }
    @Override
    public void trace(Marker marker, String msg) { delegate.trace(marker, msg); }
    @Override
    public void trace(Marker marker, String format, Object arg) {
        delegate.trace(marker, format, SensitiveDataMasker.mask(arg));
    }
    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        delegate.trace(marker, format, SensitiveDataMasker.mask(arg1), SensitiveDataMasker.mask(arg2));
    }
    @Override
    public void trace(Marker marker, String format, Object... arguments) {
        delegate.trace(marker, format, maskArgs(arguments));
    }
    @Override
    public void trace(Marker marker, String msg, Throwable t) { delegate.trace(marker, msg, t); }
    
    // --- ENABLED CHECKS ---
    @Override
    public boolean isTraceEnabled() { return delegate.isTraceEnabled(); }
    @Override
    public boolean isTraceEnabled(Marker marker) { return delegate.isTraceEnabled(marker); }
    @Override
    public boolean isDebugEnabled() { return delegate.isDebugEnabled(); }
    @Override
    public boolean isDebugEnabled(Marker marker) { return delegate.isDebugEnabled(marker); }
    @Override
    public boolean isInfoEnabled() { return delegate.isInfoEnabled(); }
    @Override
    public boolean isInfoEnabled(Marker marker) { return delegate.isInfoEnabled(marker); }
    @Override
    public boolean isWarnEnabled() { return delegate.isWarnEnabled(); }
    @Override
    public boolean isWarnEnabled(Marker marker) { return delegate.isWarnEnabled(marker); }
    @Override
    public boolean isErrorEnabled() { return delegate.isErrorEnabled(); }
    @Override
    public boolean isErrorEnabled(Marker marker) { return delegate.isErrorEnabled(marker); }
    
    @Override
    public String getName() { return delegate.getName(); }
}
