package jrails;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JServer {
    public static void start(JRouter r) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new Handler(r));
            server.setExecutor(null); // Default executor
            System.out.println("Starting server...point your web browser to http://localhost:8000");
            server.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start server", e);
        }
    }

    static class Handler implements HttpHandler {
        private final JRouter router;

        Handler(JRouter router) {
            this.router = router;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response;
            int statusCode = 200;

            try {
                // Extract HTTP verb and path
                String verb = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();

                // Parse query parameters
                Map<String, String> params = parseParams(exchange);

                // Route the request and get the HTML response
                Html html = router.route(verb, path, params);
                response = html.toString();
            } catch (UnsupportedOperationException e) {
                response = "<h1>404 Not Found</h1><p>The requested URL was not found on this server.</p>";
                statusCode = 404;
            } catch (Exception e) {
                response = "<h1>500 Internal Server Error</h1><p>An unexpected error occurred.</p>";
                statusCode = 500;
                e.printStackTrace();
            }

            // Send the response
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }

        private Map<String, String> parseParams(HttpExchange exchange) throws IOException {
            Map<String, String> params = new HashMap<>();

            // Parse query string from the URL
            String query = exchange.getRequestURI().getQuery();
            if (query != null) {
                parseQueryString(params, query);
            }

            // Parse form data from POST body
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                parseQueryString(params, body);
            }

            return params;
        }

        private void parseQueryString(Map<String, String> params, String query) {
            for (String pair : query.split("&")) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                    params.put(key, value);
                }
            }
        }
    }
}
