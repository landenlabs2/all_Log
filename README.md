### Android ALog v1.1

***apk v1.1  available in app director to demonstrate and test ALog ***

WebSite
[http://landenlabs.com/android/alog/index.html](http://landenlabs.com/android/alog/index.html)

![ALog](http://landenlabs.com//android/alog/alog.png)

ALog is an Android Log wrapper which has the following features:

>   1. Small \- Four small classes, only one required.

>   2. Light weight \- No explict memory allocations. 

>   3. Performant \- No processing beyond if-test when logging disabled or 
log level below min level.  Message presentation (formatting or concatenation) 
only occur if logging is required. 

>   4. Chainable \- Calls are chainable making it easier to extend parameter processing. 

>   5. ThreadSafe \- Logs can be generated in concurrent threads.


***
Overview

The core class ALog is an enumeration. Using an enueration restricts memory allocation
of the class to startup and subsequent use is light-weight singleton.

Core class:
* ALog  - enumerated log level priorities and format controls.

Optional Extended abstraction classes (files):
* ALogOut - Output target abstraction to support alternate targets such as private file.
* ALogFileWriter - Implementation of private output file target.
* AppLog - Enumeration to manage <b>named</b> logging instances which can have different targets.

***
ALog

<pre>
public enum ALog {

    // Log levels to system log file.
    v(ALog.VERBOSE),
    d(ALog.DEBUG),
    i(ALog.INFO),
    w(ALog.WARN),
    e(ALog.ERROR),
    a(ALog.ASSERT),
    none(ALog.ASSERT+1),

    . . . 
</pre>

ALog's enumeration provides easy access to the standard log level priorities:
* (2) Verbose    
* (3) Debug
* (4) Info
* (5) Warning
* (6) Error
* (7) Assert

Example Syntax of ALog

Call | Logged message
-----| --------------
ALog.i.self().msg("#log info message");  | /I (MainActivity.java:211)(pid):#log info message
ALog.d.tag("myTag1").msg("#log debug message");  | D/myTag1 (pid):#log debug message
ALog.w.tagMsg("myTag2", "#log warning message"); | W/myTag2 (pid):#log warning message
ALog.e.tag("classTag").fmt("#error FIRST:%s LAST:%s", "first", "last"); | E/classTag (pid):#error FIRST:first LAST:last
ALog.i.tag("catTag").cat(" ", "Info", "Log", "a", "new", "msg"); | I/catTag (pid):info Log a new msg

To control logging, set the static global minLevel in ALog. 

<pre>
File: ALog.java:

    /**
     * Minimum level to log.
     */
    public static int minLevel = ALog.VERBOSE;
</pre>

The logging is <b>active</b> if the calling log level exceeds or is equal to the priority of the minimum global log level.

<pre>
    ALog.minLevel = ALog.WARN;
    ALog.d.msg("this log is ignored, below min level");
    ALog.w.msg("this log is sent");
    ALog.e.msg("this log is also sent");
</pre>

The ALog enumeration supports chaining of methods to customize the logging output. 
The <b>tag</b> and <b>self</b> method set the Tag field which persists between calls within a thread. 

Method | Description
------ | -----------
tag(String tagStr) | Set output tag field, default is 'self()'. Setting persists between calls.
self()      | Set output tag to self which parses the call stack and generates a tag value of syntax filename:lineNumber


The key advantage of ALog is the <b>cat</b> and <b>fmt</b>
methods do <b>no</b> processing unless the log is active. 
This avoids wasting time and memory formating strings only to be discarded.

The following methods cause an immediate printing of the log message if logging is active. 

Method | Description
------ | -----------
msg(String msgStr) | Print msgStr using current tag to log target. 
msg(String msgStr, Throwable tr) | Print msgStr using current tag to log target along with Throwable stack trace
tagMsg(String tagStr, String msgStr) | Print tag  and 
cat(String separator, Object... args) | Print concatenaated objects. 
fmt(String fmt, Object... args) | Print formatted objects

Example when Tag set once and default on subsequent calls:

Call | Logged message
-----| --------------
ALog.w.tag("tag3"); |
ALog.w.msg("with tag3, msg#1"); | W/tag3 (pid):msg#1
w.msg("with tag3, msg#2"); | W/tag3 (pid):msg#2

Using following syntax to log an exception:

<pre>
Exception ex = new Exception("test exception");
ALog.e.tr(ex);
</pre>

***
AppLog


This implementation futher extends the logging by wrapping ALog in another 
enumeration called <b>AppLog</b>.  The AppLog enumeration is used to create named loggers
which can be used to log  common actions such as Network access, Parsing XML, Fragments, etc.
The AppLog enumeration futher extends the logging flavors by supporting alternate
logging destinations, such as a private log file or No Logging. 

<pre>
public enum AppLog {

    /**
     * Logging to android log system.
     */
    LOG(LogSys),

    /**
     * Logging to private log file.
     */
    LOGFILE(LogFile),

    // =========== Application specific log flavors ===========

    /**
     * General Fragment logging
     */
    LOG_FRAG(LogSys),

    /**
     * General Network activity (currently disabled).
     */
    LOG_NETWORK(LogNone),

    /**
     * General Parinsg activity (currently disabled).
     */
    LOG_PARSING(LogNone),

</pre>

Example of using hire level Application level wrapper which provides flavors of logging which gives
an added level of abstraction to setup default behavior.


Call | Logged message
-----| --------------
AppLog.LOG.i().tag("TestTag").msg("Log fixed test"); |
AppLog.LOG_FRAG.i().self().msg("Frag fixed test"); |
AppLog.LOGFILE.e().tag("LogFile").msg("LogFile fixed Test"); |


