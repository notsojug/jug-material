class: center, middle

# Guava

Which is... a fruit

---

# What/Who?

Just kidding... it's Google's collection of utilities:

- Java 6

- Battle-tested in production.

- Well unit-tested (286,000 test cases, as of 2014).

---

# So what?

- Simple, clean code
- Collection utilities 
- Defensive coding aids
- (limited) functional-style programming
- ...

---

## Example: Collections constructors 

```
List<String> list = 
	Lists.newArrayList("one", "two", null, "yo");
```
```
Set<Integer> set = Sets.newHashSetWithExpectedSize(10);
```
```
Comparator<Elephants> ce = /* ... */
Map<Elephants, Strings> map = Maps.newTreeMap(ce);
```

???

Also set algebra, map transform/filter, ...

---

## Example: Collections2

```
List<String> warnames = Collections2.transform(elephants, 
	new Function<Elephant, String>() {
		@Override
		public String apply(Elephant input) {
			return input.getWarName();
		}
	});
```

---

## Example: Immutable* collections

```
ImmutableMap<String, Integer> m = 
	ImmutableMap.of("long", 1, "john", 2, "silver", 3);
```
```
ImmutableSet<String> s =
	ImmutableSet.of("never","gonna","give","you","up");
```

Those are useful for const values (list of valid strings/objects/whatever...).
Deeply immutable, quite different from `Collections.unmodifiable*(*)`.

???

Collections.unmodifiableX is still susceptible of mutability, since anyone holding the original reference can access to the original map methods.

---

## Example: MultiSet, BiMap

```
Multiset<String> ms = HashMultiset.create();
ms.add("yo");
ms.count("yo"); // 1
ms.add("yo");
ms.count("yo"); // 2
```
```
BiMap<String, Integer> bm = HashBiMap.create();
bm.put("five", 5);
BiMap<Integer,String> inverse = bm.inverse();
inverse.get(5); // "five"
```
---

## Example: Objects/MoreObjects/Enums

```
@Override
public int hashCode(){
	return Objects.hashCode(my, very, 
		lengthy, fields, list); 
}
```
```
MoreObjects.firstNonNull(getPotentiallyNull(), "default");
```

???
Have a look also at `MoreObjects.ToStringHelper`

---

## More

Things you may want to look at:

- ComparisonChain
- FluentIterable (use with caution)
- CharMatcher
- HttpHeaders
- Cache/CacheBuilder
- concurrent package
- event-bus system
- ... testing aids
- ... more? 