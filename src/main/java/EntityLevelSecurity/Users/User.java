package EntityLevelSecurity.Users;
import EntityLevelSecurity.Roles.Role;
import org.springframework.context.annotation.Scope;
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
        this.name = name;
        return this;
    }

    public User withRole(Role role){
        this.role = role;
        return this;
    }
    //    public static class UserBuilder {}
}
