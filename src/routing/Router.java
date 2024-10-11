package routing;

import java.lang.reflect.Method;
import java.util.HashMap;

import core.Core;
import data.SubmitData;
import data.SubmitResult;

/**
 * Routes user route requests to the designated controllers + methods
 */
public class Router 
{
    private RouteConfig routerConfig;
    private static final String GET_REQUESTS_KEY = "getRequests";
    private static final String POST_REQUESTS_KEY = "postRequests";

    public Router() 
    {
        routerConfig = new RouteConfig();
    }

    /**
     * Redirects the users browser to a new page/route
     * @param route the route that the users browser should be redirected to
     */
    public void redirect(String route) 
    {
        HashMap<String, Route> getRequests = routerConfig.getConfig().get(GET_REQUESTS_KEY);

        invokeMethod(getRequests, route, null);
    }

    public void redirect(String route, SubmitData submitData) 
    {
        HashMap<String, Route> getRequests = routerConfig.getConfig().get(GET_REQUESTS_KEY);

        invokeMethod(getRequests, route, submitData);
    }

    /**
     * Makes a submit request to the designated submit/post route with the submit data
     * @param route the route of the submit request that leads to the desired controller + method
     * @param submitData the submit data that the desired controller should work with
     * @return the submit result with the outcome/result that the controller returned
     */
    public SubmitResult submit(String route, SubmitData submitData) 
    {
        HashMap<String, Route> postRequests = routerConfig.getConfig().get(POST_REQUESTS_KEY);

        Object result = invokeMethod(postRequests, route, submitData);

        if (result instanceof SubmitResult) {
            return (SubmitResult) result;
        } 
        
        return null;
    }

    /**
     * Dynamically calls a desired routes controller + method.
     * @param routeMap the route hashmap of the get or post requests
     * @param route the route that should be dynamically called
     * @param submitData the submit data if there is any that is given to the controllers method as a parameter
     * @return the result/outcome object of the called method
     */
    private Object invokeMethod(HashMap<String, Route> routeMap, String route, SubmitData submitData) 
    {
        if (!routeMap.containsKey(route)) {
            Core.instance().displayErrorDialog("Route not found: " + route);
            return "";
        }

        Route routeConfig = routeMap.get(route);
        String controllerName = routeConfig.getController();
        String methodName = routeConfig.getMethod();

        try {
            Class<?> cls = Class.forName("controller." + controllerName);
            Object instance = cls.getDeclaredConstructor().newInstance();
            if (submitData == null) {
                Method method = cls.getMethod(methodName);
                return method.invoke(instance);
            } else {
                Method method = cls.getMethod(methodName, SubmitData.class);
                return method.invoke(instance, submitData);
            }
        } catch (ClassNotFoundException e) {
            Core.instance().displayErrorDialog("Controller not found: " + controllerName);
        } catch (NoSuchMethodException e) {
            Core.instance().displayErrorDialog("Method not found: " + methodName);
        } catch (Exception e) {
            Core.instance().displayErrorDialog("Error invoking method: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}