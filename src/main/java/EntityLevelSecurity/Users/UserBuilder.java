package EntityLevelSecurity.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class UserBuilder {
    @Autowired
    private ApplicationContext context;

    public User createUser() {
        return context.getBean(User.class);
    }
}
