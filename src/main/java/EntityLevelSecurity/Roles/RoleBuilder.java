package EntityLevelSecurity.Roles;

import EntityLevelSecurity.Logger.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import EntityLevelSecurity.Logger.MyLogger;

@Component
public class RoleBuilder {
    @Autowired
    private ApplicationContext context;

    public Role createRole() {
        MyLogger logger = MyLogger.getInstance();
        logger.log("Creating Role");
        return context.getBean(Role.class);
    }
}
