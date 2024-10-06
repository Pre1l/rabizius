package controller;


import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;

/**
 * Controls the login page requests
 */
public class LoginController {
    /**
     * Renders the login page
     */
    public void render() {
        // Data for the view/page
        RenderData renderData = new RenderData();
        renderData.setData("title", "Login");

        // Render the page LoginPage with renderData onto the browser
        Core.instance().renderPage("LoginPage", renderData);
    }

    /**
     * Handles login requests by the view/user
     * @param submitData the submit data from the login request like username and password
     * @return the result data for view to react, like errors or if the login was successful
     */
    public SubmitResult processLoginRequest(SubmitData submitData) {
        SubmitResult result = new SubmitResult();
        result = AuthController.login(submitData.getData("username"), submitData.getData("password"));
        return result;
    }
}