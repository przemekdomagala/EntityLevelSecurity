package EntityLevelSecurity.Users;
import EntityLevelSecurity.Roles.Role;
import org.springframework.context.annotation.Scope;
import EntityLevelSecurity.Logger.Logger;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class User {
    private String name;

    private Role role;
    public static User CreateNew(){
        return new User();
    }

    public User withName(String name){
        Logger logger = Logger.getInstance();
        logger.log("User's name: " + name);
        this.name = name;
        return this;
    }

    public User withRole(Role role){
        Logger logger = Logger.getInstance();
        logger.log("User's role: " + role.getName());
        this.role = role;
        return this;
    }

    public String getName(){
        return this.name;
    }
    public Role getRole(){
        return this.role;
    }
    //    public static class UserBuilder {}
}
