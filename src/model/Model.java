package model;

import java.sql.*;
import java.util.Map;

import core.Core;

public abstract class Model 
{
    private static Connection conn;
    
    public static void connect() 
    {
        String url = "jdbc:mariadb://rabizius.softanium.dev/rabizius";
        String user = "rabizius";
        String password = "jXqM6YsK4siiFYHq6dKHBKEXxzePAkdsgbtdEndKbEPytF2YBgyLafJjPYixmDQ4agwAFmpVk3ipKGzJMYsZhVtxXzAx63MDwzXgnwTVBPXKzPSE88rwm3MMdfDYY5w3";

        try {
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            Core.instance().displayErrorDialog("Unable to connect to database, shutting down.");
            Core.shutdown();
        }
    }

    public static void disconnect() 
    {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception on closing: " + e.getMessage());
        }
    }

    protected static ResultSet getData(String table, String[] columns, Map<String, String> conditions, boolean expectSingle) 
    {
        ResultSet resultSet = null;
        StringBuilder query = new StringBuilder("SELECT ");

        if (columns.length != 0) {
            query.append(String.join(", ", columns));
        } else {
            query.append("COUNT(*) AS count");
        }

        query.append(" FROM " + table);

        generateWhereClauses(conditions, query);

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query.toString());
            int index = 1;
            for (String key : conditions.keySet()) {
                preparedStatement.setString(index++, conditions.get(key));
            }
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        if (expectSingle) {
            try {
                resultSet.next();
            } catch (Exception e) {
                System.out.println("SQL Exception: " + e.getMessage());
            }
        }

        return resultSet;
    }

    protected static boolean setData(String table, Map<String, String> conditions, Map<String, String> updates) 
    {
        StringBuilder query = new StringBuilder("UPDATE " + table + " SET ");
        boolean firstUpdate = true;
        for (String key : updates.keySet()) {
            if (!firstUpdate) {
                query.append(", ");
            }
            query.append(key).append(" = ?");
            firstUpdate = false;
        }

        generateWhereClauses(conditions, query);

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query.toString());
            int index = 1;
            for (String key : updates.keySet()) {
                preparedStatement.setString(index++, updates.get(key));
            }
            for (String key : conditions.keySet()) {
                preparedStatement.setString(index++, conditions.get(key));
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        return false;
    }

    protected static boolean addData(String table, Map<String, String> data) 
    {
        StringBuilder query = new StringBuilder("INSERT INTO " + table + " (");
        StringBuilder values = new StringBuilder(" VALUES (");
        boolean first = true;

        for (String key : data.keySet()) {
            if (!first) {
                query.append(", ");
                values.append(", ");
            }
            query.append(key);
            values.append("?");
            first = false;
        }
        query.append(")").append(values).append(")");

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query.toString());
            int index = 1;

            for (String value : data.values()) {
                preparedStatement.setString(index++, value);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        return false;
    }

    protected static boolean removeData(String table, Map<String, String> conditions) 
    {
        StringBuilder query = new StringBuilder("DELETE FROM " + table);

        generateWhereClauses(conditions, query);

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query.toString());
            int index = 1;
            for (String key : conditions.keySet()) {
                preparedStatement.setString(index++, conditions.get(key));
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        return false;
    }

    protected static Timestamp getServerTimestamp() 
    {
        String query = "SELECT NOW() AS server_time";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getTimestamp("server_time");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return null;
    }

    private static void generateWhereClauses(Map<String, String> conditions, StringBuilder query) 
    {
        if (conditions != null && !conditions.isEmpty()) {
            query.append(" WHERE ");
            boolean firstCondition = true;
            for (String key : conditions.keySet()) {
                if (!firstCondition) {
                    query.append(" AND ");
                }
                query.append(key).append(" = ?");
                firstCondition = false;
            }
        }
    }
}