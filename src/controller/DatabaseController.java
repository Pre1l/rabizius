package controller;

import core.Core;
import data.RenderData;
import data.SubmitData;
import data.SubmitResult;
import model.Model;

/**
 * Controls the login page requests
 */
public class DatabaseController 
{
    /**
     * Renders the login page
     */
    public void render() 
    {
        // Data for the view/page
        RenderData renderData = new RenderData();
        renderData.setData("title", "Database");

        // Render the page LoginPage with renderData onto the browser
        Core.instance().renderPage("DatabasePage", renderData);
    }

    /**
     * Handles login requests by the view/user
     * @param submitData the submit data from the login request like username and password
     * @return the result data for view to react, like errors or if the login was successful
     */
    public SubmitResult processDatabaseRequest(SubmitData submitData) 
    {
        SubmitResult result = new SubmitResult();
        
        result.setStatus(Model.connect("jdbc:mariadb://" + submitData.getData("url"), submitData.getData("user"), submitData.getData("password")));

        return result;
    }
}