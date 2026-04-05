
---

# 🔹 1. Core / Stereotype Annotations

---
Web Sources: 
1. [Spring Core Annotations](https://www.baeldung.com/spring-core-annotations)
2. [Annotations in Spring Framework](https://howtodoinjava.com/spring-core/spring-annotations/) 
---

## ✅ `@Component`

👉 Marks a class as a Spring Bean.

```java
@Component
public class MyComponent {
}
```

---

## ✅ `@Service`

👉 Used for **business logic layer**

```java
@Service
public class UserService {
}
```

---

## ✅ `@Repository`

👉 Used for **DAO layer** (adds exception translation)

```java
@Repository
public class UserRepository {
}
```

---

## ✅ `@Controller`

👉 Used in **Spring MVC (returns views)**

```java
@Controller
public class HomeController {
}
```

---

## ✅ `@RestController`

👉 Combines `@Controller + @ResponseBody`

```java
@RestController
public class ApiController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
```

---

# 🔹 2. Dependency Injection Annotations

---

## ✅ `@Autowired`

👉 Injects dependency automatically

👉 We can use this annotation with a constructor, setter, or field injection.

👉 @Autowired(required = true) — The Default
> This is the "strict" mode. You are telling Spring: "I cannot function without this dependency. If you can't find it, don't even bother starting the app."

👉 @Autowired(required = false) — The Optional
>This is the "flexible" mode. You are telling Spring: "I'd like to have this bean if it exists, but if it doesn't, just leave the variable as null and keep going."

### Constructor injection:
We’ll see that an instance of Engine is injected by Spring as an argument to the Car constructor.
Starting with version 4.3, we don’t need to annotate constructors with @Autowired explicitly unless we declare at least two constructors.

> **Note: that if we use constructor injection, all constructor arguments are mandatory.**

```Java
class Car {
Engine engine;

    @Autowired
    Car(Engine engine) {
        this.engine = engine;
    }
}
```
### Setter injection:
The setter method is called with the instance of Engine when Car is created.

```java
class Car {
Engine engine;

    @Autowired
    void setEngine(Engine engine) {
        this.engine = engine;
    }
}
```
### Field injection:

```java
class Car {
    @Autowired
    Engine engine;
}
```

For more details visit our articles about [@Autowired](https://www.baeldung.com/spring-autowire) and [constructor injection](https://www.baeldung.com/constructor-injection-in-spring).

---

## ✅ `@Qualifier`

👉 Used when multiple beans exists, we use @Qualifier along with @Autowired to provide the bean id or bean name we want to use in ambiguous situations.

For example, the following two beans implement the same interface:
```java
class Bike implements Vehicle {}

class Car implements Vehicle {}
```
If Spring needs to inject a Vehicle bean, it ends up with multiple matching definitions. In such cases, we can provide a bean’s name explicitly using the @Qualifier annotation.

Using constructor injection:
```java
@Autowired
Biker(@Qualifier("bike") Vehicle vehicle) {
    this.vehicle = vehicle;
}
```
Using setter injection:
```java
@Autowired
void setVehicle(@Qualifier("bike") Vehicle vehicle) {
    this.vehicle = vehicle;
}
```
Alternatively:
```java
@Autowired
@Qualifier("bike")
void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
}
```
Using field injection:
```java
@Autowired
@Qualifier("bike")
Vehicle vehicle;
```
For a more detailed description, please read this [article](https://www.baeldung.com/spring-autowire).

---

## ✅ `@Primary`

👉 Marks default bean

```java
@Primary
@Component
public class DefaultService implements Service {
}
```

---

## ✅ `@Value`

👉 Inject values from properties

```java
@Value("${server.port}")
private int port;
```

---

# 🔹 3. Configuration Annotations

---

## ✅ `@Configuration`

👉 Defines configuration class

```java
@Configuration
public class AppConfig {
}
```

---

## ✅ `@Bean`

👉 Creates bean manually

@Bean marks a factory method which instantiates a Spring bean:
``` java
@Bean
Engine engine() {
    return new Engine();
}
```
Spring calls these methods when a new instance of the return type is required.

The resulting bean has the same name as the factory method. If we want to name it differently, we can do so with the name or the value arguments of this annotation (the argument value is an alias for the argument name):
```java
@Bean("engine")
Engine getEngine() {
    return new Engine();
}
```
Note, that all methods annotated with @Bean must be in @Configuration classes.

---

## ✅ `@ComponentScan`

👉 Scans packages

```java
@ComponentScan("com.example")
```

---

# 🔹 4. Spring Boot Annotations

---

## ✅ `@SpringBootApplication`

👉 Main entry point

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

👉 Combines:

* `@Configuration`
* `@EnableAutoConfiguration`
* `@ComponentScan`

---

## ✅ `@EnableAutoConfiguration`

👉 Enables auto config

---

## ✅ `@ConfigurationProperties`

👉 Maps properties to class

```java
@ConfigurationProperties(prefix="app")
public class AppConfig {
    private String name;
}
```

---

# 🔹 5. Web / REST Annotations

---

## ✅ `@RequestMapping`

👉 Maps HTTP requests

```java
@RequestMapping("/api")
```

---

## ✅ `@GetMapping`

```java
@GetMapping("/users")
```

---

## ✅ `@PostMapping`

```java
@PostMapping("/users")
```

---

## ✅ `@PutMapping`

```java
@PutMapping("/users/{id}")
```

---

## ✅ `@DeleteMapping`

```java
@DeleteMapping("/users/{id}")
```

---

## ✅ `@PathVariable`

```java
@GetMapping("/user/{id}")
public String get(@PathVariable int id) {
}
```

---

## ✅ `@RequestParam`

```java
@GetMapping("/search")
public String search(@RequestParam String name) {
}
```

---

## ✅ `@RequestBody`

```java
@PostMapping
public void save(@RequestBody User user) {
}
```

---

## ✅ `@ResponseBody`

👉 Converts response to JSON

---

# 🔹 6. JPA / Hibernate Annotations

---

## ✅ `@Entity`

```java
@Entity
public class User {
}
```

---

## ✅ `@Table`

```java
@Table(name="users")
```

---

## ✅ `@Id`

```java
@Id
private int id;
```

---

## ✅ `@GeneratedValue`

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

---

## ✅ `@Column`

```java
@Column(name="user_name")
```

---

## ✅ `@OneToMany`, `@ManyToOne`, `@OneToOne`, `@ManyToMany`

```java
@OneToMany(mappedBy="user")
```

---

## ✅ `@Transactional`

```java
@Transactional
public void saveUser() {
}
```

---

# 🔹 7. Exception Handling

---

## ✅ `@ExceptionHandler`

```java
@ExceptionHandler(Exception.class)
public String handle() {
}
```

---

## ✅ `@ControllerAdvice`

```java
@ControllerAdvice
public class GlobalException {
}
```

---

## ✅ `@RestControllerAdvice`

```java
@RestControllerAdvice
```

---

# 🔹 8. Testing Annotations

---

## ✅ `@SpringBootTest`

```java
@SpringBootTest
```

---

## ✅ `@MockBean`

```java
@MockBean
private UserService service;
```

---

## ✅ `@Autowired` (in tests)

---

# 🔹 9. AOP Annotations

---

## ✅ `@Aspect`

```java
@Aspect
```

---

## ✅ `@Before`

```java
@Before("execution(* com.service.*.*(..))")
```

---

## ✅ `@After`

```java
@After("execution(* com.service.*.*(..))")
```

---

## 🔹 10. Misc Important

---

## ✅ `@Lazy`

```java
@Lazy
```

---

## ✅ `@Profile`

```java
@Profile("dev")
```

---

## ✅ `@Scope`

```java
@Scope("prototype")
```

---

# 🔥 Must-Know (Top 10 for Interview)

👉 Focus on:

* `@SpringBootApplication`
* `@RestController`
* `@Autowired`
* `@Service`
* `@Repository`
* `@Component`
* `@GetMapping`
* `@RequestBody`
* `@Entity`
* `@Transactional`

---

# 🎯 Interview One-Liner

👉

> Spring annotations are used to configure, manage beans, handle web requests, and integrate with databases in a declarative way.

---
