package controller;

import java.security.SecureRandom;
import java.util.regex.Pattern;

import data.SubmitResult;
import model.AccountConnectionsModel;
import model.AccountModel;
import model.AccountType;
import model.UserModel;

class AuthController {
    public static SubmitResult login(String enteredUsername, String enteredPassword) {
        SubmitResult result = new SubmitResult();

        if (UserModel.doesUsernameExist(enteredUsername)) {
            String userId = UserModel.getUserIdByUsername(enteredUsername);

            if (!UserModel.isUserDeleted(userId) && UserModel.verifyPassword(enteredPassword, userId)) {
                String token = ControllerCore.generateToken();
                ControllerCore.registerUserInstance(userId, token);
                result.setData("instanceToken", token);
                result.setData("userId", userId);
                result.setStatus(true);
                System.out.println("Logged in sucessfully");
                return result;
            }
        }

        result.setStatus(false);
        result.setData("error", "Wrong Username or Password.");
        return result;
    }

    public static SubmitResult register(String enteredUsername, String enteredPassword) {
        SubmitResult result = new SubmitResult();

        String usernameError = validateUsername(enteredUsername);

        if (usernameError != null) {
            result.setStatus(false);
            result.setData("error", usernameError);
            return result;
        }

        String passwordError = validatePassword(enteredPassword);

        if (passwordError != null) {
            result.setStatus(false);
            result.setData("error", passwordError);
            return result;
        }

        if (UserModel.doesUsernameExist(enteredUsername)) {
            result.setStatus(false);
            result.setData("error", "Username already taken.");
            return result;
        }

        if (UserModel.createUser(enteredUsername, enteredPassword)) {
            String accountNumber = createAccountNumber();
            String userId = UserModel.getUserIdByUsername(enteredUsername);
            AccountModel.createAccount(accountNumber, "0000", AccountType.GIRO, (double) 1000);
            AccountConnectionsModel.createAccountConnection(AccountModel.getAccountIdByAccountNumber(accountNumber), userId);
            System.out.println("Registered in sucessfully");
            return login(enteredUsername, enteredPassword);
        } else {
            result.setStatus(false);
            result.setData("error", "Something went wrong.");
        }

        return result;
    }

    public static String validatePassword(String rawPassword) {
        if (rawPassword.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        return null;
    }

    public static String validateUsername(String username) {
        if (username.length() < 5 || username.length() > 18) {
            return "Username must be between 5 and 18 characters long.";
        }
        if (!Pattern.compile("^[a-zA-Z0-9]+$").matcher(username).matches()) {
            return "Username must contain only alphanumeric characters.";
        }
        return null;
    }

    public static void logout(String userId, String instanceToken) {
        ControllerCore.deRegisterUserInstance(userId, instanceToken);
    }

    private static String createAccountNumber() {
        String accountNumber = createAccountNumberHelper();
        while (AccountModel.doesAccountNumberExist(accountNumber)) {
            accountNumber = createAccountNumberHelper();
        }
        return accountNumber;
    }

    private static String createAccountNumberHelper() {
        SecureRandom secureRandom = new SecureRandom();

        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int digit = secureRandom.nextInt(10);
            accountNumber.append(digit);
        }

        return accountNumber.toString();
    }
}
