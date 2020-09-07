# Summer

## Story

Your task is to create a 'Summer' framework, that will be responsible for web routing pretty much like 'Spring' framework does.
Did you get it? Spring? Summer? Hah! I'm so funny!...
 
Ekhem..*

Your task is to create an annotation, and an annotation processor that will handle HTTP requests.
Instead of defining multiple HTTP handlers with each one having its own path, you will create a single handler that will 
search for methods annotated with your custom annotation `@WebRoute(String path)`, and invoke them using reflection *if* 
the `path` matches one HTTP request was send on.

## What are you going to learn?

- Java Reflection API
- HTTP requests handling
- Custom annotation creation

## Tasks

1. Obviously, to process annotations, we must create an annotation. In background materials you can find information how to do that. Create an annotation `webRoute` with `element` of name `path` and define its retention policy so it is visible during runtime.
    - Annotation `webRoute` exists.
    - Annotation has an `element` of type `String` with a name `path`.
    - Annotation `webRoute` has defined `retention`, so it can be accessed in the runtime.

2. Let's examine classes you already have really quickly. `ExampleHandler` has 3 methods you will use to test your solution. `Summer` class accepts vararg of classes that supposedly contain handler methods (annotated with `webRoute` annotation). Lastly `App` that manages this setup process by providing `ExampleHandler` to `Summer`. Your job is to annotate 2 of 3 methods in `ExampleHandler` with `webRoute` and then extract those methods in `registerHandlers`. This extra (3rd) method is just for you to test your filtering process.
    - Class `ExampleHandler` has 2 annotated methods with paths: "/hello" and "/lorem".
    - Method `registerHandlers` reads all methods from all provided classes, and stores every method annotated with `webRoute` in a map, where `Key` is the path handled by the method, and the `Value` is the method itself.
    - Extra challenge - `registerHandlers` implementation is just one `Stream`, and nothing else.

3. We need a HTTP server - after all we want to handle HTTP requests. Our HTTP server will redirect all requests to an instance of a `HttpRequestHandler`. RequestHandler will send a response to a client. For now simple response "not implemented" will be sufficient.
    - Created instance of `HttpServer` that will listen on port 8080.
    - Created instance of `HttpRequestHandler` and provided it a map of all handler methods.
    - Server operates with just one general context "/" and is handled by `HttpRequestHandler::handle` method.
    - Method `sendResponse` in class `HttpRequestHandler` is implemented.
    - Optional feature - all paths that can be handled are displayed on start.

4. At this point you can run your app and visit http://localhost:8080 to get a message "not implemented". Last step
is to use handler methods that we found in second step -> `registerHandlers` method. Map with all those methods
should now be stored inside `HttpRequestHandler` in a field. Having that and a `HttpExchange` (parameter of
`handle` method) we are ready to glue it all together. The flow of `handle` method can be simplified to this:
- get `path` from `HttpExchange` to know what method to invoke.
- find a method corresponding to that `path`.
- invoke proper method and send result of that invocation to the user.

Some edge cases must be taken to consideration - more info below.
    - Method invokes proper method (based on `path`) and returns result to a user.
    - Method returns 404 proper method wasn't found.
    - Method returns 500 with a message "Method couldn't be invoked" if method invocation was impossible.
    - Method returns 500 with a message "Invalid method return type" if method results with an object that is not a `String`.
    - Method returns 500 with a message "Exception thrown while executing method" if method invocation causes an exception. (To test this behaviour change the implementation of one of the `ExampleHandler` methods to `throw new RuntimeException();`)

## General requirements

None

## Hints

- To retrieve methods of a `Class`, you can use `getMethods()` and `getDeclaredMethods()` methods. First one returns all `public` methods available for this class (including inherited ones), while second one returns all methods declared in this very class, no matter access modifier, but excludes all inherited methods.
- It is easier to invoke `static` methods as opposed to `non-static` ones. It is because you do not need to create instances of the class containing the method.
- `java.lang.Class<T>` class has a method `getModifiers()` that returns an int. You can parse this return type using helper class `java.lang.reflect.Modifier` and methods like `isPublic(int mod)` or `isStatic(int mod)`.

## Starting your project

To start your project click [this link](https://journey.code.cool/v2/project/solo/blueprint/summer/java).

## Background materials

- <i class="far fa-exclamation"></i> [Annotations docs](https://docs.oracle.com/javase/tutorial/java/annotations/basics.html)
- <i class="far fa-exclamation"></i> [Reflection](https://learn.code.cool/full-stack/#/../pages/java/java-reflection)
- <i class="far fa-exclamation"></i> [Annotations](https://learn.code.cool/full-stack/#/../pages/java/annotations)
- <i class="far fa-exclamation"></i> [Java Web Server](https://learn.code.cool/full-stack/#/../pages/java/your-own-web-server-in-java)