package EntityLevelSecurity;

import jakarta.persistence.metamodel.EntityType;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Database {

    private SessionFactory sessionFactory;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    // Method to initialize the SessionFactory
    public void connect(String connectionString) {
        // Spring Boot manages connection properties in `application.properties`,
        // so explicit connection setup isn't needed here.
        // Just unwrap the EntityManagerFactory into a SessionFactory.
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory has not been initialized. Call connect() first.");
        }
        return sessionFactory;
    }

    public List<String> getTableNames() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory has not been initialized. Call connect() first.");
        }
        return sessionFactory.getMetamodel()
                .getEntities()
                .stream()
                .map(EntityType::getName)
                .collect(Collectors.toList());
    }
}
