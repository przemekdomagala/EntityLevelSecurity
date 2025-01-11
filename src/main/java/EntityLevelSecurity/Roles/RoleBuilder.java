package EntityLevelSecurity.Roles;

import EntityLevelSecurity.Logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RoleBuilder {
    @Autowired
    private ApplicationContext context;

    public Role createRole() {
        Logger logger = Logger.getInstance();
        logger.log("Creating Role");
        return context.getBean(Role.class);
    }
}
