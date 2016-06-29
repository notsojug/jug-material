class: center, middle

# Java API for RESTful Web Services

## An official part of Java EE 6/7

---

# Reference implementation

_Jersey_ RESTful Web Services framework provides support for JAX-RS APIs and serves as a JAX-RS (JSR 311 & JSR 339) Reference Implementation. **Included in Glassfish**.

## What we use: RESTEasy

_RESTEasy_ is a **JBoss project** fully certified and portable implementation of the JAX-RS specification.

---

# Transition time: Java EE 6 -> 7

**Jboss EAP 6** is a certified **Java EE 6** application server.
* JAX-RS 1.1 (_RESTEasy 2.3.x embedded_)

**Jboss EAP 7** is a certified **Java EE 7** application server.
* JAX-RX 2.0 (_RESTEasy 3.0.x embedded_)

--

It's always possible to disable the embedded module and use another implementation (possible, not _easy_).

---

# What is RESTeasy?

It's a _Servlet_ that intercept and dispatch HTTP request to the associated _Resource implementation_.

--

RESTeasy can work in standalone mode (inside any Servlet container that runs on JDK 5+) or in embedded mode.

--

There are many [Configuration Switches](https://docs.jboss.org/resteasy/docs/2.3.7.Final/userguide/html_single/index.html#d4e39) to be used into the `web.xml` file through the RESTeasy servlet's `context-param`.

---

## The JAX-RS Application

`javax.ws.rs.core.Application` class is a standard JAX-RS class that you may implement to provide information on your deployment. It is simply a class the lists all JAX-RS root resources and providers.

```java
@ApplicationPath("/rest-prefix")
public class MyApplication extends Application {
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(
            Arrays.asList(TheResource.class,
                AnotherResource.class));
    }
}
```

???
* Returning null is equivalent to returning an empty set.
* If you have this set, you should probably turn off automatic scanning as this will probably result in duplicate classes being registered.

---

## Resource classes annotated with

* `@Path` specifies the relative path for a resource class or method.
* `@GET, @PUT, @POST, @DELETE` and `@HEAD` specify the HTTP request type of a resource.
* `@Produces` specifies the response Internet media types (used for content negotiation).
* `@Consumes` specifies the accepted request Internet media types.

---

### @Path and regular expression

The `@Path` annotation is not limited to simple path expressions. You also have the ability to insert regular expressions into `@Path`'s value.

--

For example:

```java
@Path("/resources")
public class MyResource {

   @GET
   @Path("{var:.*}/stuff")
   public String get() {...}
}
```

---

### @Path and regular expression

The following GETs will route to the getResource() method:

```
GET /resources/stuff
GET /resources/foo/stuff
GET /resources/on/and/on/stuff
```

--

The format of the expression is:

```
"{" variable-name [ ":" regular-expression ] "}"
```

---

## Method parameters annotated with

* `@PathParam` a path segment.
* `@QueryParam` the value of an HTTP query parameter.
* `@MatrixParam` the value of an HTTP matrix parameter.
* `@HeaderParam` an HTTP header value.
* `@CookieParam` a cookie value.
* `@FormParam` a form value.
* `@DefaultValue` specifies a default value for the above bindings when the key is not found.
* `@Context` returns the entire context of the object (for example `@Context HttpServletRequest request`).

---

## Beware the @FormParam

It work if and only if the request body is of the type `application/x-www-form-urlencoded`!

--

You cannot combine @FormParam with the default `application/x-www-form-urlencoded` that unmarshalls to a MultivaluedMap<String, String>.

---

## Beware the @FormParam

i.e. This is illegal:

```java
@Path("/resources")
public class NameRegistry {
   @Path("/service")
   @POST
   @Consumes("application/x-www-form-urlencoded")
   public void addName(@FormParam("first") String first,
        MultivaluedMap<String, String> form) {...}
}
```

---

## @Encoded and encoding

The `@javax.ws.rs.Encoded` annotation can be used on a class, method, or param. By default, inject `@PathParam` and `@QueryParams` are decoded. By additionally adding the `@Encoded` annotation, the value of these params will be provided in encoded form.

```java
@Path("/")
public class MyResource {
  @Path("/{param}")
  @GET
  public String get(@PathParam("param")
    @Encoded String param) {...}
}
```

---

## @Context

The @Context annotation allows you to inject instances of:

* `javax.ws.rs.core.HttpHeaders`
* `javax.ws.rs.core.UriInfo`
* `javax.ws.rs.core.Request`
* **`javax.servlet.HttpServletRequest`**
* **`javax.servlet.HttpServletResponse`**
* `javax.servlet.ServletConfig`
* `javax.servlet.ServletContext`
* `javax.ws.rs.core.SecurityContext`

---

## JAX-RS Content Negotiation

The server declares content preferences via the `@Produces` and `@Consumes` headers.

--
Example...

```java
@Consumes("text/*")
@Path("/library")
public class Library {
         @POST
         public String stringBook(String book) {...}

         @Consumes("text/xml")
         @POST
         public String jaxbBook(Book book) {...}
}
```

---

## JAX-RS Content Negotiation

```text
POST /library
content-type: text/plain
thsi sis anice book
```

Method called?
--
 `stringBook`
--

```text
POST /library
content-type: text/xml
<book name="EJB 3.0" author="Bill Burke"/>
```

Guess which...
--
 `jaxbBook`!

???
Same `@Produces`

---

## Content Marshalling/Providers

The JAX-RS specification allows you to plug in your own request/response **body reader and writers**!!

You can use:

* JAXB Provider
* Jackson Provider (JSON only)
* Others...

--

But what if I want to marshall the HTTP String of a parameter?

---

## String marshalling for String based _@*Param_

_Injected parameters_ can be converted to objects if these objects have a `valueOf(String)` static method or a _constructor that takes one String parameter_.
--

Not suitable? **StringConverter**!

```java
public interface StringConverter<T>
{
   T fromString(String str);
   String toString(T value);
}
```

???
@PathParam, @QueryParam, @MatrixParam, @FormParam, and @HeaderParam are represented as strings in a raw HTTP request.

**Remember @Provider!**

---

## String marshalling with Annotations

`StringParamUnmarshaller` is sensitive to the annotations placed on the parameter or field you are injecting into.

```java
public interface StringParameterUnmarshaller<T>
{
   void setAnnotations(Annotation[] annotations);
   T fromString(String str);
}
```

--

```java
get(@PathParam("date") @DateFormat("MM-dd-yy") Date date)
```

---

## Exception Handling, simple!

ExceptionMappers are
--
 custom..
--
 application provided..
--
 components that can catch thrown application exceptions and write specific HTTP responses!
--

```java
public interface ExceptionMapper<E> {
   Response toResponse(E exception);
}
```

You register ExceptionMappers the same way you do MessageBodyReader/Writers!

---

# Interceptors

--

.center[<img src="interceptor.jpg" alt="Interceptor" width="80%">]

---

# Interceptors

RESTeasy has the capability to intercept JAX-RS invocations and route them through listener-like objects called interceptors.

There are **4 different interception points** on the serverside:
* wrapping around **MessageBodyWriter** invocations
* wrapping around **MessageBodyReader** invocations
* **pre-processors** the intercept the incoming request before anything is unmarshalled
* **post-processors** which are invoked right after the JAX-RS method is finished.

---

## Pre/Post-ProcessInterceptor

Must be annotated with `@ServerInterceptor` (and `@Provider`)

```java
public interface PreProcessInterceptor {
  ServerResponse preProcess(HttpRequest request,
                            ResourceMethod method)
    throws Failure, WebApplicationException;
}
```

```java
public interface PostProcessInterceptor {
  void postProcess(ServerResponse response);
}
```

---

## Binding Interceptors

But what if I want to intercept (i.e.) a method only?

--

You can fine tune this by having your interceptors implement the `org.jboss.resteasy.spi.AcceptedByMethod` interface:

```java
public interface AcceptedByMethod {
   public boolean accept(Class declaring, Method method);
}
```

--

You can also extract **annotations** from `Class` or `Method`! Example?

---

## Interceptor w/binding example

```java
@Provider
@ServerInterceptor
public class MyHeaderDecorator implements
  MessageBodyWriterInterceptor, AcceptedByMethod {
  public boolean accept(Class declaring, Method method) {
     return method.isAnnotationPresent(Custom.class);
  }
 public void write(MessageBodyWriterContext context)
  throws IOException, WebApplicationException {
    context.getHeaders().add("My-Header", "custom");
    context.proceed();
 }
}
```

???
They also have Ordering and Precedence (built-in or custom).

---

# Bad news, bug or feature?

HTTP verbs that have a request body (POST, PUT) must be provided with the associated Character Encoding (using the `Content-Type` header).

--
By default browser AJAX or some other client requests doesn't specify the Encoding and we would expect it to be `UTF-8`.

--

Sadly this isn't always true.
--
 RESTeasy 2.3.x defaults to `ASCII` and the application fails to decode the body content.

--

Wait, **PreProcessInterceptor** to the rescue!

---

```java
@Provider @ServerInterceptor @DecoderPrecedence
public class ContentTypeSetterPreProcessorInterceptor
  implements PreProcessInterceptor, AcceptedByMethod {
  @Context
  private HttpServletRequest httpRequest;

  public ServerResponse preProcess(HttpRequest request, ResourceMethod method) throws Failure, WebApplicationException {
    try {
        if (Strings.isNullOrEmpty(httpRequest.getCharacterEncoding())) {
            httpRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
        }
    } catch (UnsupportedEncodingException e) {
        // Never
    }
    return null; // Avoid short-circuit
  }
```

---

### Continue...

```java
  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public boolean accept(Class declaring, Method method) {
    // First line probably avoidable
    return declaring.getAnnotation(Path.class) != null
      && (method.getAnnotation(POST.class) != null
      || method.getAnnotation(PUT.class) != null);
  }
}
```

--

If you can find a better solution to solve the problem, you are welcome!

---

# Other goodies

* GZIP Compression/Decompression
* Caching Features
* Asynchronous HTTP Request Processing
* **CDI Integration**
* Validation

## Links

* [JAX-RS Specification](https://jax-rs-spec.java.net/)
* [JavaEE 6 Tutorial](https://docs.oracle.com/javaee/6/tutorial/doc/giepu.html)
* [RESTEasy JAX-RS](https://docs.jboss.org/resteasy/docs/2.3.7.Final/userguide/html_single/)

---

class: center, middle

# Questions