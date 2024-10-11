package controller;

import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;
import model.AccountConnectionsModel;
import model.AccountModel;
import model.UserModel;

public class DeleteAccountController 
{
    public void render(SubmitData submitData) 
    {
        String userId = submitData.getData("userId");
        String instanceToken = submitData.getData("instanceToken");
        RenderData renderData = new RenderData();

        
        if (!ControllerCore.authenticateUserInstance(userId, instanceToken)) {
            return;
        }

        renderData.setData("title", null);

        Core.instance().renderPage("DeleteAccountPage", renderData);
    }

    public SubmitResult processDeleteAccountRequest(SubmitData submitData) 
    {
        return deleteAccount(submitData);
    }

    private SubmitResult deleteAccount(SubmitData submitData) 
    {
        SubmitResult result = new SubmitResult();

        String instanceToken = submitData.getData("instanceToken");
        String userId = submitData.getData("userId");

        if (!ControllerCore.authenticateUserInstance(userId, instanceToken)) {
            result.setStatus(false);
            result.setData("error", "Something went wrong.");
            return result;
        }

        String accountId = AccountConnectionsModel.getUserConnections(userId).get(0);
        Double balance = AccountModel.getBalance(accountId);

        if (balance < 0) {
            result.setStatus(false);
            result.setData("error", "Account deletion failed: balance must be zero or positive.");
            return result;
        }

        AccountModel.deleteAccount(accountId);
        UserModel.deleteUser(userId);

        result.setStatus(true);
        return result;
    }
}
