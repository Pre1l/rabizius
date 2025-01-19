package routing;

import java.util.HashMap;

/**
 * Configuration for the get/post request routes
 */
class RouteConfig 
{
    private HashMap<String, HashMap<String, Route>> routes;

    public RouteConfig() 
    {
        routes = new HashMap<>();

        populateRoutes();
    }

    /**
     * Retrieves the routes config
     * @return the hashmap of get/post Request url configs, where each url inside of each request Hashmap have a designated Route Object
     */
    public HashMap<String, HashMap<String, Route>> getConfig() 
    {
        return routes;
    }

    /**
     * Populates/loads all routes into the routes attribute
     */
    private void populateRoutes() 
    {
        routes.put("getRequests", new HashMap<>());
        routes.put("postRequests", new HashMap<>());

        HashMap<String, Route> getRequests = routes.get("getRequests");
        HashMap<String, Route> postRequests = routes.get("postRequests");

        // Get Requests
        getRequests.put("/database", new Route("DatabaseController", "render"));
        getRequests.put("/login", new Route("LoginController", "render"));
        getRequests.put("/register", new Route("RegisterController", "render"));
        getRequests.put("/dashboard", new Route("DashboardController", "render"));
        getRequests.put("/transfer", new Route("TransferController", "render"));
        getRequests.put("/deposit", new Route("DepositController", "render"));
        getRequests.put("/withdraw", new Route("WithdrawController", "render"));
        getRequests.put("/account/delete", new Route("DeleteAccountController", "render"));

        // Post/Submit Requests
        postRequests.put("/database", new Route("DatabaseController", "processDatabaseRequest"));
        postRequests.put("/login", new Route("LoginController", "processLoginRequest"));
        postRequests.put("/register", new Route("RegisterController", "processRegisterRequest"));
        postRequests.put("/withdraw", new Route("WithdrawController", "processWithdrawRequest"));
        postRequests.put("/deposit", new Route("DepositController", "processDepositRequest"));
        postRequests.put("/transfer", new Route("TransferController", "processTransferRequest"));
        postRequests.put("/account/delete", new Route("DeleteAccountController", "processDeleteAccountRequest"));
    }
}