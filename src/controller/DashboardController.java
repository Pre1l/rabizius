package controller;

import java.util.ArrayList;

import core.Core;
import data.RenderData;
import data.SubmitData;
import model.AccountConnectionsModel;
import model.AccountModel;

public class DashboardController 
{
    /**
     * Renders the dashboard page
     */
    public void render(SubmitData submitData) 
    {
        // Data for the view/page
        String userId = submitData.getData("userId");
        String instanceToken = submitData.getData("instanceToken");
        ArrayList<String> userConnection;
        String accountId;
        RenderData renderData = new RenderData();

        if (!ControllerCore.authenticateUserInstance(userId, instanceToken)) return;

        userConnection = AccountConnectionsModel.getUserConnections(userId);
        accountId = userConnection.get(0);

        renderData.setData("title", "Dashboard");
        renderData.setData("balance", String.valueOf(AccountModel.getBalance(accountId)));
        renderData.setData("accountNumber", AccountModel.getAccountNumber(accountId));
        // TODO erstmal nur ein fester Kontotyp
        renderData.setData("accountType", "Giro");

        // Render the page DashboardPage with renderData onto the browser
        Core.instance().renderPage("DashboardPage", renderData);
    }
}
