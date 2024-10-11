package model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class AccountConnectionsModel extends Model 
{
    public static boolean createAccountConnection(String accountId, String userId) 
    {
        Map<String, String> data = new HashMap<>();
        data.put("account_id", accountId);
        data.put("user_id", userId);

        return addData("account_connections", data);
    }

    public static boolean closeAccountConnection(String accountId, String userId) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);
        conditions.put("user_id", userId);

        return removeData("account_connections", conditions);
    }

    public static ArrayList<String> getUserConnections(String userId) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("user_id", userId);
        ResultSet result = getData("account_connections", new String[]{"account_id"}, conditions, false);

        ArrayList<String> accountIds = new ArrayList<>();

        try {
            while (result.next()) {
                accountIds.add(Integer.toString(result.getInt("account_id")));
            }
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        return accountIds;
    }

    public static boolean closeUserConnections(String userId) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("user_id", userId);
        return removeData("account_connections", conditions);
    }

    public static boolean closeAccountConnections(String accountId) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);
        return removeData("account_connections", conditions);
    }

    public static boolean verifyAccountConnection(String userId, String accountId) 
    {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);
        conditions.put("user_id", userId);
        ResultSet result = getData("account_connections", new String[]{}, conditions, true);
        try {
            return result.getInt("count") != 0;
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        return false;
    }
}
