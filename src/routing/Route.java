package routing;

/**
 * Holds a controller + a method of that controller
 */
class Route 
{
    private String controller;
    private String method;

    public Route(String controller, String method) 
    {
        this.controller = controller;
        this.method = method;
    }

    public String getController() 
    {
        return controller;
    }

    public String getMethod() 
    {
        return method;
    }
}
