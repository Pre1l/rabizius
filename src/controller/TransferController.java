package controller;

import java.util.ArrayList;

import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;
import model.AccountConnectionsModel;
import model.AccountModel;
import model.Status;
import model.TransactionsModel;

/**
 * Controls the register page requests
 */
public class TransferController {
    /**
     * Renders the login page
     */
    public void render(SubmitData submitData) {
        String userId = submitData.getData("userId");
        String instanceToken = submitData.getData("instanceToken");
        RenderData renderData = new RenderData();

        if (!ControllerCore.authenticateUserInstance(userId, instanceToken)) {
            return;
        }

        renderData.setData("title", "Transfer");
        Core.instance().renderPage("TransferPage", renderData);
    }

    /**
     * Handles login requests by the view/user
     * @param submitData the submit data from the login request like username and password
     * @return the result data for view to react, like errors or if the login was successful
     */
    public SubmitResult processTransferRequest(SubmitData submitData) {
        SubmitResult result = new SubmitResult();

        String userId = submitData.getData("userId");
        String instanceToken = submitData.getData("instanceToken");
        String amountString = submitData.getData("amount");
        String recievingAccountNumber = submitData.getData("recievingAccountNumber");
        String purpose = submitData.getData("purpose");

        if (recievingAccountNumber == null || recievingAccountNumber.equals("") || amountString== null || amountString.equals("") || purpose == null || purpose.equals("")) {
            result.setStatus(false);
            result.setData("error", "Fields can not be empty.");
            return result;
        }

        if (!amountString.matches("\\d+")) {
            result.setStatus(false);
            result.setData("error", "Amount needs to be a number.");
            return result;
        }

        double amount = Double.parseDouble(amountString);
        
        if (!AccountModel.doesAccountNumberExist(recievingAccountNumber)) {
            result.setStatus(false);
            result.setData("error", "Account does not exist.");
            return result;
        }

        ArrayList<String> userConnection;
        String accountId;
        String recievingAccountId;
        recievingAccountId = AccountModel.getAccountIdByAccountNumber(recievingAccountNumber);

        if (ControllerCore.authenticateUserInstance(userId, instanceToken)) {
            userConnection = AccountConnectionsModel.getUserConnections(userId);
            accountId = userConnection.get(0);
            if ((AccountModel.getBalance(accountId) - amount) < ControllerCore.CREDIT_LIMIT) {
                result.setStatus(false);
                result.setData("error", "You have reached your account balance limit.");
                return result;
            }
            
            TransactionsModel.createTransaction(accountId, userId, amount*-1, Status.COMPLETED, null,
                    "Üerweisung auf das Konto " + recievingAccountNumber + " abgeschlossen." + purpose);
            TransactionsModel.createTransaction(recievingAccountId, userId, amount, Status.COMPLETED, null,
                    "Üerweisung von dem Konto " + accountId + " erhalten." + purpose);

            AccountModel.subtractBalance(amount, accountId);
            AccountModel.addBalance(amount, recievingAccountId);

            result.setStatus(true);
            return result;
        }
        
        result.setStatus(false);
        result.setData("error", "Something went wrong.");
        return result;
    }
}
