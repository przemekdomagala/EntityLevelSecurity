package EntityLevelSecurity.Roles;
import EntityLevelSecurity.Logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class Role {

    private String name;

    Map<String, Permission> permissions = new HashMap<String, Permission>();

    public static Role CreateNew(){
        return new Role();
    }

    public Role withPermission(Permission permission, String table){
        permissions.put(table, permission);
        return this;
    }
    public Role withName(String name){
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
    //    public static class RoleBuilder {}
}
