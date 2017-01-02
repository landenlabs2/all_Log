/*
 *  Copyright (c) 2017 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated documentation files (the "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 *  LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 *  NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  @author Dennis Lang  (Jan-2017)
 *  @see http://landenlabs.com
 *
 */

package com.landenlabs.all_log.alog;

import android.text.TextUtils;
import android.util.Log;

/**
 * Alternate logging API, which uses a cascaded API to control and delay presentation.
 * By using the ALog API:
 * <ul>
 *     <li>tag(String tag)</li>
 *     <li>self()</li>
 *     <li>msg(String msg)</li>
 *     <li>msg(String msg, Throwable tr)</li>
 *     <li>fmt(String fmt, Object... args)</li>
 *     <li>cat(String separator, Object... args)</li>
 *     <li>ex(Throwable tr)</li>
 * </ul>
 * <p>
 * Examples:
 * <pre color="red">
 *    ALog.d.msg("log this message);
 *    ALog.e.tag("MyFooClass").fmt("First:%s Last:%s", mFirst, mLast);
 *    ALog.i.out(ALogFileWriter.Default).tag("FooBar").cat(" ", item.name, item.desc, item.tag);
 * </pre>
 * <p>
 * Different
 *
 * @see AppLog
 * @authoer Dennis Lang
 *
 */

public enum ALog {

    // Logging levels (2=V, 3=D, 4=I, 5=W 6=E 7=A)
    v(Log.VERBOSE),
    d(Log.DEBUG),
    i(Log.INFO),
    w(Log.WARN),
    e(Log.ERROR),
    a(Log.ASSERT),
    none(Log.ASSERT+1),
    ;

    /**
     * Minimum level to log.
     */
    public static int minLevel = Log.DEBUG;

    private final int mLevel;
    private final ALogOut mOut = new ALogOut();

    // String mTag;
    private static ThreadLocal<String> mThreadTag = new ThreadLocal<String>();


    ALog(int level) {
        mLevel = level;
    }

    /**
     * Replace default output log target with custom output log target.
     * <p>
     * Ex: ALog.i.out(ALogFileWriter.Default).tag("FooBar").cat(" ", "aaaa", "bbbbb", "ccccc");
     * @param logPrn
     * @return
     */
    public ALog out(ALogOut.LogPrinter logPrn) {
        mOut.outPrn = logPrn;
        return this;
    }

    /**
     * Set log tag, if not set or set with empty string, ALog will auto generate a log from stack trace.
     * @param tagStr
     * @return
     *
     * @see #self()
     */
    public ALog tag(String tagStr) {
        if (mLevel >= minLevel) {
            // mTag = tagStr;                     // !!! This is not Thread Safe !!!!
            mThreadTag.set(tagStr);
        }
        return this;
    }

    /**
     * Set tag to automatically identify self (class which is calling ALog by stack inspection).
     * <p>
     * Warning - Stack inspection is very slow.
     *
     * @return
     * @see #tag(String)
     */
    public ALog self() {
        if (mLevel >= minLevel) {
            // mTag = null;                    // !!! This is not Thread Safe !!!!
            mThreadTag.set(null);
        }
        return this;
    }

    /**
     * If valid log level, Print msg with any previously set tag.
     *
     * @param msg
     * @see #minLevel
     */
    public void msg(String msg) {
        if (mLevel >= minLevel) {
            println(mLevel, findTag(), msg);
        }
    }

    /**
     * If valid log level, Print msg with Throwable and any previously set tag.
     *
     * @param msgStr
     * @param tr
     */
    public void msg(String msgStr, Throwable tr) {
        if (mLevel >= minLevel) {
            cat("\n", msgStr, Log.getStackTraceString(tr));
        }
    }

    /**
     * If valid log leve, Print tag and msg.
     *
     * @param tagStr
     * @param msgStr
     */
    public void tagMsg(String tagStr, String msgStr) {
        if (mLevel >= minLevel) {
            tag(tagStr).msg(msgStr);
        }
    }

    /**
     * If valid log level, format message and print.
     * <p>
     * Ex:   AppLog.LOG.d().fmt("First:%s Last:%s", firstName, lastName);
     * @param fmt
     * @param args
     */
    public void fmt(String fmt, Object... args) {
        if (mLevel >= minLevel) {
            String msgStr = String.format(fmt, args);
            println(mLevel, findTag(), msgStr);
        }
    }

    /**
     * If valid log level, Concatenate strings with <b>separator</b>
     * <p>
     * Ex:   AppLog.LOG.d().cat(" to ", fromTag, toTag);
     * @param separator
     * @param args
     */
    public void cat(String separator, Object... args) {
        if (mLevel >= minLevel) {
            String msgStr = TextUtils.join(separator, args);
            println(mLevel, findTag(), msgStr);
        }
    }

    /**
     * If valid log level, Log Throwable message and stacktrace.
     *
     * @param tr Throwable logged, message and stack.
     */
    public void tr(Throwable tr) {
        cat("\n", tr.getLocalizedMessage(), Log.getStackTraceString(tr));
    }


    /**
     * Print (log) tag and message.
     *
     * @param level
     * @param tag
     * @param msg
     */
    public void println(int level, String tag, String msg) {
        mOut.outPrn.println(mLevel, tag, msg);
    }


    // Helper to make Log tag from stack, provide class and line number.
    private static final String NAME = ALog.class.getCanonicalName();

    /**
     * Make a Log tag by locating class calling ALog.
     *
     * @return  "filename:lineNumber"
     */
    public static String makeTag() {
        String tag = "";
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        for (int idx = 0; idx < ste.length; idx++) {
            StackTraceElement elem = ste[idx];
            if (elem.getMethodName().equals("makeTag") && elem.getClassName().equals(NAME)) {
                while (++idx < ste.length) {
                    elem = ste[idx];
                    if (!elem.getClassName().equals(NAME))
                        break;
                }
                tag = "("+elem.getFileName() + ":" + elem.getLineNumber()+")";
                return tag;
            }
        }
        return tag;
    }

    /**
     * Get previously set <b>tag</b> or generate a tag by inspecting the stacktrace.
     * @return User provided tag or "filename:lineNumber"
     */
    public String findTag() {
        String tag = mThreadTag.get();
       return (tag != null) ? tag : makeTag();
    }
}
