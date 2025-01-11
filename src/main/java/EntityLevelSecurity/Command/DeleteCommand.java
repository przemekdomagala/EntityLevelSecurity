package EntityLevelSecurity.Command;

import EntityLevelSecurity.DatabaseOperations;

import java.util.Map;

public class DeleteCommand implements Command {
    private final DatabaseOperations database;
    private final String tableName;
    private final Map<String, Object> whereConditions; // Warunki WHERE

    public DeleteCommand(DatabaseOperations database, String tableName, Map<String, Object> whereConditions) {
        this.database = database;
        this.tableName = tableName;
        this.whereConditions = whereConditions;
    }

    @Override
    public void execute() {
        database.delete(tableName, whereConditions);
        System.out.println("Delete executed on table: " + tableName);
    }
}
