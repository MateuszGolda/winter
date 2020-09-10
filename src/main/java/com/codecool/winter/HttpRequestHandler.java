package com.codecool.winter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class HttpRequestHandler implements HttpHandler {
    private final Map<String, Method> handlers;

    public HttpRequestHandler(Map<String, Method> handlers) {
        this.handlers = handlers;
        handlers.keySet().forEach(System.out::println);
    }

    @WebRoute(path = "/" +
            "")
    private String getResponse() {
        return "Not implemented";
    }

    /**
     * Invokes proper method handling proper endpoint and sends HTTP Response back.
     *
     * @param exchange Encapsulated HTTP request.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().toString();
        Method routeHandler = handlers.get(uri);
        String response;
        if (routeHandler == null) {
            sendResponse("No proper method was found", exchange, 404);
            return;
        }

        try {
            if (handlerClassIsHttpRequestHandler(routeHandler)) {
                response = (String) routeHandler.invoke(this);
            } else {
                response = (String) routeHandler.invoke(new ExampleHandler());
            }
            sendResponse(response, exchange, 200);
        } catch (IllegalAccessException e) {
            sendResponse("Method couldn't be invoked", exchange, 500);
            e.printStackTrace();
        } catch (ClassCastException e) {
            sendResponse("Invalid method return type", exchange, 500);
        } catch (InvocationTargetException e) {
            sendResponse("Exception thrown while executing method", exchange, 500);
        }
    }

    private boolean handlerClassIsHttpRequestHandler(Method routeHandler) {
        return routeHandler.getDeclaringClass().getSimpleName().equals(this.getClass().getSimpleName());
    }

    private void sendResponse(String response, HttpExchange exchange, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
