package com.changhong.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ava on 2017/4/5.
 * email:zhyx2014@yeah.net
 */
public class LogUtil {
    private final static Logger log = LoggerFactory.getLogger("logutil");

    public static void info(Object... args) {
        StringBuffer sb = new StringBuffer();
        for (Object arg : args) {
            sb.append(arg == null ? "null" : arg.toString()).append("-----");
        }
        log.info(sb.toString());
    }

    public static void debug(Object... args) {
        StringBuffer sb = new StringBuffer();
        for (Object arg : args) {
            sb.append(arg == null ? "null" : arg.toString()).append("-----");
        }
        log.debug(sb.toString());
    }

    public static void error(Object... args) {
        StringBuffer sb = new StringBuffer();
        for (Object arg : args) {
            sb.append(arg == null ? "null" : arg.toString()).append("-----");
        }
        log.error(sb.toString());
    }

    public static void printErr(Object... args) {
        StringBuffer sb = new StringBuffer();
        for (Object arg : args) {
            sb.append(arg == null ? "null" : arg.toString())
                    .append("-----")
            ;
        }
        System.err.println(sb);
    }
}
