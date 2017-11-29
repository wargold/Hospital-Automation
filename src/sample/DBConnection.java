package sample;

import java.sql.*;

/**
 * DBConnection is a class for connecting to a database by using the JDBC API.
 * This class contains methods for mainting the database e.g retrieving and adding
 * values to the tables in the database.
 *
 * @author War
 * @version 1.0
 */
public class DBConnection {

    private Connection conn = null;
    private Statement stmt;

    public DBConnection() {
        initialize();
    }

    /**
     * Connects to the database, don't forget to have the Classpath of the driver...
     */
    public void initialize() {

        System.out.println("Checking if Driver is registered with DriverManager\n");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Registered the driver ok, making DB connection now\n");
        /*Accessing a Database running on a local server on your own computer*/
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/er");
            conn.setAutoCommit(false);
        } catch (SQLException e) {

            e.printStackTrace();
        }

        if (conn != null) {
            System.out.println("Successfully established connection to database");
        } else {
            System.out.println("Could not established connection to database");
        }
    }

    /**
     * Return a resultset of the a psql query.
     * @param query psql query
     * @return ResulSet of the query.
     */
    public ResultSet SQlQuery(String query) {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Close a connection
     */
    public void closeSnC() {
        try {
            conn.close();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Insert a psql query to the database.
     * @param query Psql query.
     */
    public void insertSql(String query) {
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            //stmt.close();
            conn.commit();
            //c.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}