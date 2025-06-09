package ru.edme.custom.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import static ru.edme.custom.logger.SensitiveDataMasker.mask;
import static ru.edme.custom.logger.SensitiveDataMasker.maskArgs;

public class SensitiveDataLogger implements Logger {
    private final Logger delegate;
    
    public SensitiveDataLogger(Class<?> clazz) {
        this.delegate = LoggerFactory.getLogger(clazz);
    }
    
    @Override
    public void info(String msg) {
        delegate.info(msg);
    }
    
    @Override
    public void info(String format, Object arg) {
        delegate.info(format, mask(arg));
    }
    
    @Override
    public void info(String format, Object arg1, Object arg2) {
        delegate.info(format, mask(arg1), mask(arg2));
    }
    
    @Override
    public void info(String format, Object... arguments) {
        delegate.info(format, maskArgs(arguments));
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
        delegate.info(marker, format, mask(arg));
    }
    
    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        delegate.info(marker, format, mask(arg1), mask(arg2));
    }
    
    @Override
    public void info(Marker marker, String format, Object... arguments) {
        delegate.info(marker, format, maskArgs(arguments));
    }
    
    @Override
    public void info(Marker marker, String msg, Throwable t) {
        delegate.info(marker, msg, t);
    }
    
    @Override
    public void error(String msg) {
        delegate.error(msg);
    }
    
    @Override
    public void error(String format, Object arg) {
        delegate.error(format, mask(arg));
    }
    
    @Override
    public void error(String format, Object arg1, Object arg2) {
        delegate.error(format, mask(arg1), mask(arg2));
    }
    
    @Override
    public void error(String format, Object... arguments) {
        delegate.error(format, maskArgs(arguments));
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
        delegate.error(marker, format, mask(arg));
    }
    
    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        delegate.error(marker, format, mask(arg1), mask(arg2));
    }
    
    @Override
    public void error(Marker marker, String format, Object... arguments) {
        delegate.error(marker, format, maskArgs(arguments));
    }
    
    @Override
    public void error(Marker marker, String msg, Throwable t) {
        delegate.error(marker, msg, t);
    }
    
    @Override
    public void warn(String msg) {
        delegate.warn(msg);
    }
    
    @Override
    public void warn(String format, Object arg) {
        delegate.warn(format, mask(arg));
    }
    
    @Override
    public void warn(String format, Object arg1, Object arg2) {
        delegate.warn(format, mask(arg1), mask(arg2));
    }
    
    @Override
    public void warn(String format, Object... arguments) {
        delegate.warn(format, maskArgs(arguments));
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
        delegate.warn(marker, format, mask(arg));
    }
    
    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        delegate.warn(marker, format, mask(arg1), mask(arg2));
    }
    
    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        delegate.warn(marker, format, maskArgs(arguments));
    }
    
    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        delegate.warn(marker, msg, t);
    }
    
    @Override
    public void debug(String msg) {
        delegate.debug(msg);
    }
    
    @Override
    public void debug(String format, Object arg) {
        delegate.debug(format, mask(arg));
    }
    
    @Override
    public void debug(String format, Object arg1, Object arg2) {
        delegate.debug(format, mask(arg1), mask(arg2));
    }
    
    @Override
    public void debug(String format, Object... arguments) {
        delegate.debug(format, maskArgs(arguments));
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
        delegate.debug(marker, format, mask(arg));
    }
    
    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        delegate.debug(marker, format, mask(arg1), mask(arg2));
    }
    
    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        delegate.debug(marker, format, maskArgs(arguments));
    }
    
    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        delegate.debug(marker, msg, t);
    }
    
    @Override
    public void trace(String msg) {
        delegate.trace(msg);
    }
    
    @Override
    public void trace(String format, Object arg) {
        delegate.trace(format, mask(arg));
    }
    
    @Override
    public void trace(String format, Object arg1, Object arg2) {
        delegate.trace(format, mask(arg1), mask(arg2));
    }
    
    @Override
    public void trace(String format, Object... arguments) {
        delegate.trace(format, maskArgs(arguments));
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
        delegate.trace(marker, format, mask(arg));
    }
    
    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        delegate.trace(marker, format, mask(arg1), mask(arg2));
    }
    
    @Override
    public void trace(Marker marker, String format, Object... arguments) {
        delegate.trace(marker, format, maskArgs(arguments));
    }
    
    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        delegate.trace(marker, msg, t);
    }
    
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
}
