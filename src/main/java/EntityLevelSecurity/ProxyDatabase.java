package EntityLevelSecurity;

import EntityLevelSecurity.Roles.Permission;
import EntityLevelSecurity.Users.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProxyDatabase implements DatabaseOperations {
    private final Database database;
    private final User currentUser; // Assume injected or loaded based on user session

    public ProxyDatabase(Database database, User currentUser) {
        this.database = database;
        this.currentUser = currentUser;
    }

    @Override
    public void connect(String connectionString) {
        database.connect(connectionString);
    }

    @Override
    public List<String> getTableNames() {
        // Example: All users can retrieve table names
        return database.getTableNames();
    }

    private boolean hasPermission(String tableName, Permission requiredPermission) {
        return currentUser.getRole().getPermissions()
                .getOrDefault(tableName, Permission.NO_PERMISSION)
                .ordinal() >= requiredPermission.ordinal();
    }

    @Override
    public List<Map<String, Object>> select(String tableName, Map<String, Object> whereConditions) {
        checkPermission(tableName, Permission.READ);
        return database.select(tableName, whereConditions);
    }

    @Override
    public void insert(String tableName, Map<String, Object> data) {
        checkPermission(tableName, Permission.READ_WRITE);
        database.insert(tableName, data);
    }

    @Override
    public void update(String tableName, Map<String, Object> data, Map<String, Object> whereConditions) {
        checkPermission(tableName, Permission.MODIFY);
        database.update(tableName, data, whereConditions);
    }

    @Override
    public void delete(String tableName, Map<String, Object> whereConditions) {
        checkPermission(tableName, Permission.MODIFY);
        database.delete(tableName, whereConditions);
    }

    private void checkPermission(String tableName, Permission requiredPermission) {
        if (!hasPermission(tableName, requiredPermission)) {
            throw new SecurityException("Insufficient permissions");
        }
    }
}