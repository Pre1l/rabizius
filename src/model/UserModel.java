package model;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public final class UserModel extends Model 
{
    public static boolean doesUsernameExist(String username) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("username", username);
        ResultSet result = getData("users", new String[]{}, conditions, true);
        try {
            return result.getInt("count") != 0;
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        return false;
    }

    public static String getUserIdByUsername(String username) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("username", username);
        ResultSet result = getData("users", new String[]{"user_id"}, conditions, true);
        try {
            return Integer.toString(result.getInt("user_id"));
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return "";
    }

    public static boolean isUserDeleted(String userId) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("user_id", userId);
        ResultSet result = getData("users", new String[]{"deleted_at"}, conditions, true);
        try {
            return result.getTimestamp("deleted_at") != null;
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }

    public static boolean updatePassword(String userId, String rawPassword) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("user_id", userId);

        Map<String, String> updates = new HashMap<>();
        updates.put("password", rawPassword);

        return setData("users", conditions, updates);
    }

    public static boolean updateUsername(String userId, String username) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("user_id", userId);

        Map<String, String> updates = new HashMap<>();
        updates.put("username", username);

        return setData("users", conditions, updates);
    }

    public static boolean createUser(String username, String password) 
    {
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        return addData("users", data);
    }

    public static boolean deleteUser(String userId) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("user_id", userId);

        Map<String, String> updates = new HashMap<>();
        updates.put("deleted_at", getServerTimestamp().toString());

        return setData("users", conditions, updates);
    }

    public static String getUsername(String userId) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("user_id", userId);
        ResultSet result = getData("users", new String[]{"username"}, conditions, true);
        try {
            return result.getString("username");
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return null;
    }

    public static boolean verifyPassword(String rawPassword, String userId) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("user_id", userId);
        ResultSet result = getData("users", new String[]{"password"}, conditions, true);
        try {
            return rawPassword.equals(result.getString("password"));
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }
}
