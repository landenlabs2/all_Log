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

import android.content.Context;

/**
 * Application top level logger, defines flavors of logging used to group common activities
 * together like  Fragments, Network, Parsing, etc.
 * <p>
 * Each enumeration can set its default logging enable state. Logging is only possible if
 * both the items logging is enabled and the global Min Level is below the log level.
 *
 * <p>
 * Example:
 * <Pre color="red">
 *     // Set minimum logging level for all logging.
 *     AppLog.setMinLevel(Log.DEBUG);
 *
 *     AppLog.LOG_FRAGMENTS.d().tag(mTag).cat(" ",
 *          "mapViewWidth=", mapViewWidth, " mapVisibleRegion=", mapVisibleRegion);
 *
 *     AppLog.LOGFILE.d().self().fmt("%s=%s", "key", "vaue");
 *
 *     AppLog.LOG_NETWORK.enabled = true;
 * </Pre>
 *
 * @author  Dennis Lang
 */

public enum AppLog {

    /**
     * Global generic logging to android log system.
     */
    LOG(true),

    /**
     * Global generic logging to a private log file.
     */
    LOGFILE(true, ALogFileWriter.Default),

    // =========== Application specific log flavors ===========

    /**
     * General Fragment logging
     */
    LOG_FRAG(true),

    /**
     * General Network activity
     */
    LOG_NETWORK(false),

    /**
     * General Parinsg activity.
     */
    LOG_PARSING(false),

    ;

    public boolean enabled;
    public final ALogOut out = new ALogOut();

    AppLog(boolean b) {
        enabled = b;
    }

    AppLog(boolean b, ALogOut.LogPrinter outPrn) {
        enabled = b;
        out.outPrn = outPrn;
    }

    public void context(Context c) {
        out.context = c;
        out.outPrn.open(c);
    }

    public ALog v() {
        return enabled ? ALog.v.out(out.outPrn) : ALog.none;
    }
    public ALog d() {
        return enabled ? ALog.d.out(out.outPrn) : ALog.none;
    }
    public ALog i() {
        return enabled ? ALog.i.out(out.outPrn) : ALog.none;
    }
    public ALog w() {
        return enabled ? ALog.w.out(out.outPrn) : ALog.none;
    }
    public ALog e() {
        return enabled ? ALog.e.out(out.outPrn) : ALog.none;
    }
    public ALog a() {
        return enabled ? ALog.a.out(out.outPrn) : ALog.none;
    }

    /**
     * Set global minimum log level
     *
     * @param level
     */
    public static void setMinLevel(int level) {
        ALog.minLevel = level;
    }
}
