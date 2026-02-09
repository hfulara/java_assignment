# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
I would refactor gradually to improve consistency, but only where it adds clear value. 
Simple domains can remain straightforward, while more complex domains should keep stronger abstractions. 
The goal would be clarity and maintainability, not enforcing a single pattern everywhere.
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
OpenAPI-first development provides strong contracts and is ideal for critical or externally consumed APIs. 
Directly coded endpoints allow faster iteration and are simpler for internal or stable APIs. 
I would keep a mixed approach, choosing the strategy based on the importance and consumers of each API.

```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```txt
I would prioritize unit tests for core business logic where failures are most costly. 
This would be complemented by a small number of end-to-end integration tests for critical flows. 
Test coverage would grow incrementally as features evolve rather than aiming for full coverage upfront.
```