package EntityLevelSecurity.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.io.PrintWriter;

/**
 * Logger z chata, ktory PODOBNO zapisuje do pliku i nawet z timestampem
 * Logowanie stringa chyba finalnie moze byc, nie jest duzo krocej
 * jak sobie porobisz metody na wszystko
 * Case do dodania: Exceptions i Nieprawidlowa proba dostepu
 **/

public class Logger {
    private static final Logger instance = new Logger();
    private PrintWriter writer;

    private Logger() {
        try {
            // Open a file in append mode
            writer = new PrintWriter(new FileWriter("application.log", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getInstance() {
        return instance;
    }

    public void log(String message) {
        String timestampedMessage = "[" + LocalDateTime.now() + "] " + message;
        writer.println(timestampedMessage);
        writer.flush();
    }


    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}

