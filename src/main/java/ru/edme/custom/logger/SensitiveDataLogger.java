package ru.edme.custom.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import ru.edme.pattern.MaskingPattern;

import java.util.function.BiConsumer;

public class SensitiveDataLogger implements Logger {
    private final Logger delegate;
    
    public SensitiveDataLogger(Class<?> clazz) {
        this.delegate = LoggerFactory.getLogger(clazz);
    }
    
    // ----------- INFO LEVEL METHODS -----------
    public void info(String format, Object arg, MaskData maskData) {
        logSingle(delegate::info, format, arg, maskData);
    }
    
    public void info(String format, Object[] args, MaskData[] patterns) {
        logMultiple(delegate::info, format, args, patterns);
    }
    
    @Override
    public void info(String msg) {
        delegate.info(msg);
    }
    
    @Override
    public void info(String format, Object arg) {
        logSingle(delegate::info, format, arg, null);
    }
    
    @Override
    public void info(String format, Object arg1, Object arg2) {
        logPair(delegate::info, format, arg1, arg2);
    }
    
    @Override
    public void info(String format, Object... arguments) {
        logVarargs(delegate::info, format, arguments);
    }
    
    @Override
    public void info(String msg, Throwable t) {
        delegate.info(msg, t);
    }
    
    @Override
    public void info(Marker marker, String msg) {
        delegate.info(marker, msg);
    }
    
    @Override
    public void info(Marker marker, String format, Object arg) {
        logMarkerSingle(delegate::info, marker, format, arg);
    }
    
    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        logMarkerPair(delegate::info, marker, format, arg1, arg2);
    }
    
    @Override
    public void info(Marker marker, String format, Object... arguments) {
        logMarkerVarargs(delegate::info, marker, format, arguments);
    }
    
    @Override
    public void info(Marker marker, String msg, Throwable t) {
        delegate.info(marker, msg, t);
    }
    
    // ----------- ERROR LEVEL METHODS -----------
    
    public void error(String format, Object arg, MaskData maskData) {
        logSingle(delegate::error, format, arg, maskData);
    }
    
    public void error(String format, Object[] args, MaskData[] patterns) {
        logMultiple(delegate::error, format, args, patterns);
    }
    
    @Override
    public void error(String msg) {
        delegate.error(msg);
    }
    
    @Override
    public void error(String format, Object arg) {
        logSingle(delegate::error, format, arg, null);
    }
    
    @Override
    public void error(String format, Object arg1, Object arg2) {
        logPair(delegate::error, format, arg1, arg2);
    }
    
    @Override
    public void error(String format, Object... arguments) {
        logVarargs(delegate::error, format, arguments);
    }
    
    @Override
    public void error(String msg, Throwable t) {
        delegate.error(msg, t);
    }
    
    @Override
    public void error(Marker marker, String msg) {
        delegate.error(marker, msg);
    }
    
    @Override
    public void error(Marker marker, String format, Object arg) {
        logMarkerSingle(delegate::error, marker, format, arg);
    }
    
    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        logMarkerPair(delegate::error, marker, format, arg1, arg2);
    }
    
    @Override
    public void error(Marker marker, String format, Object... arguments) {
        logMarkerVarargs(delegate::error, marker, format, arguments);
    }
    
    @Override
    public void error(Marker marker, String msg, Throwable t) {
        delegate.error(marker, msg, t);
    }
    
    // ----------- WARN LEVEL METHODS -----------
    
    public void warn(String format, Object arg, MaskData maskData) {
        logSingle(delegate::warn, format, arg, maskData);
    }
    
    public void warn(String format, Object[] args, MaskData[] patterns) {
        logMultiple(delegate::warn, format, args, patterns);
    }
    
    @Override
    public void warn(String msg) {
        delegate.warn(msg);
    }
    
    @Override
    public void warn(String format, Object arg) {
        logSingle(delegate::warn, format, arg, null);
    }
    
    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logPair(delegate::warn, format, arg1, arg2);
    }
    
    @Override
    public void warn(String format, Object... arguments) {
        logVarargs(delegate::warn, format, arguments);
    }
    
    @Override
    public void warn(String msg, Throwable t) {
        delegate.warn(msg, t);
    }
    
    @Override
    public void warn(Marker marker, String msg) {
        delegate.warn(marker, msg);
    }
    
    @Override
    public void warn(Marker marker, String format, Object arg) {
        logMarkerSingle(delegate::warn, marker, format, arg);
    }
    
    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        logMarkerPair(delegate::warn, marker, format, arg1, arg2);
    }
    
    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        logMarkerVarargs(delegate::warn, marker, format, arguments);
    }
    
    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        delegate.warn(marker, msg, t);
    }
    
    // ----------- DEBUG LEVEL METHODS -----------
    
    @Override
    public void debug(String msg) {
        delegate.debug(msg);
    }
    
    @Override
    public void debug(String format, Object arg) {
        logSingle(delegate::debug, format, arg, null);
    }
    
    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logPair(delegate::debug, format, arg1, arg2);
    }
    
    @Override
    public void debug(String format, Object... arguments) {
        logVarargs(delegate::debug, format, arguments);
    }
    
    @Override
    public void debug(String msg, Throwable t) {
        delegate.debug(msg, t);
    }
    
    @Override
    public void debug(Marker marker, String msg) {
        delegate.debug(marker, msg);
    }
    
    @Override
    public void debug(Marker marker, String format, Object arg) {
        logMarkerSingle(delegate::debug, marker, format, arg);
    }
    
    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logMarkerPair(delegate::debug, marker, format, arg1, arg2);
    }
    
    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        logMarkerVarargs(delegate::debug, marker, format, arguments);
    }
    
    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        delegate.debug(marker, msg, t);
    }
    
    // ----------- TRACE LEVEL METHODS -----------
    
    public void trace(String format, Object arg, MaskData maskData) {
        logSingle(delegate::trace, format, arg, maskData);
    }
    
    public void trace(String format, Object[] args, MaskData[] patterns) {
        logMultiple(delegate::trace, format, args, patterns);
    }
    
    @Override
    public void trace(String msg) {
        delegate.trace(msg);
    }
    
    @Override
    public void trace(String format, Object arg) {
        logSingle(delegate::trace, format, arg, null);
    }
    
    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logPair(delegate::trace, format, arg1, arg2);
    }
    
    @Override
    public void trace(String format, Object... arguments) {
        logVarargs(delegate::trace, format, arguments);
    }
    
    @Override
    public void trace(String msg, Throwable t) {
        delegate.trace(msg, t);
    }
    
    @Override
    public void trace(Marker marker, String msg) {
        delegate.trace(marker, msg);
    }
    
    @Override
    public void trace(Marker marker, String format, Object arg) {
        logMarkerSingle(delegate::trace, marker, format, arg);
    }
    
    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        logMarkerPair(delegate::trace, marker, format, arg1, arg2);
    }
    
    @Override
    public void trace(Marker marker, String format, Object... arguments) {
        logMarkerVarargs(delegate::trace, marker, format, arguments);
    }
    
    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        delegate.trace(marker, msg, t);
    }
    
    // ----------- ENABLED CHECKS -----------
    
    @Override
    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }
    
    @Override
    public boolean isTraceEnabled(Marker marker) {
        return delegate.isTraceEnabled(marker);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }
    
    @Override
    public boolean isDebugEnabled(Marker marker) {
        return delegate.isDebugEnabled(marker);
    }
    
    @Override
    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }
    
    @Override
    public boolean isInfoEnabled(Marker marker) {
        return delegate.isInfoEnabled(marker);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }
    
    @Override
    public boolean isWarnEnabled(Marker marker) {
        return delegate.isWarnEnabled(marker);
    }
    
    @Override
    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }
    
    @Override
    public boolean isErrorEnabled(Marker marker) {
        return delegate.isErrorEnabled(marker);
    }
    
    @Override
    public String getName() {
        return delegate.getName();
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
    private Object maskArg(Object arg, MaskData maskData) {
        if (maskData == null) return SensitiveDataMasker.mask(arg);
        
        MaskingPattern[] patterns = maskData.patterns();
        if (patterns.length == 0) return SensitiveDataMasker.mask(arg);
        if (patterns.length == 1) return SensitiveDataMasker.mask(arg, patterns[0]);
        return maskWithMultiplePatterns(arg, patterns);
    }
    
    /**
     * Helper method to mask an argument with multiple patterns
     */
    private Object maskWithMultiplePatterns(Object arg, MaskingPattern[] patterns) {
        return SensitiveDataMasker.maskWithPatterns(arg, patterns);
    }
    
    /**
     * Process arguments, extracting MaskData if present
     */
    private Object[] processMaskingArgs(Object[] arguments) {
        if (arguments == null || arguments.length == 0) return arguments;
        
        // Check if the last argument is MaskData
        if (arguments[arguments.length - 1] instanceof MaskData maskData) {
            Object[] actualArgs = new Object[arguments.length - 1];
            System.arraycopy(arguments, 0, actualArgs, 0, actualArgs.length);
            
            return maskArgs(actualArgs, maskData);
        }
        
        return maskArgs(arguments);
    }
    
    /**
     * Apply specific masking patterns to arguments
     */
    private Object[] maskArgs(Object[] args, MaskData maskData) {
        if (args == null || args.length == 0) return args;
        
        Object[] maskedArgs = new Object[args.length];
        // Apply a pattern to the first arg, standard masking to the rest
        maskedArgs[0] = maskArg(args[0], maskData);
        for (int i = 1; i < args.length; i++) {
            maskedArgs[i] = SensitiveDataMasker.mask(args[i]);
        }
        return maskedArgs;
    }
    
    // ----------- GENERIC LOG METHOD HANDLERS -----------
    
    @FunctionalInterface
    private interface MarkerLogMethod {
        void log(Marker marker, String format, Object arg);
    }
    
    @FunctionalInterface
    private interface MarkerLogArrayMethod {
        void log(Marker marker, String format, Object[] args);
    }
    
    private void logSingle(BiConsumer<String, Object> logMethod, String format, Object arg, MaskData maskData) {
        logMethod.accept(format, maskArg(arg, maskData));
    }
    
    private void logMultiple(BiConsumer<String, Object[]> logMethod, String format, Object[] args, MaskData[] patterns) {
        if (args == null || args.length == 0) {
            logMethod.accept(format, args);
            return;
        }
        
        Object[] maskedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (i < patterns.length && patterns[i] != null) {
                maskedArgs[i] = maskArg(args[i], patterns[i]);
            } else {
                maskedArgs[i] = SensitiveDataMasker.mask(args[i]);
            }
        }
        
        logMethod.accept(format, maskedArgs);
    }
    
    private void logPair(BiConsumer<String, Object[]> logMethod, String format, Object arg1, Object arg2) {
        Object[] args = new Object[2];
        args[0] = SensitiveDataMasker.mask(arg1);
        args[1] = SensitiveDataMasker.mask(arg2);
        logMethod.accept(format, args);
    }
    
    private void logVarargs(BiConsumer<String, Object[]> logMethod, String format, Object... arguments) {
        logMethod.accept(format, processMaskingArgs(arguments));
    }
    
    private void logMarkerSingle(MarkerLogMethod logMethod, Marker marker, String format, Object arg) {
        logMethod.log(marker, format, SensitiveDataMasker.mask(arg));
    }
    
    private void logMarkerPair(MarkerLogArrayMethod logMethod, Marker marker, String format, Object arg1, Object arg2) {
        Object[] args = new Object[2];
        args[0] = SensitiveDataMasker.mask(arg1);
        args[1] = SensitiveDataMasker.mask(arg2);
        logMethod.log(marker, format, args);
    }
    
    private void logMarkerVarargs(MarkerLogArrayMethod logMethod, Marker marker, String format, Object... arguments) {
        logMethod.log(marker, format, maskArgs(arguments));
    }
}
