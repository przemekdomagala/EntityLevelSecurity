package EntityLevelSecurity;

import jakarta.persistence.metamodel.EntityType;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Database implements DatabaseOperations{

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

    @Override
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

    @Override
    public void performOperation(String tableName, String operationType) {
        // Simulate performing an operation on the database
        System.out.println("Executing " + operationType + " on " + tableName);
    }

    @Override
    public List<Map<String, Object>> select(String tableName, Map<String, Object> whereConditions) {
        var session = sessionFactory.openSession();
        try {
            // Tworzenie zapytania SQL
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + tableName);

            // Budowanie klauzuli WHERE
            if (!whereConditions.isEmpty()) {
                queryBuilder.append(" WHERE ");
                String conditions = whereConditions.entrySet().stream()
                        .map(entry -> entry.getKey() + " = :" + entry.getKey())
                        .collect(Collectors.joining(" AND "));
                queryBuilder.append(conditions);
            }

            // Tworzenie natywnego zapytania
            var query = session.createNativeQuery(queryBuilder.toString());

            // Przypisywanie parametrów
            whereConditions.forEach(query::setParameter);

            // Wykonanie zapytania i pobranie wyników
            var results = query.getResultList();

            List<String> columnNames = query.unwrap(org.hibernate.query.NativeQuery.class)
                    .getReturnAliases();

            // Mapowanie wyników na listę map
            return results.stream()
                    .map(row -> {
                        var rowMap = new HashMap<String, Object>();

                        // Jeśli wynik to Object[], obsługuj jako wiele kolumn
                        if (row instanceof Object[]) {
                            Object[] rowArray = (Object[]) row;
                            for (int i = 0; i < columnNames.size(); i++) {
                                rowMap.put(columnNames.get(i), rowArray[i]);
                            }
                        } else { // Jeśli wynik to pojedyncze dane (jedna kolumna)
                            rowMap.put(columnNames.get(0), row);
                        }
                        return rowMap;
                    })
                    .collect(Collectors.toList());
        } finally {
            session.close();
        }
    }
    @Override
    public void insert(String tableName, Map<String, Object> data) {
        var session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            var entity = instantiateEntity(tableName, data);
            session.persist(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to insert entity: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void update(String tableName, Map<String, Object> data, Map<String, Object> whereConditions) {
        var session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            var criteriaBuilder = session.getCriteriaBuilder();
            var entityType = getEntityType(tableName);
            var query = criteriaBuilder.createQuery(entityType.getJavaType());
            var root = query.from(entityType.getJavaType());

            // Build where conditions dynamically
            var predicates = whereConditions.entrySet().stream()
                    .map(entry -> criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()))
                    .toArray(jakarta.persistence.criteria.Predicate[]::new);
            query.where(predicates);

            // Fetch the entity to update
            var entity = session.createQuery(query).getSingleResult();

            // Apply updates
            data.forEach((fieldName, fieldValue) -> {
                try {
                    var field = entity.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(entity, fieldValue);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to update field: " + fieldName, e);
                }
            });

            session.merge(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to update entity: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(String tableName, Map<String, Object> whereConditions) {
        var session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            var criteriaBuilder = session.getCriteriaBuilder();
            var entityType = getEntityType(tableName);
            var query = criteriaBuilder.createQuery(entityType.getJavaType());
            var root = query.from(entityType.getJavaType());

            // Build where conditions dynamically
            var predicates = whereConditions.entrySet().stream()
                    .map(entry -> criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()))
                    .toArray(jakarta.persistence.criteria.Predicate[]::new);
            query.where(predicates);

            // Fetch the entity to delete
            var entity = session.createQuery(query).getSingleResult();

            session.remove(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to delete entity: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    // Helper methods
    private EntityType<?> getEntityType(String tableName) {
        return sessionFactory.getMetamodel().getEntities()
                .stream()
                .filter(entity -> entity.getName().equalsIgnoreCase(tableName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Table not found in entity mappings: " + tableName));
    }

    private Object instantiateEntity(String tableName, Map<String, Object> data) {
        try {
            var entityType = getEntityType(tableName);
            var entityClass = entityType.getJavaType();
            var entity = entityClass.getDeclaredConstructor().newInstance();

            for (var entry : data.entrySet()) {
                var field = entityClass.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(entity, entry.getValue());
            }

            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate entity for table: " + tableName, e);
        }
    }

}
