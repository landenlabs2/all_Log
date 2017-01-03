### Android ALog v1.1

***apk v1.1  available in app directory.***

WebSite
[http://landenlabs.com/android/alog/index.html](http://landenlabs.com/android/alog/index.html)

![ALog](http://landenlabs.com//android/alog/alog.png)

ALog is an Android Log wrapper which has the following features:

>   1. Small \- Four small classes, only two required.

>   2. Light weight \- No explict memory allocations. 

>   3. Performant \- No processing beyond if-test when logging disabled or 
log level below min level.  Message presentation (formatting or concatenation) 
only occur if logging is required. 

>   4. Chainable \- Calls are chainable making it easier to extend parameter processing. 

>   5. ThreadSafe \- Logs can be generated in concurrent threads.


***
Overview

The core class ALog is an enumeration. Using an enueration any memory allocation
of the class occurs at startup and subsequent use is light-weight accessing
the singleton instance.

This example code futher extends the logging by wrapping ALog in another 
enumeration AppLog.  The AppLog enumeration is used to create named loggers
which can be used in code classes or packages to manage common actions such as
Network access, Parsing XML, Fragments, etc. 


***
Example Syntax

Call | Logged message
-----| --------------
ALog.i.self().msg("#log info message");  | /I (MainActivity.java:211)(pid):#log info message
ALog.d.tag("myTag1").msg("#log debug message");  | D/myTag1 (pid):#log debug message
ALog.w.tagMsg("myTag2", "#log warning message"); | W/myTag2 (pid):#log warning message
ALog.e.tag("classTag").fmt("#error FIRST:%s LAST:%s", "first", "last"); | E/classTag (pid):#error FIRST:first LAST:last
ALog.i.tag("catTag").cat(" ", "Info", "Log", "a", "new", "msg"); | I/catTag (pid):info Log a new msg

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

Example of using hire level Application level wrapper which provides flavors of logging which gives
an added level of abstraction to setup default behavior.


Call | Logged message
-----| --------------
AppLog.LOG.i().tag("TestTag").msg("Log fixed test"); |
AppLog.LOG_FRAG.i().self().msg("Frag fixed test"); |
AppLog.LOGFILE.e().tag("LogFile").msg("LogFile fixed Test"); |



***
Text Page - shows default fonts and sample sizes:

![Text](http://landenlabs.com//android/all_log/foo.jpg)



