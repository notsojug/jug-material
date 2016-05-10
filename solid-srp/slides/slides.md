class: center, middle

# Single Responsibility Principle

> A class should have only a single responsibility (i.e. only one potential change in the software's specification should be able to affect the specification of the class)

---

# SOLID principles

- Principles for good object-oriented programming.
- Introduced by Michael Feathers, named by Robert C. Martin (aka Uncle Bob Martin)
- Wikipedia: "The principles, when applied together, intend to make it more likely that a programmer will create a system that is easy to maintain and extend over time."

<https://en.wikipedia.org/w/index.php?title=SOLID_(object-oriented_design)>

???

Treat them as guidelines

---

# SOLID principles

- Single responsibility principle
- Open/closed principle
- Liskov substitution principle
- Interface segregation principle
- Dependency inversion principle

---

# Single responsibility principle

- To be read: a class should have a single **reason** to change.
- A class should do one thing => Separation of Concerns
- Opposite: God object

Thus classes should be:
- small, easy to read
- easy to mantain
- easy to unit-test

---

# Example


---

# Pros and Cons

Pros:

- Class names represent the correct behaviour 
- Classes are easier to read and test

Cons:

- A lot of classes
- Harder to grasp the big picture

---

# Is it only for programming?

Have you ever heard about microservices?

[http://martinfowler.com/articles/microservices.html]()
 
???

Fowler's reasoning is built around the concept of Bounded Context, which is roughly the same as SRP/Separation of concerns  


---

## Links:

* SOLID  <https://en.wikipedia.org/w/index.php?title=SOLID_(object-oriented_design)>
* Inspiration for this slides: [http://www.slideshare.net/kindblad/the-sngle-responsibility-principle]()
* An article from Martin Fowler about microservices 
[http://martinfowler.com/articles/microservices.html]()

---

class: center, middle

# Questions
