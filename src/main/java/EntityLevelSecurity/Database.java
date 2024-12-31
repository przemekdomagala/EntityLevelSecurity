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
            @SuppressWarnings("unchecked")
            List<Object[]> results = (List<Object[]>) query.getResultList();

            // Mapowanie wyników na listę map
            return results.stream()
                    .map(row -> {
                        var result = new HashMap<String, Object>();
                        for (int i = 0; i < row.length; i++) {
                            result.put("column" + i, row[i]);
                        }
                        return result;
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
            // Budowanie zapytania SQL
            String columns = String.join(", ", data.keySet());
            String values = data.keySet().stream()
                    .map(key -> ":" + key) // Wartości będą wstawiane jako parametry
                    .collect(Collectors.joining(", "));

            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, values);

            var query = session.createNativeQuery(sql);

            // Przypisywanie parametrów do zapytania
            data.forEach(query::setParameter);

            // Wykonanie zapytania
            query.executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to insert data into table " + tableName + ": " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void update(String tableName, Map<String, Object> data, Map<String, Object> whereConditions) {
        var session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            // Budowanie zapytania SQL UPDATE
            String setClause = data.keySet().stream()
                    .map(key -> key + " = :" + key) // Tworzenie wyrażeń SET np. "column = :column"
                    .collect(Collectors.joining(", "));

            String whereClause = whereConditions.keySet().stream()
                    .map(key -> key + " = :" + key) // Tworzenie warunków WHERE np. "column = :column"
                    .collect(Collectors.joining(" AND "));

            String sql = String.format("UPDATE %s SET %s WHERE %s", tableName, setClause, whereClause);

            var query = session.createNativeQuery(sql);

            // Ustawianie parametrów dla klauzuli SET
            data.forEach(query::setParameter);

            // Ustawianie parametrów dla klauzuli WHERE
            whereConditions.forEach(query::setParameter);

            // Wykonanie zapytania
            int rowsAffected = query.executeUpdate();
            System.out.println("Rows updated: " + rowsAffected);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Failed to update table " + tableName + ": " + e.getMessage(), e);
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
