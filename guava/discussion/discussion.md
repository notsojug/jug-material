
# Questions:
1. Which algorithm is used for `Objects.hashCode`?
It does not matter, as long as `Objejct::hashCode` contract is honored
Passiamo al codice
2. Are there parts of guava which are covered by Java 7 or 8? 
Yes, please find these info in the javadoc
3. Why would I _not_ want to use guava?
You must take in account the size of it (over 10 MB, unless you use ProGuard). But, in most cases, the code de-cluttering features outweight the size problems.
4. Is it thread-safe?
The library's classes are thread-safe, unless specified in the javadoc. 
5. What about the licence?
Apache 2.0 License
6. Should I use it over Apache Commons? Are there equivalence tables around?
There are several sites that cover equivalences between the libraries, and the wiki also covers some of them.
You can use ProGuard to remove unnecessary parts from the resulting build.
7. [Suggestion] Speakers should comment the code in the repo.
