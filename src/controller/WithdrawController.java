package controller;

import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;

import java.util.ArrayList;

import model.AccountConnectionsModel;
import model.AccountModel;


/**
 * Controls the register page requests
 */

public class WithdrawController {

    /**
     * Renders the withdraw page
     */

    public void render(SubmitData submitData) {
        String userId = submitData.getData("userId");
        String instanceToken = submitData.getData("instanceToken");
        ArrayList<String> userConnection;
        String accountId;
        RenderData renderData = new RenderData();

        
        if (ControllerCore.authenticateUserInstance(userId, instanceToken)) {
            userConnection = AccountConnectionsModel.getUserConnections(userId);
            accountId = userConnection.get(0);
        } else {
            return;
        }

        renderData.setData("title", "Withdraw");
        renderData.setData("balance", Double.toString(AccountModel.getBalance(accountId)));

        Core.instance().renderPage("WithdrawPage", renderData);
    }


    /**
     * Handles login requests by the view/user
     * @param submitData the submit data from the login request like username and password
     * @return the result data for view to react, like errors or if the login was successful
     */
    public SubmitResult processWithdrawRequest(SubmitData submitData) {
        SubmitResult result = new SubmitResult();
        
        if (submitData == null) {
            result.setStatus(false);
            result.setData("error", "Null ist nicht erlaubt");
            return result;
        }
        
        String userId = submitData.getData("userId");
        String instanceToken = submitData.getData("instanceToken");
        String amountString = submitData.getData("amount");
        double amount = Double.parseDouble(amountString);
        ArrayList<String> userConnection;
        String accountId;

        if (ControllerCore.authenticateUserInstance(userId, instanceToken)) {
            userConnection = AccountConnectionsModel.getUserConnections(userId);
            accountId = userConnection.get(0);
            if ((AccountModel.getBalance(accountId) - amount) < ControllerCore.CREDIT_LIMIT) {
                result.setStatus(false);
                result.setData("error", "You have reached your account balance limit.");
                return result;
            }
            
            AccountModel.subtractBalance(amount, accountId);
            result.setStatus(true);
            result.setData("balance", Double.toString(AccountModel.getBalance(accountId)));
            return result;
        }
        
        result.setStatus(false);
        result.setData("error", "Something went wrong.");
        return result;
    }
}