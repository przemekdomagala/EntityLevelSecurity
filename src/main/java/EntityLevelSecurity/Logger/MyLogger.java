package EntityLevelSecurity.Logger;

public class MyLogger {
    private static MyLogger instance;
    private MyLogger() {

    }

    public static MyLogger getInstance() {
        if (instance == null) {
            synchronized (MyLogger.class) {
                if (instance == null) {
                    instance = new MyLogger();
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("Log: "+message);
    }
}
