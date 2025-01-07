package jrails;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JRouterTest {
    private JRouter router;

    @Before
    public void setUp() {
        router = new JRouter();
    }

    @Test
    public void testAddAndGetRoute() {
        router.addRoute("GET", "/test", TestController.class, "testMethod");
        String route = router.getRoute("GET", "/test");
        assertEquals("jrails.TestController#testMethod", route);
    }

    @Test
    public void testRouteInvocation() {
        router.addRoute("GET", "/hello", TestController.class, "hello");
        Map<String, String> params = new HashMap<>();
        params.put("name", "World");

        Html response = router.route("GET", "/hello", params);
        assertEquals("<p>Hello, World!</p>", response.toString());
    }
}

class TestController {
    public static Html hello(Map<String, String> params) {
        String name = params.getOrDefault("name", "Stranger");
        return new Html().p(new Html().t("Hello, " + name + "!"));
    }
}
