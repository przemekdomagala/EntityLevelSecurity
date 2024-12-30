package EntityLevelSecurity.Roles;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import EntityLevelSecurity.Logger.Logger;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class Role {

    private String name;

    Map<String, Permission> permissions = new HashMap<String, Permission>();

    //Is it needed?
//    public static Role CreateNew(){
//        return new Role();
//    }

    public Role withPermission(Permission permission, String table){
        permissions.put(table, permission);
        Logger logger = Logger.getInstance();
        logger.log("Role with " + permission.name() + " permission on table " + table);
        return this;
    }
    public Role withName(String name){
        this.name = name;
        Logger logger = Logger.getInstance();
        logger.log("Role's name: " + name);
        return this;
    }

    public String getName() {
        return name;
    }

    public Map<String, Permission> getPermissions() {
        return permissions;
    }

    //    public static class RoleBuilder {}
    public Map<String, Permission> getPermissions(){
        return this.permissions;
    }
}
