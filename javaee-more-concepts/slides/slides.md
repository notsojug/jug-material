class: center, middle

# CDI: more goodies

To `@Inject`, and beyond...

---

# Last time we did this:

```
@Inject
public UserManager(
		ConfirmationEmailSender confirmationEmailSender, 
		DbAddUserCommand dbAddUserCommand,
		DbDeleteUserCommand dbDeleteUserCommand,
		DbGetUserCommand dbGetUserCommand) {
	super();
	this.confirmationEmailSender = confirmationEmailSender;
	this.dbAddUserCommand = dbAddUserCommand;
	this.dbDeleteUserCommand = dbDeleteUserCommand;
	this.dbGetUserCommand = dbGetUserCommand;
}
```

---

# And this:

```
@Inject
VariousConstructors standard;

@Inject
@Configured
VariousConstructors configured;
```

---

# And this:

```
@Produces
OnlyWithFactory getOnlyWithFactory(){
	OnlyWithFactory instance = OnlyWithFactory.create("42");
	return instance;
}
```

---

### Alternative implementations: starting point

```
public interface AddUserCommand {
	int addUser(String email, String password);
}

public interface DeleteUserCommand {
	void deleteUser(int userId);
}
public interface GetUserCommand {
	User getUser(int userId);
}
```

---
### Alternative implementations: starting point

```
public class DbGetUserCommand implements GetUserCommand {
	private final Database database;

	@Inject
	public DbGetUserCommand(Database database) {
		this.database = database;
	}
	
	@Override
	public User getUser(int userId) {
		return database.querySingle(User.class, 
			"Select * from users where userId=?", userId);
	}
}

```

---

# Alternative implementations

What if:

- you need a different implementation of a class for a specific client
- database is down and you need to test the app
- you are implementing a new version of a class, but time is running out and you need to fall back to default

---

# Alternative implementations

Fear no more:

```
@Alternative
public class InMemoryUserRepo implements 
						AddUserCommand, 
						DeleteUserCommand, 
						GetUserCommand {
	// ...
}


```

--

Container ignores it, unless you specify it in `beans.xml`


---

# Alternative implementations

How to activate:

```xml
<beans ... >
    <alternatives>
        <class>jug.alternative.memory.InMemoryUserRepo</class>
    </alternatives>
</beans>
```

---

### Interceptors: Repeated cross-functional behaviours


> An interceptor is a class used to interpose in method invocations or lifecycle events that occur in an associated target class. The interceptor performs tasks, such as logging or auditing, that are separate from the business logic of the application and are repeated often within an application.

---

### Interceptors: how to

The annotation:

```
@Inherited
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
public @interface Logged {
	
}
```
---

### Interceptors: how to

The class:

```
public class ClassToBeLogged {

	@Logged
	public String someMethod(String aParam, int anotherParam) {
		return "been there, done that";
	}
}
```
---

### Interceptors: how to

```
@Logged
@Interceptor
public class LoggedInterceptor implements Serializable {
	@AroundInvoke
	public Object logMyCall(InvocationContext context) 
		throws Exception {
		Method method = context.getMethod();
		// do what you need 
		System.out.println("called " + method.getName() + 
			" on class " + method.getDeclaringClass()+ 
			" params=" + printParams(context.getParameters()));
		
		// invoke the method or let the next interceptor do its work
		return context.proceed();
	}
}
```
---

### Interceptors: how to

How to activate:

```xml
<beans ... >
    <interceptors>
        <class>jug.interceptor.LoggedInterceptor</class>
    </interceptors>
</beans>
```
---

### Extend functionality without inheritance


--

.center[
AKA: decorators
]

---

# Decorators

Example interface

```
public interface Client {

	String getValueFromTheInternet(String id);

	String putValueToTheInternet(String id, String value);
}
```

---

# Decorators

Example implementation:

```
public class SomeClient implements Client {
	@Override
	public String getValueFromTheInternet(String id){
		return "42"; 
	}

	@Override
	public String putValueToTheInternet(String id, String value){
		return "done";
	}
}
```



---

# Decorators

The decorator:

```
@Decorator
public abstract class SomeDelayedClient implements Client{

	@Inject @Delegate @Any
	Client inner;

	@Override
	public String getValueFromTheInternet(String id) {
		sleepFiveSecondsIfUserIsYo(id);
		return "delayed=" + inner.getValueFromTheInternet(id);
	}
}
```
---

# Decorators

How to activate:
```xml
<beans ... >
    <decorators>
        <class>jug.decorator.SomeDelayedClient</class>
    </decorators>
</beans>
```
---


# Even more goodies, not covered here

- CDI events: fire-and-forget decoupled computation, like JMS without persistence.
	* container-fired events and how to use them.
- Specialized instances: like @Alternative, but always-on.
- Stereotypes: group scope and qualifiers to a single annotation.

---

## Links:

* Java EE 6 [https://docs.oracle.com/javaee/6/tutorial/doc/](see here the tutorial)

---

class: center, middle

# Questions
