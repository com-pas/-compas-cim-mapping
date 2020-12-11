package org.lfenergy.compas.service;

public interface DatabaseService {
    
    /**
     * Execute a database command
     * @param command the command to execute
     * @return the result
     */
    String executeCommand(String command);

    /**
     * Execute a database query
     * @param database the database to run the query on
     * @param query the query to execute
     * @return the result
     */
    String executeQuery(String database, String query);
}
