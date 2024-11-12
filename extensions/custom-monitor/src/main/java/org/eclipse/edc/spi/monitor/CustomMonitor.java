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

package org.eclipse.edc.spi.monitor;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Custom monitor implementation. Outputs the function caller class name to the console.
 */
public class CustomMonitor extends ConsoleMonitor {

    public CustomMonitor() {
        super();
    }

    public CustomMonitor(Level level, boolean useColor) {
        super(level, useColor);
    }

    public CustomMonitor(@Nullable String runtimeName, Level level, boolean useColor) {
        super(runtimeName, level, useColor);
    }

    private static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(CustomMonitor.class.getName()) &&
                    !ste.getClassName().equals(Monitor.class.getName()) &&
                    !ste.getClassName().equals(PrefixMonitor.class.getName()) &&
                    ste.getClassName().indexOf("java.lang.Thread") != 0) {
                return ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
            }
        }
        return null;
    }

    @Override
    public void warning(Supplier<String> supplier, Throwable... errors) {
        System.out.println("[%s]".format(getCallerClassName()));
        super.warning(supplier, errors);
    }

    @Override
    public void info(Supplier<String> supplier, Throwable... errors) {
        System.out.println("[%s]".format(getCallerClassName()));
        super.info(supplier, errors);
    }

    @Override
    public void debug(Supplier<String> supplier, Throwable... errors) {
        System.out.println("[%s]".format(getCallerClassName()));
        super.debug(supplier, errors);
    }

    @Override
    public void severe(Supplier<String> supplier, Throwable... errors) {
        System.out.println("[%s]".format(getCallerClassName()));
        super.severe(supplier, errors);
    }
}
