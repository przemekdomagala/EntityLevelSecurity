package EntityLevelSecurity.Users;

import EntityLevelSecurity.Logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class UserBuilder {
    @Autowired
    private ApplicationContext context;

    public User createUser() {
        Logger logger = Logger.getInstance();
        logger.log("Creating user");
        return context.getBean(User.class);
    }
}
