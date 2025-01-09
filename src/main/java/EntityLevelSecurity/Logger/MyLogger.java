package EntityLevelSecurity.Logger;
import EntityLevelSecurity.Roles.Role;

/**
 * W Role i RoleBuilder podejście z getInstance i Dodaniem Loga w konkretnych funkcjach(1)
 * Dla user podejscie z zrobieniem funkcji Dla loggera, ktore są w klasie User tylko uzywane(2)
 * Trzeba obrac jedno podejscie i dociągnąć z nim resztę (w tym poczekac na całość libki i dodać
 * Prawdopodobnie w finalnej bibliotece wolimy podejscie z klasy Logger(3) (druga klasa w tym packagu)
 * Gdzie logi zapisują się z timestampem do pliku. Nie chcemy zeby uzytkownik biblioteki czytal nasze
 * logi w swoim terminalu.
 * **/

public class MyLogger {
    private static MyLogger instance;
    private MyLogger() {

    }

    public static MyLogger getInstance() {
        if (instance == null) {
            /*Thread safe implementation*/
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


    /*Name adding i role Adding metodki dla usera*/
    public void nameAdding(String name) {
        System.out.println("Name added: "+name);
    }

    public void roleAdding(Role role) {
        System.out.println("Role added: "+role.getName());
    }
}
