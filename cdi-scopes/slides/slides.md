class: center, middle

# (Context/Container) Dependency Injection

## Scopes: lifecycle and visibility of Instances

---

# Each _contextual instance_ of a bean is a singleton in that context

Contextual instance of the bean is shared by all objects that execute in the same context.
Default scope is `Dependent` (the same lifecycle as the client).
--

.left-column[
* `RequestScoped`: the context is a single HTTP request.
* `SessionScoped`: the context is across multiple HTTP requests.
* `ApplicationScoped`: the context is the web application.]
--

.right-column[
* `Singleton`: (pseudo scope) same as `ApplicationScoped` but without a proxy object.
* `ConversationScoped`: JSF applications may use this, we don't cover it.]

---

# Example usages

* `@RequestScoped`: DTO/Models.

--

* `@SessionScoped`: **user informations**.

--

* `@ApplicationScoped`: datas shared by the entire application, caches.

--

* `@Dependent`: everywhere a new instance of a bean is needed, without a proxy object.

--

**It's not advised change the scope of a bean**, but what if we need a `@Dependent` version of a bean (a new instance bounded to the client scope)?

---

# Proxies

Clients of an injected bean do not usually hold a direct reference to a bean instance 
--
unless the bean  has `@Dependent` scope.

--

What if an **application scoped bean** holds a direct reference to a **request scoped bean**? 
--
The application-scoped bean **is shared between many different requests** but each request should see a **different instance** of the request scoped bean.

--

This is not a problem, but what if the container needs to **serialize** the application scoped bean for _memory optimization_? 
--
The container must indirect all injected references to the bean through a **proxy object**.

???
The client proxy also allows beans bound to contexts such as the session context to be serialized to disk without recursively serializing other injected beans.

---

# Proxies

The following Java types cannot be proxied by the container:

* classes which don't have a non-private empty constructor
* classes which are declared final or have a final method
* arrays and primitive types

--

They still be injected with `@Dependent` scope
--
, or else you can:

* add a constructor with no parameters to `X`
* change the type of the injection point to `Instance<X>`
* introduce an interface `Y`, implemented by the injected bean, and change the type of the injection point to `Y`

---

# The @New qualifier

```
@SessionScoped
public class Calculator { ... }
```

So the following injected attributes each get a different instance of Calculator:

```
public class PaymentCalc {
  @Inject Calculator calculator;
  @Inject @New Calculator newCalculator;
}
```

This feature is particularly useful with producer methods.

---

# Producer methods

A producer method acts as a source of objects to be injected, where:

* the objects to be injected are not required to be instances of beans,
* the concrete type of the objects to be injected may vary at runtime or
* the objects require some custom initialization

--

producer methods let us:
.left-column[* expose a JPA entity as a bean,
* expose any JDK class as a bean,
* vary the implementation of a bean type at runtime,]

.right-column[
* define multiple beans, **with different scopes or initialization**, for the same implementation class.]

---

# Scope of a producer method

The scope of the producer method defaults to `@Dependent`.

```
@Produces @SessionScoped
public PaymentStrategy getPaymentStrategy() { ... }
```

Now, when the producer method is called, the returned PaymentStrategy will be bound to the session context. **The producer method won't be called again in the same session.**

--

### A producer method **does not inherit the scope of the bean that declares the method**.

???
There are two different beans here: the producer method, and the bean which declares it. The scope of the producer method determines how often the method will be called, and the lifecycle of the objects returned by the method. The scope of the bean that declares the producer method determines the lifecycle of the object upon which the producer method is invoked.

---

# Injection into producer methods

```
@Produces @Preferred @SessionScoped
public PaymentStrategy getPaymentStrategy(CreditCardPaymentStrategy ccps,
                                          CheckPaymentStrategy cps,
                                          PayPalPaymentStrategy ppps) {
   switch (paymentStrategy) {
      case CREDIT_CARD: return ccps;
      case CHEQUE: return cps;
      case PAYPAL: return ppps;
      default: return null;
   } 
}
```
--

Wait, what if `CreditCardPaymentStrategy` is a request-scoped bean?

???
The request scoped object will be destroyed by the container before the session ends, but the reference to the object will be left "hanging" in the session scope. This error will not be detected by the container, so please take extra care when returning bean instances from producer methods!

A better option would be to change the scope of the producer method to `@Dependent` or `@RequestScoped`.

But a more common solution is to use the special `@New` qualifier annotation.

---

# Use of **@New** with producer methods

```
@Produces @Preferred @SessionScoped
public PaymentStrategy getPaymentStrategy(@New
                                          CreditCardPaymentStrategy ccps,
                                          @New
                                          CheckPaymentStrategy cps,
                                          @New
                                          PayPalPaymentStrategy ppps) {
   switch (paymentStrategy) {
      case CREDIT_CARD: return ccps;
      case CHEQUE: return cps;
      case PAYPAL: return ppps;
      default: return null;
   } 
}
```

???
Then a new dependent instance of CreditCardPaymentStrategy will be created, passed to the producer method, returned by the producer method and finally bound to the session context. The dependent object won't be destroyed until the Preferences object is destroyed, at the end of the session.

---

# Disposer methods

Some producer methods return objects that require explicit destruction.

```
@Produces @RequestScoped Connection connect(User user) {
   return createConnection(user.getId(), user.getPassword());
}
```

Destruction can be performed by a matching disposer method, defined by the same class as the producer method:

```
void close(@Disposes Connection connection) {
   connection.close();
}
```

???
The disposer method must have at least one parameter, annotated @Disposes, with the same type and qualifiers as the producer method.

---

# And my Enterprise Java Beans?

Still there doing the good job.

--

.left-column[They provide:

* Remote access.
* MDB/JMS.
* Timers!
* Asynchronous methods.
* JPA.
* Locking/concurrent access.]

--

.right-column[When injecting EJBs:
* Use `@Inject` to get contextual injection.
* Use `@EJB` **ONLY** for remote session beans.]

---

# And my Enterprise Java Beans?

EJBs are CDI managed beans!

* Stateless EJB: `@Dependent` scope
* Stateful EJB: any scope
* Singleton EJB: `@ApplicationScoped`

--

## Converse is not true! CDI beans are not EJBs!

Oracle/JCP are moving EJB benefits into CDI spec.

---

## Links:

* [CDI bestpractices](http://www.slideshare.net/kelapure/2012-0409v2tdp1167cdibestpracticesfinal)
* [EJB and CDI - Alignment and Strategy](http://www.slideshare.net/delabassee/ejb-and-cdi-alignment-and-strategy)
* [Introduction to CDI and DI in Java EE 6](http://www.slideshare.net/rayploski/introduction-to-cdi-and-di-in-java-ee-6)
* [CDI: How do I?](http://www.slideshare.net/agoncal/cdi-how-do-i)
* [WELD (CDI 1.0)](https://docs.jboss.org/weld/reference/1.1.16.Final/en-US/html_single/)

---

class: center, middle

# Questions
