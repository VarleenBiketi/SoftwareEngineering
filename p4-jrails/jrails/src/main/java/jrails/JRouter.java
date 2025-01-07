package jrails;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JRouter {
    private final Map<String, String> routes = new HashMap<>();

    public void addRoute(String verb, String path, Class<?> clazz, String method) {
        String key = verb.toUpperCase() + " " + path;
        String value = clazz.getName() + "#" + method;
        routes.put(key, value);
    }

    public String getRoute(String verb, String path) {
        String key = verb.toUpperCase() + " " + path;
        return routes.get(key);
    }

    public Html route(String verb, String path, Map<String, String> params) {
        String key = verb.toUpperCase() + " " + path;
        String route = routes.get(key);

        if (route == null) {
            throw new UnsupportedOperationException("No route found for: " + key);
        }

        String[] parts = route.split("#");
        String className = parts[0];
        String methodName = parts[1];

        try {
            // Load the controller class
            Class<?> clazz = Class.forName(className);

            // Get the controller method
            Method method = clazz.getMethod(methodName, Map.class);

            // Invoke the method
            return (Html) method.invoke(null, params);
        } catch (Exception e) {
            throw new RuntimeException("Error routing request: " + key, e);
        }
    }
}
