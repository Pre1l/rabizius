package controller;

import java.util.ArrayList;

import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;
import model.AccountModel;
import model.AccountConnectionsModel;


public class DepositController 
{
    public void render(SubmitData submitData) 
    {
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

        renderData.setData("title", "Deposit");
        renderData.setData("balance", Double.toString(AccountModel.getBalance(accountId)));

        Core.instance().renderPage("DepositPage", renderData);
    }
    
    public SubmitResult processDepositRequest(SubmitData submitData) 
    {
        SubmitResult result = new SubmitResult();

        String userId = submitData.getData("userId");
        String amountString = submitData.getData("amount");
        double amount = Double.parseDouble(amountString);
        ArrayList<String> userConnection;
        String accountId;


        if (amount <= 0) {
            result.setStatus(false);
            result.setData("error", "Something went wrong.");
            return result;
        }

        userConnection = AccountConnectionsModel.getUserConnections(userId);
        accountId = userConnection.get(0);
            
        AccountModel.addBalance(amount, accountId);
        result.setData("balance", Double.toString(AccountModel.getBalance(accountId)));
        result.setStatus(true);
        return result;
        

    }
}


