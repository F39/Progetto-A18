package Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class Logger {

    private static final Logger instance = new Logger();
    private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
    private static Calendar cal = Calendar.getInstance();

    public static Logger getInstance(){
        return instance;
    }

    private Logger(){
        if (instance != null){
            throw new IllegalStateException("Cannot instantiate a new singleton instance of log");
        }
    }

    public static void log(String message){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String logString = "[ " + stackTraceElements[2].getClassName() + " - " + stackTraceElements[2].getMethodName() + " - " + stackTraceElements[2].getLineNumber() + " ] - " + dateFormat.format(cal.getTime()) + " : " + message;
        System.out.println(logString);
    }

}
