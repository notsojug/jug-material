class: center, middle

# Retrofit

Type-safe HTTP client for Android and Java by [Square](https://squareup.com/).

---

class: center

# Why?

Do you remember the last time you had to write a client?

--

 Java compatibility

 Dependency problems

 More code to handle library quirks than to actually return the correct value or exception


---

## This is why

The client:

```
public interface GitHubService {
  @GET("users/{user}/repos")
  List<Repo> listRepos(@Path("user") String user);
}
```
--
How you call it:

```
GitHubService service = // some magic here
List<Repo> repos = service.listRepos("octocat");
```

---

## The magic

There is no magic:

```
RestAdapter retrofit = new RestAdapter.Builder()
    .setEndpoint("https://api.github.com/")
    .build();

GitHubService service = retrofit.create(GitHubService.class);
```

---

## Other examples
Query and path params

```
@GET("group/{id}/users")
List<User> groupList(@Path("id") int groupId,
                     @Query("sort") String sort);
```

Complex query parameters
```
@GET("group/{id}/users")
List<User> groupList(@Path("id") int groupId,
                     @QueryMap Map<String, String> options);
```

---

## Other examples

Post with body
```
@POST("users/new")
User createUser(@Body User user);
```

Forms
```
@FormUrlEncoded
@POST("user/edit")
User updateUser(@Field("first_name") String first,
                @Field("last_name") String last);
```

???

Objects are converted to JSON by default.

---

## Other examples

Fixed headers
```
@Headers("Cache-Control: max-age=640000")
@GET("widget/list")
List<Widget> widgetList();
```

Parametrized headers
```
@GET("user")
User getUser(@Header("TheAwesomeToken") String theAwesomeToken)
```

---

## Backends

Retrofit can use different http client backends

* Java's [HttpUrlConnection](https://docs.oracle.com/javase/6/docs/api/java/net/HttpURLConnection.html)
* Apache's [http client](https://hc.apache.org/)
* Square's [OkHttp](http://square.github.io/okhttp/)

???

If you don't specity a client and it finds OkHttp library, it uses that.

---

## Compatibility
Retrofit until version 1.9.0 supports java 6 (yay!)

--

Retrofit uses [Gson](https://github.com/google/gson) for JSON (de)serialization, which is fast enough, and java 6 compatible. But can use [Jackson](https://github.com/square/retrofit/blob/parent-1.9.0/retrofit-converters/jackson/src/main/java/retrofit/converter/JacksonConverter.java), if you rely heavily on its annotations.

---

# Other goodies:


* Can produce/consume JSON, XMl, Protobuf, ...
* Can be configured to handle errors differently (e.g. throwing certain exceptions)
* Can log events if needed
* Can be configured for async callbacks
* Supports rxjava reactive framework

---

## Links:

* [Square](https://squareup.com/)
* [Retrofit website](https://square.github.io/retrofit/)
* [Code on github](https://github.com/square/retrofit)

---

class: center, middle

# Questions
