package EntityLevelSecurity.Command;

import EntityLevelSecurity.DatabaseOperations;

import java.util.List;
import java.util.Map;

public class SelectCommand implements Command {
    private final DatabaseOperations database;
    private final String tableName;
    private final Map<String, Object> whereConditions;
    private List<Map<String, Object>> result;

    public SelectCommand(DatabaseOperations database, String tableName, Map<String, Object> whereConditions) {
        this.database = database;
        this.tableName = tableName;
        this.whereConditions = whereConditions;
    }

    @Override
    public void execute() {
        result = database.select(tableName, whereConditions);
        System.out.println("Select executed. Result: " + result);
    }

    public List<Map<String, Object>> getResult() {
        return result;
    }
}
