package EntityLevelSecurity.Users;
import EntityLevelSecurity.Roles.Role;
import org.springframework.context.annotation.Scope;
import EntityLevelSecurity.Logger.MyLogger;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class User {
    private String name;

    private Role role;
    public static User CreateNew(){
        return new User();
    }

    /**Jak widac na zalaczonym obrazku, chuja jest krocej robiac wszystkiemu specjalne metody w loggerze**/
    public User withName(String name){
        MyLogger logger = MyLogger.getInstance();
        logger.nameAdding(name);
        this.name = name;
        return this;
    }

    public User withRole(Role role){
        MyLogger logger = MyLogger.getInstance();
        logger.roleAdding(role);
        this.role = role;
        return this;
    }
    //    public static class UserBuilder {}
}
