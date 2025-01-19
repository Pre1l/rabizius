package core;

import data.RenderData;
import data.SubmitData;
import data.SubmitResult;
import model.Model;
import routing.Router;
import view.browser.Browser;

/**
 * Core of the Rabizius Banking Software
 */
public class Core
{
    private static Core instance;

    private Router router;
    private Browser browser;

    private Core() 
    {
        
    }

    public static void shutdown() 
    {
        Model.disconnect();
        instance.browser.dispose();
        instance.router = null;
        System.exit(0);
    }

    public static void main(String[] args) 
    {
        // Init core singleton
        Core.init();
    }

    /**
     * Initializes the core
     */
    private static void init() 
    {
        instance = new Core();
        instance.startRouting();
        Core.instance().redirect("/database");
    }

    /**
     * Starts the router and browser
     */
    private void startRouting() 
    {
        Browser.init();
        browser = new Browser();
        router = new Router();
    }

    /**
     * Retrieves the core instance of the singleton design pattern
     * @return the core instance
     */
    public static Core instance() 
    {
        return instance;
    }

    /**
     * Redirects the users browser to a new page/route over the router
     * @param route the route that the users browser should be redirected to
     */
    public void redirect(String route) 
    {
        router.redirect(route);
    }

    public void redirect(String route, SubmitData submitData) 
    {
        router.redirect(route, submitData);
    }

    /**
     * Makes a submit request to the designated submit/post route with the submit data over the router
     * @param route the route of the submit request that leads to the desired controller + method
     * @param submitData the submit data that the desired controller should work with
     * @return the submit result with the outcome/result that the controller returned
     */
    public SubmitResult submit(String route, SubmitData submitData) 
    {
        return router.submit(route, submitData);
    }

    /**
     * Renders the desired page into the browser
     * @param page the page that should be rendered
     * @param renderData the render data for the page to use
     */
    public void renderPage(String page, RenderData renderData) 
    {
        browser.renderPage(page, renderData);
    }

    /**
     * Displays a error in form of a dialog popup window to the user
     * @param errorMessage The error message that should be displayed
     */
    public void displayErrorDialog(String errorMessage) 
    {
        browser.displayErrorDialog(errorMessage);
    }
}
