package model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public final class TransactionsModel extends Model {
    public static boolean createTransaction(String accountId, String userId, double amount, Status status, String failureReason, String description) {
        Map<String, String> data = new HashMap<>();
        data.put("account_id", accountId);
        data.put("user_id", userId);
        data.put("amount", Double.toString(amount));
        data.put("status", status.toString());
        data.put("failure_reason", failureReason);
        data.put("description", description);

        return addData("transactions", data);
    }

    public static ResultSet getTransasctionList() {
        return getData("transactions", new String[]{"*"}, new HashMap<>(), false);
    }

    public static ResultSet getUserTransasctionList(String userId) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("user_id", userId);
        return getData("transactions", new String[]{"*"}, conditions, false);
    }

    public static ResultSet getAccountTransasctionList(String accountId) {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("account_id", accountId);
        return getData("transactions", new String[]{"*"}, conditions, false);
    }
}
