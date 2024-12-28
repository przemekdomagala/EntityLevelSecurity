package EntityLevelSecurity.Roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RoleBuilder {
    @Autowired
    private ApplicationContext context;

    public Role createRole() {
        return context.getBean(Role.class);
    }
}
