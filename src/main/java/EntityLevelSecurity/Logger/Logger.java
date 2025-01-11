package EntityLevelSecurity.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Logger {
    private PrintWriter writer;
    private static Logger instance;
    private Logger() {
        try {
            // Open a file in append mode
            writer = new PrintWriter(new FileWriter("application.log", true));
        } catch (IOException e) {
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            /*Thread safe implementation*/
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        String fullMessage = "[" + LocalDateTime.now() + "] " + message;
        writer.println(fullMessage);
        writer.flush();
    }

    public void logException(Exception exception){
        String fullMessage = "[" + LocalDateTime.now() + "] " + exception.getMessage();
        writer.println(fullMessage);
        writer.flush();
    }

    public void printLog(String message){
        System.out.println("[" + LocalDateTime.now() + "]" + " Log: " + message);
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
    public void clearLogs(){
        try{
            PrintWriter overWriter = new PrintWriter(new FileWriter("application.log", false));
            overWriter.write("");
            overWriter.flush();
            overWriter.close();
        }
        catch(IOException e){
        }

    }


}
