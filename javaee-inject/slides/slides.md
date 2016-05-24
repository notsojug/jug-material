class: center, middle

# (Context/Container) Dependency Injection

Or: Dependency inversion principle applied easily with the aid of the container.

---

# Last time we did the following:

```
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

### Chef Tony: _Remember what your granny used to do?_

```
Database db = new Database();
UserManager userManager = new UserManager(
		new ConfirmationEmailSender(), new DbAddUserCommand(db),
		new DbDeleteUserCommand(db), new DbGetUserCommand(db));
ServiceLocator.registerUserManager(userManager);
		
// somewhere else
boolean someMethod(String someParam){
	UserManager um = ServiceLocator.findUserManager();
	um.getUser(0);
	return true;
}

```

--

...boring

---

# No more stress 

```
@Inject // <-- here comes the magic
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

# No more stress

```
public class SomewhereElse{
	@Inject // <-- moar magic
	UserManager userManager;
	
	boolean someMethod(String someParam){
		userManager.getUser(0);
		return true;
	}
}

```

---

# What is this sorcery?

- This "sorcery" is standard in Java EE (6 onward).
- JSR 299, JSR 330
- Inspired by Google's Guice, a dependency injection library

--

So important that the whole enterprise framework uses it throughout the code to do practially everything, from JAX-RS, to events, to `<insert function here>`.   

???

The specification is acqually broader, it includes a plethora of functionalities, like interceptors, alternative beans, decorators, events...

---

# How can I enable it?

You just need a file named `beans.xml` in the `META-INF`/`WEB-INF` directory of your archive.

The content should be:

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://java.sun.com/xml/ns/javaee"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
      http://java.sun.com/xml/ns/javaee/beans_1_0.xsd">
</beans>
``` 

Or just empty, unless you need some (interesting) advanced features.

---

# Where can I use it?

- Rest endpoints
- Events 
- JMS queues
- combined with EJBs (handle with care)

--

...all you need is an entry point, really...

---

# What can I @Inject ?

- Implementations of interfaces
- Classes
- Primitives
- Events' entrypoints (to fire events)
- Resources (sessions, EJBs, datasources)


???

You don't need to create interfaces for all your classes, you can make a tradeoff and inject classes directly.  

DEMO

---

# Dependency Injection outside a Java EE container

- Use Weld SE in a standalone jar (same workflow as in Java EE)
- Use Guice (slightly different workflow than Java EE)
- Use Guice/Dagger 2 (and ButterKnife) in Android

---

# Dependency Injection in tests

- With arquillian
- With guice
- With EasyMock (for most use cases) => no setup!

---

## Links:

* Java EE 6 [https://docs.oracle.com/javaee/6/tutorial/doc/](see here the tutorial)
* JSR 299 [https://jcp.org/en/jsr/detail?id=299]()
* JSR 330 [https://jcp.org/en/jsr/detail?id=330]()
* Weld SE [https://docs.jboss.org/weld/reference/latest-master/en-US/html/environments.html#weld-se]
* Guice [https://github.com/google/guice]()
* Dagger [https://github.com/square/dagger]()
* Dagger 2 [https://github.com/google/dagger]()
* ButterKnife [https://github.com/JakeWharton/butterknife]()

---

class: center, middle

# Questions
