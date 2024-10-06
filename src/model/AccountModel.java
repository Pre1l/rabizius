package model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public final class AccountModel extends Model {
    public static boolean doesAccountNumberExist(String accountNumber) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_number", accountNumber);
        ResultSet result = getData("accounts", new String[]{}, conditions, true);
        try {
            return result.getInt("count") != 0;
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        return false;
    }

    public static String getAccountIdByAccountNumber(String accountNumber) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_number", accountNumber);
        ResultSet result = getData("accounts", new String[]{"account_id"}, conditions, true);
        try {
            return Integer.toString(result.getInt("account_id"));
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return "";
    }

    public static boolean isAccountDeleted(String accountId) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);
        ResultSet result = getData("accounts", new String[]{"deleted_at"}, conditions, true);
        try {
            return result.getTimestamp("deleted_at") != null;
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }

    public static boolean updatePin(String accountId, String rawPin) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);

        Map<String, String> updates = new HashMap<>();
        updates.put("pin", rawPin);

        return setData("accounts", conditions, updates);
    }

    public static boolean createAccount(String accountNumber, String pin, AccountType accountType, double startingBalance) {
        Map<String, String> data = new HashMap<>();
        data.put("account_number", accountNumber);
        data.put("pin", pin);
        data.put("account_type", accountType.toString());
        data.put("balance", Double.toString(startingBalance));

        return addData("accounts", data);
    }

    public static boolean deleteAccount(String accountId) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);

        Map<String, String> updates = new HashMap<>();
        updates.put("deleted_at", getServerTimestamp().toString());

        return setData("accounts", conditions, updates);
    }

    public static String getAccountNumber(String accountId) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);
        ResultSet result = getData("accounts", new String[]{"account_number"}, conditions, true);
        try {
            return result.getString("account_number");
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return null;
    }

    public static boolean verifyPin(String rawPin, String accountId) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);
        ResultSet result = getData("accounts", new String[]{"pin"}, conditions, true);
        try {
            return rawPin.equals(result.getString("pin"));
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }

    public static boolean addBalance(double amount, String accountId) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);
        ResultSet result = getData("accounts", new String[]{"balance"}, conditions, true);
        try {
            Map<String, String> data = new HashMap<>();
            double balance = Double.parseDouble(result.getString("balance"));
            balance += amount;
            data.put("balance", Double.toString(balance));
            return setData("accounts", conditions, data);
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }

    public static boolean subtractBalance(double amount, String accountId) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);
        ResultSet result = getData("accounts", new String[]{"balance"}, conditions, true);
        try {
            double balance = Double.parseDouble(result.getString("balance"));
            balance -= amount;

            Map<String, String> data = new HashMap<>();
            data.put("balance", Double.toString(balance));
            return setData("accounts", conditions, data);
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }

    public static double getBalance(String accountId) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);
        ResultSet result = getData("accounts", new String[]{"balance"}, conditions, true);
        try {
            return Double.parseDouble(result.getString("balance"));
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return 0;
    }
}
