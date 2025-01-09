package EntityLevelSecurity.Roles;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import EntityLevelSecurity.Logger.MyLogger;

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
        MyLogger logger = MyLogger.getInstance();
        logger.log("Role with " + permission.name() + " permission " + table);
        return this;
    }
    public Role withName(String name){
        this.name = name;
        MyLogger logger = MyLogger.getInstance();
        logger.log("Role with " + name);
        return this;
    }

    public String getName() {
        return name;
    }
    //    public static class RoleBuilder {}
}
