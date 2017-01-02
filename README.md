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

AppLog.LOG.i().tag("Test-l").msg("Log - fixed test");
AppLog.LOG_FRAG.i().msg("Frag fixed test");
AppLog.LOGFILE.e().tag("Test-f").msg("LogFile - fixed Test");

// Low level - logging samples.
ALog.d.msg("#debug log this message");
ALog.d.tag("myTag1").msg("#debug log this message");
ALog.d.tagMsg("myTag2", "#debug log this message");
ALog.e.tag("MyClassTag").fmt("#error First:%s Last:%s", "First", "Last");
i.out(Default).tag("FooBar").cat(" ", "Info-", "Log", "to", "a", "file");

w.tag("myTag3");
w.msg("with tag3, msg1");
w.msg("with tag3, msg2");

Exception ex = new Exception("test exception");
ALog.e.tr(ex);

***
Text Page - shows default fonts and sample sizes:

![Text](http://landenlabs.com//android/all_log/foo.jpg)



