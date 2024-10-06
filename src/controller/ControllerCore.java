package controller;

import java.util.HashMap;

import core.Core;

import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.security.SecureRandom;
import java.util.Base64;

class ControllerCore {
    private static final int instanceTokenTimoutIntervalTime = 20;
    private static HashMap<String, String> authHashMap = new HashMap<>();
    public static final int CREDIT_LIMIT = -4000;

    // Registers a userID instanceToken pair
    public static void registerUserInstance(String userId, String instanceToken) {
        authHashMap.put(userId, instanceToken);
    }

    // deRegisters (removes) a userID instanceToken pair
    public static void deRegisterUserInstance(String userId, String instanceToken) {
        authHashMap.remove(userId);
    }

    // Generates random Token with timestamp, can be duplicate
    public static String generateTokenHelper() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[25];
        random.nextBytes(bytes);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mmHHddMMyyyy");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);
        String token = Base64.getEncoder().encodeToString(bytes);
        String newtoken = token + time;
        return newtoken;
    }

    // returns a valid instanceToken
    public static String generateToken() {
        String token = "";
        do {
            token = generateTokenHelper();
        } while (authHashMap.containsValue(token));
        return token;
    }

    // Checks if the userID instanceToken pair is valid
    public static boolean authenticateUserInstance(String userId, String instanceToken) {
        int tokenLength = instanceToken.length();
        String storedToken = authHashMap.get(userId);
        if (storedToken == null || !storedToken.equals(instanceToken)) {
            Core.instance().displayErrorDialog("Invalid Token.");
            Core.instance().redirect("/login");
            return false;
        }

        String tokenTimestampStr = instanceToken.substring(tokenLength - 12, tokenLength);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mmHHddMMyyyy");
        LocalDateTime tokenTimestamp = LocalDateTime.parse(tokenTimestampStr, dtf);

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(tokenTimestamp, now);
        if (duration.toMinutes() > instanceTokenTimoutIntervalTime) {
            authHashMap.remove(instanceToken);
            Core.instance().displayErrorDialog("Token Expired.");
            Core.instance().redirect("/login");
            return false;
        }

        return true;
    }

}