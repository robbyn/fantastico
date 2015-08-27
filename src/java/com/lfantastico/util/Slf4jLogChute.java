package com.lfantastico.util;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.slf4j.Logger;

public class Slf4jLogChute implements LogChute {
    private Logger log;

    public Slf4jLogChute(Logger log) {
        this.log = log;
    }

    public void init(RuntimeServices rs) throws Exception {
    }

    public void log(int level, String string) {
        switch (level) {
            case LogChute.TRACE_ID:
                log.trace(string);
                break;
            case LogChute.DEBUG_ID:
                log.debug(string);
                break;
            case LogChute.INFO_ID:
                log.info(string);
                break;
            case LogChute.WARN_ID:
                log.warn(string);
                break;
            case LogChute.ERROR_ID:
                log.error(string);
                break;
        }
    }

    public void log(int level, String string, Throwable e) {
        switch (level) {
            case LogChute.TRACE_ID:
                log.trace(string, e);
                break;
            case LogChute.DEBUG_ID:
                log.debug(string, e);
                break;
            case LogChute.INFO_ID:
                log.info(string, e);
                break;
            case LogChute.WARN_ID:
                log.warn(string, e);
                break;
            case LogChute.ERROR_ID:
                log.error(string, e);
                break;
        }
    }

    public boolean isLevelEnabled(int level) {
        switch (level) {
            case LogChute.TRACE_ID:
                return log.isTraceEnabled();
            case LogChute.DEBUG_ID:
                return log.isDebugEnabled();
            case LogChute.INFO_ID:
                return log.isInfoEnabled();
            case LogChute.WARN_ID:
                return log.isWarnEnabled();
            case LogChute.ERROR_ID:
                return log.isErrorEnabled();
            default:
                return false;
        }
    }
}
