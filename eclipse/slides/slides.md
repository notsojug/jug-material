class: center, middle

# Eclipse for ninjas

Thou shalt know the tools.

---

# Eclipse as an editor

- it's slow
--

- it's heavy
--

- it's written in java
--
(you don't say?)

--

The problem is...

---
class: center, middle

# ECLIPSE IS *NOT* AN EDITOR

--

Eclipse is a nuclear power plant that also happens to help you coding in any JVM language (and more)

---

## Did you know... Auto-complete?

You can auto complete almost everyting just by writing a bunch of characters and/or pressing `Ctrl-space` ?

.center[![Ctrl-space](img/ctrl-space.gif)]

--

Of course you know, but if you didn't: i have bad news for ya...

---
## Auto-complete


It has **fuzzy search**, thanks to Code recommenders' plugin.

I.e. if you have long methods' names with almost the same prefix:

- `setTheEndOfTheWorldAsYouKnowItById`
- `setTheEndOfTheWorldAsYouKnowItByName`
- ...

you don't need to type  `setTheEndOfTheW...` and so on to get the right, just type some characters like `swid`, press the magic shortcut, and BAM, close the issue and go home.

???

It works also on selected preferences dialogs.

---
## Did you know... Contextual fix?

Eclipse is smart enough to suggest _and do_ repair your code if you kindly ask it to.

.center[![Ctrl-1](img/ctrl-1.gif)]

No more catching the tooltip mouse clicking... just press `Ctrl-1` and off to the next bug...

---
## Did you know... Quick assist?

Shortcut: `Ctrl-2 (wait) {F,L,M,R}`

This shortcut can do a lot of things:

- assign an object to a variable
- assign an object to a field
- extract a method from selected lines
- rename a piece of code (variable, field, method, class, ...)

Some of these are so common they have their own shortcut, but this shortcut shows a tooltip suggesting the next button you should tap ;)


---
## Alternative shortcuts for quick assist

- You can rename by using `Alt-Shift-R`.
- You can assign an object to a variable with `Alt-Shift-L`
- You can extract a method from selected lines with `Alt-Shift-M`
- There's a way to promote a variable to a field via the refactor menu, see next slides...

---
## Did you know... Quick navigation?

Press `Ctrl-3`, type the name of dialogs, files, perspectives, etc.. and BAM, you reach it!

.center[![ctrl-3](img/ctrl-3.png)]

---
## Did you know... tests?

You hate tests, right?

--

No you don't, because you are a very good person.

--

Here's a way to create a test method:

You type `test`, press `Ctrl-space`, `Enter`, type the name of the test, and code it.

--

Because it should not be hard to write a test: eclipse knows it, and happily helps you.

---
class: center, middle

<img src="img/test.gif" alt="test" style="width: 100%;"/>

*CAN YOU SEE IT NOW???*

---
## Did you know... _running_ tests?

So you have test and a happy mouse to click `Run`.

--

No, you don't: you press `Alt-shift-X T` because you're faster than light.

--

Eclipse is also smart enough to run either the full test class, in general, or the single test if you select the current method name.

---
## Did you know... _debugging_ tests?

There's also a shortcut to launch tests in debug mode: `Alt-shift-D T`

Of course tests shall be so straight-up obvious you don't need debugging at all, but if you need it, there it is...

---
## Did you know... run coverage?

With all these tests you may as well launch the coverage for this test: `Alt-shift-E T`

--

Because, you know, you may have missed a line... or a dozen...


---
## Did you know... running other things?

Let's say you want to run classes with `main` methods.

--

First of all: is it really a main method or just an excuse for not writing a test?

--

But if you really (tm) need to run a main method:

- Normal mode: `Alt-shift-X  J`
- Debug mode: `Alt-shift-D  J`

---
## Did you know... opening things?

You can open files from everywhere:

- Types using `Ctrl-shift-T`
- Generic files using `Ctrl-shift-R`

These dialogs let you type parts of the file/path/package and open it in edit mode.

They do not support fuzzy search (yet), but accept `*` wildcards.


---
## Did you know... refactor and source menus?

.left-column[
Refactor `Alt-shift-R`
![refactor](img/refactor.png)
]

--

.right-column[
Source `Alt-shift-S`
![source](img/source.png)
]

---
class: center, middle

Of course you don't need half of source menu, because you have immutable beans and use libraries such as [immutables](https://immutables.github.io/), right?


---

class: center, middle

# Questions
