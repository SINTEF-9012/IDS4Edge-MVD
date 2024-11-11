/*
 *   Copyright (c) 2024 SINTEF
 *   All rights reserved.

 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:

 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.

 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package no.sintef.ids4edge.runtime;

import org.eclipse.edc.spi.monitor.ConsoleColor;
import org.eclipse.edc.spi.monitor.Monitor;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

/**
 * Custom monitor implementation. Outputs the function caller class name to the console.
 */
public class CustomMonitor implements Monitor {

    private static final String SEVERE = "SEVERE";
    private static final String WARNING = "WARNING";
    private static final String INFO = "INFO";
    private static final String DEBUG = "DEBUG";

    private final boolean useColor;

    private final Level level;
    private final String prefix;

    public CustomMonitor() {
        this(true);
    }

    public CustomMonitor(boolean useColor) {
        this(null, Level.DEBUG, useColor);
    }

    public CustomMonitor(@Nullable String runtimeName, Level level) {
        this(runtimeName, level, true);
    }

    public CustomMonitor(@Nullable String runtimeName, Level level, boolean useColor) {
        this.prefix = runtimeName == null ? "" : "[%s] ".formatted(runtimeName);
        this.level = level;
        this.useColor = useColor;
    }

    @Override
    public void severe(Supplier<String> supplier, Throwable... errors) {
        output(SEVERE, supplier, errors);
    }

    @Override
    public void warning(Supplier<String> supplier, Throwable... errors) {
        if (Level.WARNING.value < level.value) {
            return;
        }
        output(WARNING, supplier, errors);
    }

    @Override
    public void info(Supplier<String> supplier, Throwable... errors) {
        if (Level.INFO.value < level.value) {
            return;
        }
        output(INFO, supplier, errors);
    }

    @Override
    public void debug(Supplier<String> supplier, Throwable... errors) {
        if (Level.DEBUG.value < level.value) {
            return;
        }
        output(DEBUG, supplier, errors);
    }

    private static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(CustomMonitor.class.getName()) && !ste.getClassName().equals(Monitor.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                return ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
            }
        }
        return null;
    }

    private void output(String level, Supplier<String> supplier, Throwable... errors) {
        var time = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        var colorCode = useColor ? getColorCode(level) : "";
        var resetCode = useColor ? ConsoleColor.RESET : "";

        //print the caller class name surrounded by square brackets
        System.out.println("%s[%s]".formatted(colorCode, getCallerClassName()));

        //System.out.println(colorCode + getCallerClassName());
        System.out.println(colorCode + prefix + level + " " + time + " " + sanitizeMessage(supplier) + resetCode);
        if (errors != null) {
            for (var error : errors) {
                if (error != null) {
                    System.out.print(colorCode);
                    error.printStackTrace(System.out);
                    System.out.print(resetCode);
                }
            }
        }
    }

    private String getColorCode(String level) {
        return switch (level) {
            case SEVERE -> ConsoleColor.RED;
            case WARNING -> ConsoleColor.YELLOW;
            case INFO -> ConsoleColor.GREEN;
            case DEBUG -> ConsoleColor.BLUE;
            default -> "";
        };
    }

    public enum Level {
        SEVERE(3), WARNING(2), INFO(1), DEBUG(0);

        private final int value;

        Level(int value) {
            this.value = value;
        }
    }
}
