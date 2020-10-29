package com.codecool.winter;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Winter {
    private final Map<String, Method> handlers;

    Winter() {
        handlers = new HashMap<>();
    }

    /**
     * Accepts any number of handlers and extracts all methods annotated with {@link WebRoute}.
     *
     * @param handlerClasses classes with methods annotated with {@link WebRoute}.
     */
    void registerHandlers(Class<?>... handlerClasses) {
        Arrays.stream(handlerClasses)
                .flatMap(aClass -> Arrays.stream(aClass.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(WebRoute.class))
                .forEach(method -> handlers.put(method.getAnnotation(WebRoute.class).path(), method));
    }

    Map<String, Method> getHandlers() {
        return handlers;
    }

    /**
     * Starts HTTP server, waits for HTTP requests and redirects them to one of registered handler methods.
     */
    void run() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpRequestHandler(handlers));
        server.setExecutor(null);
        server.start();
    }
}
