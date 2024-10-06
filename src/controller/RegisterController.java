package controller;

import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;

/**
 * Controls the register page requests
 */
public class RegisterController {
    /**
     * Renders the login page
     */
    public void render() {
        // Data for the view/page
        RenderData renderData = new RenderData();
        renderData.setData("title", "Register");

        // Render the page RegisterPage with renderData onto the browser
        Core.instance().renderPage("RegisterPage", renderData);
    }

    /**
     * Handles login requests by the view/user
     * @param submitData the submit data from the login request like username and password
     * @return the result data for view to react, like errors or if the login was successful
     */
    public SubmitResult processRegisterRequest(SubmitData submitData) {
        String enteredUsername = submitData.getData("username");
        String enteredPassword = submitData.getData("password");

        return AuthController.register(enteredUsername, enteredPassword);
    }
}
