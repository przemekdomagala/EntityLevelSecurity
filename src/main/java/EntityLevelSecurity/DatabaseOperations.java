package EntityLevelSecurity;

import java.util.List;
import java.util.Map;

public interface DatabaseOperations {
    void connect(String connectionString);

    List<String> getTableNames();

    List<Map<String, Object>> select(String tableName, Map<String, Object> whereConditions);

    void insert(String tableName, Map<String, Object> data);

    void update(String tableName, Map<String, Object> data, Map<String, Object> whereConditions);

    void delete(String tableName, Map<String, Object> whereConditions);
}