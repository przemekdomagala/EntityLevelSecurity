package EntityLevelSecurity;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.test.util.ReflectionTestUtils;
import jakarta.persistence.metamodel.Metamodel;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseTest {

    private Database database;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Metamodel metamodel;

    @Mock
    private EntityType<?> entityType1;

    @Mock
    private EntityType<?> entityType2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        database = new Database();
        ReflectionTestUtils.setField(database, "entityManagerFactory", entityManagerFactory);
    }

    @Test
    void testConnect_ShouldInitializeSessionFactory() {
        // Arrange
        when(entityManagerFactory.unwrap(SessionFactory.class)).thenReturn(sessionFactory);

        // Act
        database.connect("dummy-connection-string");

        // Assert
        assertNotNull(database.getSessionFactory());
        assertEquals(sessionFactory, database.getSessionFactory());
    }

    @Test
    void testConnect_ShouldThrowExceptionWhenSessionFactoryNotInitialized() {
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, database::getSessionFactory);
        assertEquals("SessionFactory has not been initialized. Call connect() first.", exception.getMessage());
    }

    @Test
    void testGetTableNames_ShouldReturnTableNamesWhenSessionFactoryIsInitialized() {
        // Arrange
        when(entityManagerFactory.unwrap(SessionFactory.class)).thenReturn(sessionFactory);
        when(sessionFactory.getMetamodel()).thenReturn(metamodel);
        when(metamodel.getEntities()).thenReturn(Set.of(entityType1, entityType2));
        when(entityType1.getName()).thenReturn("Table1");
        when(entityType2.getName()).thenReturn("Table2");

        database.connect("dummy-connection-string");

        // Act
        List<String> tableNames = database.getTableNames();

        // Assert
        Collections.sort(tableNames);
        assertEquals(Arrays.asList("Table1", "Table2"), tableNames);
    }

    @Test
    void testGetTableNames_ShouldThrowExceptionWhenSessionFactoryNotInitialized() {
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, database::getTableNames);
        assertEquals("SessionFactory has not been initialized. Call connect() first.", exception.getMessage());
    }

    @Test
    void testGetTableNames_ShouldReturnEmptyListWhenNoEntities() {
        // Arrange
        when(entityManagerFactory.unwrap(SessionFactory.class)).thenReturn(sessionFactory);
        when(sessionFactory.getMetamodel()).thenReturn(metamodel);
        when(metamodel.getEntities()).thenReturn(Collections.emptySet());

        database.connect("dummy-connection-string");

        // Act
        List<String> tableNames = database.getTableNames();

        // Assert
        assertNotNull(tableNames);
        assertTrue(tableNames.isEmpty());
    }
}