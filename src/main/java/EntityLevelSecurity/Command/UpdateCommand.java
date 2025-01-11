package EntityLevelSecurity.Command;

import EntityLevelSecurity.DatabaseOperations;

import java.util.Map;

public class UpdateCommand implements Command {
    private final DatabaseOperations database;
    private final String tableName;
    private final Map<String, Object> data; // Dane do zaktualizowania
    private final Map<String, Object> whereConditions; // Warunki WHERE

    public UpdateCommand(DatabaseOperations database, String tableName, Map<String, Object> data, Map<String, Object> whereConditions) {
        this.database = database;
        this.tableName = tableName;
        this.data = data;
        this.whereConditions = whereConditions;
    }

    @Override
    public void execute() {
        database.update(tableName, data, whereConditions);
        System.out.println("Update executed on table: " + tableName);
    }
}
