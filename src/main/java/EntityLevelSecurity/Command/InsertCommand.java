package EntityLevelSecurity.Command;

import EntityLevelSecurity.DatabaseOperations;

import java.util.Map;

public class InsertCommand implements Command {
    private final DatabaseOperations database;
    private final String tableName;
    private final Map<String, Object> data;

    public InsertCommand(DatabaseOperations database, String tableName, Map<String, Object> data) {
        this.database = database;
        this.tableName = tableName;
        this.data = data;
    }

    @Override
    public void execute() {
        database.insert(tableName, data);
        System.out.println("Insert executed on table: " + tableName);
    }
}
