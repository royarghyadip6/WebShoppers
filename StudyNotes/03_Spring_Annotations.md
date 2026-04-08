
---

# All Annotations

---

Web Sources:
1. [Spring Core Annotations](https://www.baeldung.com/spring-core-annotations)
2. [Annotations in Spring Framework](https://howtodoinjava.com/spring-core/spring-annotations/)
---

# 🔹 1. Core / Stereotype Annotations

---

## ✅ 1.1 Context Configuration Annotations
We can configure the application context with the annotations described in this section.

### ✅ 1.1.1. `@Profile`
👉 If we want Spring to use a @Component class or a @Bean method only when a specific profile is active, we can mark it with @Profile. We can configure the name of the profile with the value argument of the annotation:
```java
@Component
@Profile("sportDay")
class Bike implements Vehicle {
    
}
```
You can read more about profiles in this article.

### ✅ 1.1.2. `@Import`
👉 We can use specific @Configuration classes without component scanning with this annotation. We can provide those classes with @Import‘s value argument:

```java
@Import(VehiclePartSupplier.class)
class VehicleFactoryConfig {
    
}
```

### ✅ 1.1.3. `@ImportResource`

👉 We can import XML configurations with this annotation. We can specify the XML file locations with the locations argument, or with its alias, the value argument:

```java
@Configuration
@ImportResource("classpath:/annotations.xml")
class VehicleFactoryConfig {
    
}
```
### ✅ 1.1.4. `@PropertySource`

👉 With this annotation, we can define property files for application settings:

```java
@Configuration
@PropertySource("classpath:/annotations.properties")
class VehicleFactoryConfig {
    
}
```

@PropertySource leverages the Java 8 repeating annotations feature, which means we can mark a class with it multiple times:
```java
@Configuration
@PropertySource("classpath:/annotations.properties")
@PropertySource("classpath:/vehicle-factory.properties")
class VehicleFactoryConfig {
    
}
```

### ✅ 1.1.5. `@PropertySources`

👉 We can use this annotation to specify multiple @PropertySource configurations:

```java
@Configuration
@PropertySources({
@PropertySource("classpath:/annotations.properties"),
@PropertySource("classpath:/vehicle-factory.properties")
})
class VehicleFactoryConfig {
    
}
```

Note, that since Java 8 we can achieve the same with the repeating annotations feature as described above.

---

## 🔹 1.2. Dependency Injection Annotations

---

### ✅ 1.2.1. `@Bean`

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

### ✅ 1.2.2 `@Autowired`

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

### ✅ 1.2.3. `@Qualifier`

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

### ✅ 1.2.4. `@Primary`

👉 Marks default bean

👉 Sometimes we need to define multiple beans of the same type. In these cases, the injection will be unsuccessful because Spring has no clue which bean we need.

👉 We already saw an option to deal with this scenario: marking all the wiring points with @Qualifier and specify the name of the required bean.

👉 However, most of the time we need a specific bean and rarely the others. We can use @Primary to simplify this case: if we mark the most frequently used bean with @Primary it will be chosen on unqualified injection points:

```java
@Component
@Primary
class Car implements Vehicle {}

@Component
class Bike implements Vehicle {}

@Component
class Driver {
    @Autowired
    Vehicle vehicle;
}

@Component
class Biker {
    @Autowired
    @Qualifier("bike")
    Vehicle vehicle;
}
```
---

### ✅ 1.2.5. `@Value`

👉 We can use @Value for injecting property values into beans. It’s compatible with constructor, setter, and field injection.

Constructor injection:
```java
Engine(@Value("8") int cylinderCount) {
    this.cylinderCount = cylinderCount;
}
```
Setter injection:
```java
@Autowired
void setCylinderCount(@Value("8") int cylinderCount) {
    this.cylinderCount = cylinderCount;
}
```
Alternatively:
```java
@Value("8")
void setCylinderCount(int cylinderCount) {
    this.cylinderCount = cylinderCount;
}
```
Field injection:
```java
@Value("8")
int cylinderCount;
```
Of course, injecting static values isn’t useful. Therefore, we can use placeholder strings in @Value to wire values defined in external sources, for example, in .properties or .yaml files.

Let’s assume the following .properties file:
```properties
engine.fuelType=petrol
```
We can inject the value of engine.fuelType with the following:
```java
@Value("${engine.fuelType}")
String fuelType;
```
We can use @Value even with SpEL. More advanced examples can be found in our [article about @Value](https://www.baeldung.com/spring-value-annotation).

---

### ✅ 1.2.6. `@DependsOn`
👉 We can use this annotation to make Spring initialize other beans before the annotated one. Usually, this behavior is automatic, based on the explicit dependencies between beans.

👉 We only need this annotation when the dependencies are implicit, for example, JDBC driver loading or static variable initialization.

👉 We can use @DependsOn on the dependent class specifying the names of the dependency beans. The annotation’s value argument needs an array containing the dependency bean names:

```java
@DependsOn("engine")
class Car implements Vehicle {}
```

Alternatively, if we define a bean with the @Bean annotation, the factory method should be annotated with @DependsOn:

```java
@Bean
@DependsOn("fuel")
Engine engine() {
    return new Engine();
}
```

---

### ✅ 1.2.7. `@Lazy`

👉We use @Lazy when we want to initialize our bean lazily. By default, Spring creates all singleton beans eagerly at the startup/bootstrapping of the application context.

However, there are cases when **we need to create a bean when we request it, not at application startup**.

This annotation behaves differently depending on where we exactly place it. We can put it on:

* a @Bean annotated bean factory method, to delay the method call (hence the bean creation)
* a @Configuration class and all contained @Bean methods will be affected
* a @Component class, which is not a @Configuration class, this bean will be initialized lazily
* an @Autowired constructor, setter, or field, to load the dependency itself lazily (via proxy)
* This annotation has an argument named value with the default value of true. It is useful to override the default behavior.

For example, marking beans to be eagerly loaded when the global setting is lazy, or configure specific @Bean methods to eager loading in a @Configuration class marked with @Lazy:

```java
@Configuration
@Lazy
class VehicleFactoryConfig {

    @Bean
    @Lazy(false)
    Engine engine() {
        return new Engine();
    }
}
```

For further reading, please visit [this article](https://www.baeldung.com/spring-lazy-annotation).

---

### ✅ 1.2.8. `@Lookup`

👉 A method annotated with @Lookup tells Spring to return an instance of the method’s return type when we invoke it.

Detailed information about the annotation can be found in [this article](https://www.baeldung.com/spring-lookup).

---
### ✅ 1.2.9. `@Scope`

👉We use @Scope to define the scope of a @Component class or a @Bean definition. It can be either singleton, prototype, request, session, globalSession or some custom scope.

For example:
```java
@Component
@Scope("prototype")
class Engine {}
```

---

# 🔹 2. [Spring Bean Annotations](https://www.baeldung.com/spring-bean-annotations)

---

## ✅ `@ComponentScan`

👉 Spring can automatically scan a package for beans if component scanning is enabled.

```java
@ComponentScan("com.example")
```

> @ComponentScan configures which packages to scan for classes with annotation configuration. We can specify the base package names directly with one of the basePackages or value arguments (value is an alias for basePackages):
```java
@Configuration
@ComponentScan(basePackages = "com.baeldung.annotations")
class VehicleFactoryConfig {

}
```

Also, we can point to classes in the base packages with the basePackageClasses argument:
```java
@Configuration
@ComponentScan(basePackageClasses = VehicleFactoryConfig.class)
class VehicleFactoryConfig {
    
}
```
Both arguments are arrays so that we can provide multiple packages for each.

If no argument is specified, the scanning happens from the same package where the @ComponentScan annotated class is present.

@ComponentScan leverages the Java 8 repeating annotations feature, which means we can mark a class with it multiple times:
```java
@Configuration
@ComponentScan(basePackages = "com.baeldung.annotations")
@ComponentScan(basePackageClasses = VehicleFactoryConfig.class)
class VehicleFactoryConfig {
    
}
```

Alternatively, we can use @ComponentScans to specify multiple @ComponentScan configurations:
```java
@Configuration
@ComponentScans({
@ComponentScan(basePackages = "com.baeldung.annotations"),
@ComponentScan(basePackageClasses = VehicleFactoryConfig.class)
})
class VehicleFactoryConfig {
    
}
```

When using XML configuration, the configuring component scanning is just as easy:
```xml
<context:component-scan base-package="com.baeldung" />
```

---

## ✅ `@Component`
@Component is a class level annotation. During the component scan, Spring Framework automatically detects classes annotated with @Component:
```java
@Component
class CarUtility {
// ...
}
```
By default, the bean instances of this class have the same name as the class name with a lowercase initial. In addition, we can specify a different name using the optional value argument of this annotation.

Since @Repository, @Service, @Configuration, and @Controller are all meta-annotations of @Component, they share the same bean naming behavior. Spring also automatically picks them up during the component scanning process.

---

## ✅ `@Repository`
DAO or Repository classes usually represent the database access layer in an application, and should be annotated with @Repository:
```java
@Repository
class VehicleRepository {
// ...
}
```
One advantage of using this annotation is that it has automatic persistence exception translation enabled. When using a persistence framework, such as Hibernate, native exceptions thrown within classes annotated with @Repository will be automatically translated into subclasses of Spring’s DataAccessExeption.

To enable exception translation, we need to declare our own PersistenceExceptionTranslationPostProcessor bean:
```java
@Bean
public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
return new PersistenceExceptionTranslationPostProcessor();
}
```
Note that in most cases, Spring does the above step automatically.

Or via XML configuration:
```xml
<bean class=
"org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor"/>
```

---

## ✅ `@Service`
   The business logic of an application usually resides within the service layer, so we’ll use the @Service annotation to indicate that a class belongs to that layer:
```java
@Service
public class VehicleService {
// ...    
}
```

---

## ✅ `@Controller`
   @Controller is a class level annotation, which tells the Spring Framework that this class serves as a controller in Spring MVC:

```java
@Controller
public class VehicleController {
// ...
}
```

---

## ✅ `@Configuration`

👉 Defines configuration class

```java
@Configuration
public class AppConfig {
}
```

---

## ✅ `Stereotype Annotations and AOP`

When we use Spring stereotype annotations, it’s easy to create a pointcut that targets all classes that have a particular stereotype.

For instance, suppose we want to measure the execution time of methods from the DAO layer. We’ll create the following aspect (using AspectJ annotations), taking advantage of the @Repository stereotype:

```java
@Aspect
@Component
public class PerformanceAspect {
@Pointcut("within(@org.springframework.stereotype.Repository *)")
public void repositoryClassMethods() {};

    @Around("repositoryClassMethods()")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) 
      throws Throwable {
        long start = System.nanoTime();
        Object returnValue = joinPoint.proceed();
        long end = System.nanoTime();
        String methodName = joinPoint.getSignature().getName();
        System.out.println(
          "Execution of " + methodName + " took " + 
          TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        return returnValue;
    }
}
```
In this example, we created a pointcut that matches all the methods in classes annotated with @Repository. Then we used the @Around advice to target that pointcut, and determine the execution time of the intercepted methods calls.

Furthermore, using this approach, we can add logging, performance management, audit, and other behaviors to each application layer.


---

# 🔹 3. Spring Boot Annotations

---

## ✅ `@SpringBootApplication`

👉 Main entry point. We use this annotation to mark the main class of a Spring Boot application:

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

👉 Enables auto config. It means that Spring Boot looks for auto-configuration beans on its classpath and automatically applies them.

Note, that we have to use this annotation with @Configuration:

```java
@Configuration
@EnableAutoConfiguration
class VehicleFactoryConfig {}
```

## ✅ Auto-Configuration Conditions
Usually, when we write our custom auto-configurations, we want Spring to use them conditionally. We can achieve this with the annotations in this section.

We can place the annotations in this section on @Configuration classes or @Bean methods.

In the next sections, we’ll only introduce the basic concept behind each condition. For further information, please visit this article.

---

### ✅ `@ConditionalOnClass and @ConditionalOnMissingClass`

Using these conditions, Spring will only use the marked auto-configuration bean if the class in the annotation’s argument is present/absent:
```java
@Configuration
@ConditionalOnClass(DataSource.class)
class MySQLAutoconfiguration {
//...
}
```

---

### ✅ `@ConditionalOnBean and @ConditionalOnMissingBean`
We can use these annotations when we want to define conditions based on the presence or absence of a specific bean:
```java
@Bean
@ConditionalOnBean(name = "dataSource")
LocalContainerEntityManagerFactoryBean entityManagerFactory() {
// ...
}
```

---

### ✅ `@ConditionalOnProperty`
With this annotation, we can make conditions on the values of properties:
```java
@Bean
@ConditionalOnProperty(name = "usemysql",havingValue = "local")
DataSource dataSource() {
// ...
}
```

---

### ✅ `@ConditionalOnResource`
We can make Spring to use a definition only when a specific resource is present:
```java
@ConditionalOnResource(resources = "classpath:mysql.properties")
Properties additionalProperties() {
// ...
}
```

---

### ✅ `@ConditionalOnWebApplication and @ConditionalOnNotWebApplication`
With these annotations, we can create conditions based on if the current application is or isn’t a web application:
```java
@ConditionalOnWebApplication
HealthCheckController healthCheckController() {
// ...
}
```

---

### ✅ `@ConditionalExpression`
We can use this annotation in more complex situations. Spring will use the marked definition when the SpEL expression is evaluated to true:
```java
@Bean
@ConditionalOnExpression("${usemysql} && ${mysqlserver == 'local'}")
DataSource dataSource() {
// ...
}
```

---

### ✅ `@Conditional`
For even more complex conditions, we can create a class evaluating the custom condition. We tell Spring to use this custom condition with @Conditional:
```java
@Conditional(HibernateCondition.class)
Properties additionalProperties() {
//...
}
```

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

# 🔹 4. Web / REST Annotations

---

## ✅ `@Controller`
We can define a Spring MVC controller with @Controller. For more information, please visit [article about Spring Bean Annotations](https://www.baeldung.com/spring-bean-annotations).

## ✅ `@RestController`
The @RestController combines @Controller and @ResponseBody.

Therefore, the following declarations are equivalent:

```java
@Controller
@ResponseBody
class VehicleRestController {
// ...
}
```

```java
@RestController
class VehicleRestController {
// ...
}
```


## ✅ [`@RequestMapping`](https://www.baeldung.com/spring-requestmapping)

👉 Maps HTTP requests

```java
@RequestMapping("/api")
```

> Simply put, @RequestMapping marks request handler methods inside @Controller classes; it can be configured using:

* path, or its aliases, name, and value: which URL the method is mapped to
* method: compatible HTTP methods
* params: filters requests based on presence, absence, or value of HTTP parameters
* headers: filters requests based on presence, absence, or value of HTTP headers
* consumes: which media types the method can consume in the HTTP request body
* produces: which media types the method can produce in the HTTP response body
Here’s a quick example of what that looks like:

```java
@Controller
class VehicleController {

    @RequestMapping(value = "/vehicles/home", method = RequestMethod.GET)
    String home() {
        return "home";
    }
}
```

👉 We can provide default settings for all handler methods in a @Controller class if we apply this annotation on the class level. The only exception is the URL which Spring won’t override with method level settings but appends the two path parts.

For example, the following configuration has the same effect as the one above:
```java
@Controller
@RequestMapping(value = "/vehicles", method = RequestMethod.GET)
class VehicleController {

    @RequestMapping("/home")
    String home() {
        return "home";
    }
}
```
Moreover, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, and @PatchMapping are different variants of @RequestMapping with the HTTP method already set to GET, POST, PUT, DELETE, and PATCH respectively.

These are available since Spring 4.3 release.

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

👉 This annotation indicates that a method argument is bound to a URI template variable. We can specify the URI template with the @RequestMapping annotation and bind a method argument to one of the template parts with @PathVariable.

We can achieve this with the name or its alias, the value argument:
```java
@RequestMapping("/{id}")
Vehicle getVehicle(@PathVariable("id") long id) {
// ...
}
```
If the name of the part in the template matches the name of the method argument, we don’t have to specify it in the annotation:
```java
@RequestMapping("/{id}")
Vehicle getVehicle(@PathVariable long id) {
// ...
}
```
Moreover, we can mark a path variable optional by setting the argument required to false:
```java
@RequestMapping("/{id}")
Vehicle getVehicle(@PathVariable(required = false) long id) {
// ...
}
```
---

## ✅ `@RequestParam`

```java
@GetMapping("/search")
public String search(@RequestParam String name) {
}
```

We use @RequestParam for accessing HTTP request parameters:
```java
@RequestMapping
Vehicle getVehicleByParam(@RequestParam("id") long id) {
// ...
}
```
It has the same configuration options as the @PathVariable annotation.

In addition to those settings, with @RequestParam we can specify an injected value when Spring finds no or empty value in the request. To achieve this, we have to set the defaultValue argument.

Providing a default value implicitly sets required to false:
```java
@RequestMapping("/buy")
Car buyCar(@RequestParam(defaultValue = "5") int seatCount) {
// ...
}
```
Besides parameters, there are other HTTP request parts we can access: cookies and headers. We can access them with the annotations @CookieValue and @RequestHeader respectively.

We can configure them the same way as @RequestParam.


---

## ✅ [`@RequestBody`](https://www.baeldung.com/spring-request-response-body)

```java
@PostMapping
public void save(@RequestBody User user) {
}
```

Let’s move on to @RequestBody – which maps the body of the HTTP request to an object:
```java
@PostMapping("/save")
void saveVehicle(@RequestBody Vehicle vehicle) {
// ...
}
```
The deserialization is automatic and depends on the content type of the request.

---

## ✅ [`@ResponseBody`](https://www.baeldung.com/spring-request-response-body)

👉 Converts response to JSON

If we mark a request handler method with @ResponseBody, Spring treats the result of the method as the response itself:
```java
@ResponseBody
@RequestMapping("/hello")
String hello() {
return "Hello World!";
}
```
If we annotate a @Controller class with this annotation, all request handler methods will use it.


## ✅ `@ExceptionHandler`

👉 With this annotation, we can declare a custom error handler method. Spring calls this method when a request handler method throws any of the specified exceptions.

The caught exception can be passed to the method as an argument:
```java
@ExceptionHandler(IllegalArgumentException.class)
void onIllegalArgumentException(IllegalArgumentException exception) {
// ...
}
```


## ✅ `@ResponseStatus`

👉 We can specify the desired HTTP status of the response if we annotate a request handler method with this annotation. We can declare the status code with the code argument, or its alias, the value argument.

Also, we can provide a reason using the reason argument.

We also can use it along with @ExceptionHandler:
```java
@ExceptionHandler(IllegalArgumentException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
void onIllegalArgumentException(IllegalArgumentException exception) {
// ...
}
```
For more information about HTTP response status, please visit [this article](https://www.baeldung.com/spring-mvc-controller-custom-http-status-code).

## ✅ `@ModelAttribute`
With this annotation we can access elements that are already in the model of an MVC @Controller, by providing the model key:
```java
@PostMapping("/assemble")
void assembleVehicle(@ModelAttribute("vehicle") Vehicle vehicleInModel) {
// ...
}
```
Like with @PathVariable and @RequestParam, we don’t have to specify the model key if the argument has the same name:
```java
@PostMapping("/assemble")
void assembleVehicle(@ModelAttribute Vehicle vehicle) {
// ...
}
```
Besides, @ModelAttribute has another use: if we annotate a method with it, Spring will automatically add the method’s return value to the model:
```java
@ModelAttribute("vehicle")
Vehicle getVehicle() {
// ...
}
```
Like before, we don’t have to specify the model key, Spring uses the method’s name by default:
```java
@ModelAttribute
Vehicle vehicle() {
// ...
}
```
Before Spring calls a request handler method, it invokes all @ModelAttribute annotated methods in the class.

More information about @ModelAttribute can be found in [this article](https://www.baeldung.com/spring-mvc-and-the-modelattribute-annotation).

## ✅ `@CrossOrigin`
@CrossOrigin enables cross-domain communication for the annotated request handler methods:
```java
@CrossOrigin
@RequestMapping("/hello")
String hello() {
return "Hello World!";
}
```
If we mark a class with it, it applies to all request handler methods in it.

We can fine-tune CORS behavior with this annotation’s arguments.

For more details, please visit [this article](https://www.baeldung.com/spring-cors).

---

# 🔹 5. JPA / Hibernate Annotations

---

# 1. Entity & Table Mapping

---

## ✅ `@Entity`

👉 Marks a class as a JPA entity, meaning it will be mapped to a database table.
👉 Each instance of the class represents a row in the table.
👉 Mandatory for persistence.

```java
@Entity
public class User {
}
```

---

## ✅ `@Table`

👉 Used to specify table details like name, schema, or constraints.
👉 Optional—if not provided, table name = class name.
👉 Helps when DB table name differs from class name.

```java
@Entity
@Table(name = "users")
public class User {
}
```

---

## ✅ `@Id`

👉 Defines the primary key of the entity.
👉 Each entity must have exactly one identifier.
👉 Used by JPA to uniquely identify records.

```java
@Id
private Long id;
```

---

## ✅ `@GeneratedValue`

👉 Specifies how the primary key value is generated automatically.
👉 Common strategies: `IDENTITY`, `SEQUENCE`, `AUTO`.
👉 Removes need for manual ID assignment.

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

---

## ✅ `@Column`

👉 Maps a field to a specific database column.
👉 Allows defining constraints like `nullable`, `unique`, `length`.
👉 Useful for customizing DB schema.

```java
@Column(name = "user_name", nullable = false, length = 50)
private String name;
```

---

## ✅ `@Transient`

👉 Marks a field that should NOT be persisted in the database.
👉 Used for temporary or calculated values.
👉 Ignored by JPA during insert/update.

```java
@Transient
private String tempData;
```

---

## ✅ `@Enumerated`

👉 Used for mapping enum types to database columns.
👉 Can store as `STRING` (recommended) or `ORDINAL`.
👉 Prevents ambiguity in enum values.

```java
@Enumerated(EnumType.STRING)
private Status status;
```

---

## ✅ `@Lob`

👉 Used for storing large data like images or long text.
👉 Maps to BLOB (binary) or CLOB (character) in DB.
👉 Useful for file storage.

```java
@Lob
private byte[] image;
```

---

# 2. Relationship Mapping

---

## ✅ `@OneToOne`

👉 Defines a one-to-one relationship between two entities.
👉 Each record in one table maps to exactly one record in another.
👉 Often used with `@JoinColumn`.

```java
@OneToOne
@JoinColumn(name = "profile_id")
private Profile profile;
```

---

## ✅ `@OneToMany`

👉 Represents one-to-many relationship (one parent, many children).
👉 Usually mapped with `mappedBy` on the parent side.
👉 Supports cascading operations.

```java
@OneToMany(mappedBy = "user")
private List<Order> orders;
```

---

## ✅ `@ManyToOne`

👉 Many entities can be associated with one entity.
👉 Common in relationships like Order → User.
👉 Creates foreign key in DB.

```java
@ManyToOne
@JoinColumn(name = "user_id")
private User user;
```

---

## ✅ `@ManyToMany`

👉 Defines many-to-many relationships between entities.
👉 Uses a join table internally.
👉 Both sides can own the relationship.

```java
@ManyToMany
@JoinTable(name = "user_roles")
private List<Role> roles;
```

---

## ✅ `@JoinColumn`

👉 Specifies the foreign key column in relationships.
👉 Defines how tables are linked.
👉 Used in OneToOne, ManyToOne mappings.

```java
@JoinColumn(name = "user_id")
```

---

## ✅ `@JoinTable`

👉 Used in many-to-many relationships to define join table.
👉 Specifies join columns and inverse join columns.
👉 Controls mapping table structure.

```java
@JoinTable(name = "user_roles")
```

---

# 3. Fetch & Cascade

---

## ✅ `FetchType.LAZY`

👉 Data is loaded only when accessed.
👉 Improves performance by delaying DB calls.
👉 Default for collections.

```java
@OneToMany(fetch = FetchType.LAZY)
```

---

## ✅ `FetchType.EAGER`

👉 Data is loaded immediately with parent entity.
👉 Can impact performance if overused.
👉 Default for many-to-one.

```java
@ManyToOne(fetch = FetchType.EAGER)
```

---

## ✅ `CascadeType.ALL`

👉 Applies all operations (persist, merge, remove, etc.) to child entities.
👉 Helps maintain consistency between parent and child.
👉 Should be used carefully.

```java
@OneToMany(cascade = CascadeType.ALL)
```

---

# 4. Inheritance

---

## ✅ `@Inheritance`

👉 Defines inheritance mapping strategy in DB.
👉 Strategies: SINGLE_TABLE, JOINED, TABLE_PER_CLASS.
👉 Allows OOP inheritance in DB tables.

```java
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
```

---

## ✅ `@DiscriminatorColumn`

👉 Adds column to distinguish subclass types.
👉 Used with SINGLE_TABLE strategy.
👉 Helps identify entity type.

```java
@DiscriminatorColumn(name = "type")
```

---

# 5. Query Annotations

---

## ✅ `@NamedQuery`

👉 Defines static JPQL queries at entity level.
👉 Improves readability and reuse.
👉 Loaded at startup.

```java
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
```

---

## ✅ `@Query`

👉 Used in Spring Data JPA repositories.
👉 Allows custom JPQL or native SQL queries.
👉 Supports parameters and dynamic queries.

```java
@Query("SELECT u FROM User u WHERE u.name = :name")
```

---

# 6. Lifecycle Callbacks

---

## ✅ `@PrePersist`

👉 Called before entity is saved in DB.
👉 Used for setting default values or validation.
👉 Executes automatically.

```java
@PrePersist
public void beforeSave() {
}
```

---

## ✅ `@PostPersist`

👉 Called after entity is saved.
👉 Useful for logging or triggering actions.

```java
@PostPersist
```

---

## ✅ `@PreUpdate`

👉 Called before update operation.
👉 Used for modifying values before saving.

```java
@PreUpdate
```

---

## ✅ `@PostUpdate`

👉 Called after update completes.
👉 Useful for auditing or logging.

```java
@PostUpdate
```

---

# 7. Hibernate-Specific

---

## ✅ `@CreationTimestamp`

👉 Automatically sets timestamp when entity is created.
👉 No manual coding required.
👉 Useful for audit fields.

```java
@CreationTimestamp
private LocalDateTime createdAt;
```

---

## ✅ `@UpdateTimestamp`

👉 Updates timestamp automatically on every update.
👉 Keeps track of modification time.

```java
@UpdateTimestamp
private LocalDateTime updatedAt;
```

---

## ✅ `@Formula`

👉 Used to calculate derived values using SQL expression.
👉 Not stored in DB but computed at runtime.

```java
@Formula("price * quantity")
private double total;
```

---

## ✅ `@Where`

👉 Adds condition to all queries for the entity.
👉 Useful for soft delete or filtering.

```java
@Where(clause = "active = true")
```

---

# 8. Advanced

---

## ✅ `@Embedded`

👉 Embeds another object inside entity.
👉 No separate table created.

```java
@Embedded
private Address address;
```

---

## ✅ `@Embeddable`

👉 Marks class that can be embedded.
👉 Used with `@Embedded`.

```java
@Embeddable
public class Address {}
```

---

## ✅ `@Version`

👉 Used for optimistic locking.
👉 Prevents concurrent update conflicts.

```java
@Version
private int version;
```

---

# 🎯 Interview One-Liner

👉

> JPA and Hibernate annotations map Java objects to database tables, define relationships, control fetching strategies, and manage lifecycle and persistence behavior.

---

## ✅ `@Transactional`

```java
@Transactional
public void saveUser() {
}
```

When we want to configure the transactional behavior of a method, we can do it with:
```java
@Transactional
void pay() {}
```
If we apply this annotation on class level, then it works on all methods inside the class. However, we can override its effects by applying it to a specific method.

It has many configuration options, which can be found in https://www.baeldung.com/transaction-configuration-with-jpa-and-spring.

---

# 🔹 6. Exception Handling

---

# 1. `@ExceptionHandler`

---

## ✅ What it does

👉 Handles specific exceptions inside a controller.
👉 Works at **method level**.
👉 Allows custom response for specific exception types.

---

## ✅ Example

```java
@RestController
public class UserController {

    @GetMapping("/test")
    public String test() {
        throw new RuntimeException("Error occurred");
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex) {
        return "Handled: " + ex.getMessage();
    }
}
```

---

## 🎯 Key Points

* Works only in **same controller**
* Can handle multiple exceptions
* Returns custom response

---

# 2. `@ControllerAdvice`

---

## ✅ What it does

👉 Provides **global exception handling** across all controllers.
👉 Acts like a **centralized exception handler**.

---

## ✅ Example

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        return "Global Error: " + ex.getMessage();
    }
}
```

---

## 🎯 Key Points

* Applies to **all controllers**
* Reduces duplicate code
* Used for **centralized handling**

---

# 3. `@RestControllerAdvice`

---

## ✅ What it does

👉 Combination of:

```java
@ControllerAdvice + @ResponseBody
```

👉 Returns **JSON response directly** (used in REST APIs)

---

## ✅ Example

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handle(RuntimeException ex) {
        return Map.of("error", ex.getMessage());
    }
}
```

---

## 🎯 Key Points

* Used in REST APIs
* Automatically converts response to JSON

---

# 4. `@ResponseStatus`

---

## ✅ What it does

👉 Defines HTTP status for an exception or method.
👉 Can be used on exception class or handler method.

---

## ✅ Example (Custom Exception)

```java
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
}
```

---

## ✅ Example (Handler)

```java
@ExceptionHandler(UserNotFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public String handle() {
    return "User not found";
}
```

---

## 🎯 Key Points

* Maps exception → HTTP status
* Improves API response clarity

---

# 5. `@ResponseBody`

---

## ✅ What it does

👉 Converts return value into JSON/XML response.
👉 Used with `@ControllerAdvice`.

---

## ✅ Example

```java
@ExceptionHandler(Exception.class)
@ResponseBody
public String handle() {
    return "Error";
}
```

---

## 🎯 Note

👉 Not needed with `@RestControllerAdvice`

---

# 6. `@Order`

---

## ✅ What it does

👉 Defines priority when multiple exception handlers exist.
👉 Lower value = higher priority.

---

## ✅ Example

```java
@ControllerAdvice
@Order(1)
public class HighPriorityHandler {
}
```

---

# 7. `@RestControllerAdvice(assignableTypes / basePackages)`

---

## ✅ What it does

👉 Limits exception handling to specific controllers/packages.

---

## ✅ Example

```java
@RestControllerAdvice(basePackages = "com.app.controller")
public class ScopedHandler {
}
```

---

# 8. `@ExceptionHandler Multiple Exceptions`

---

## ✅ Example

```java
@ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
public String handleMultiple(Exception ex) {
    return "Handled multiple exceptions";
}
```

---

# 🔥 Real-World Example (Best Practice)

---

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Something went wrong"));
    }
}
```

---

# 🔥 Flow of Exception Handling

```text
Controller → Exception → @ExceptionHandler → Response
```

---

# 🎯 Interview Questions (Very Important)

---

## ❓ Difference: `@ControllerAdvice` vs `@RestControllerAdvice`

> `@RestControllerAdvice` returns JSON directly, while `@ControllerAdvice` needs `@ResponseBody`.

---

## ❓ Can we handle exceptions globally?

👉 Yes, using `@ControllerAdvice`

---

## ❓ What if multiple handlers exist?

👉 Controlled using `@Order`

---

# 🔥 Common Mistakes

❌ Using `@ExceptionHandler` in every controller
❌ Not returning proper HTTP status
❌ Catching generic Exception everywhere

---

# 🎯 Interview One-Liner

> Spring provides annotations like `@ExceptionHandler` and `@ControllerAdvice` to handle exceptions locally and globally in a clean and centralized way.


---

# 🔹 7. Testing Annotations

---

# What is Testing in Spring Boot?

👉 Testing ensures your application works correctly.

👉 Spring Boot provides **built-in support for unit, integration, and web testing**.

---

# 1. `@SpringBootTest`

## ✅ What it does

👉 Loads the **full application context** (like running the app).

👉 Used for **integration testing**.

---

## ✅ Example

```java
@SpringBootTest
class AppTest {

    @Test
    void contextLoads() {
    }
}
```

---

## 🎯 Key Points

* Loads all beans
* Slow but powerful
* Used to test full application flow

---

# 2. `@WebMvcTest`

## ✅ What it does

👉 Loads only **web layer (Controller)**

👉 Does NOT load service or repository

---

## ✅ Example

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
}
```

---

## 🎯 Key Points

* Fast testing
* Used for controller testing
* Requires mocking service layer

---

# 3. `@DataJpaTest`

## ✅ What it does

👉 Tests only **JPA repositories**

👉 Uses in-memory DB (like H2)

---

## ✅ Example

```java
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repo;
}
```

---

## 🎯 Key Points

* Loads only persistence layer
* Faster than full context
* Rolls back after test

---

# 4. `@MockBean`

## ✅ What it does

👉 Creates a **mock object** and adds it to Spring context

👉 Replaces real bean

---

## ✅ Example

```java
@MockBean
private UserService service;
```

---

## 🎯 Key Points

* Used in integration tests
* Helps isolate components
* Works with `@SpringBootTest`

---

# 5. `@Autowired` (in tests)

## ✅ What it does

👉 Injects real beans into test class

```java
@Autowired
private UserService service;
```

---

## 🎯 Key Points

* Used to test actual logic
* Works with Spring context

---

# 6. `@ExtendWith`

## ✅ What it does

👉 Integrates JUnit 5 with Spring

```java
@ExtendWith(SpringExtension.class)
```

---

## 🎯 Note

👉 Not needed if using `@SpringBootTest`

---

# 7. `@Test`

## ✅ What it does

👉 Marks a method as a test case (JUnit)

```java
@Test
void testMethod() {
}
```

---

# 8. `@BeforeEach` / `@AfterEach`

## ✅ What they do

👉 Run before/after each test

```java
@BeforeEach
void setup() {}

@AfterEach
void cleanup() {}
```

---

# 9. `@DisplayName`

## ✅ What it does

👉 Gives readable name to test

```java
@DisplayName("Test user creation")
```

---

# 10. `@Transactional` (in tests)

## ✅ What it does

👉 Rolls back DB changes after test

```java
@Transactional
```

---

# 🔥 Real Example (Controller Test)

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get("/user"))
               .andExpect(status().isOk());
    }
}
```

---

# 🔥 Types of Testing in Spring Boot

| Type             | Annotation        |
|------------------|-------------------|
| Unit Test        | `@Test`           |
| Integration Test | `@SpringBootTest` |
| Controller Test  | `@WebMvcTest`     |
| Repository Test  | `@DataJpaTest`    |

---

# 🎯 Interview Questions

---

## ❓ Difference: `@SpringBootTest` vs `@WebMvcTest`

> `@SpringBootTest` loads full context, while `@WebMvcTest` loads only web layer.

---

## ❓ Why use `@MockBean`?

> To replace real dependencies with mock objects for isolated testing.

---

## ❓ Why tests are fast with `@DataJpaTest`?

> Because only persistence layer is loaded with in-memory DB.

---

# 🔥 Common Mistakes

❌ Using `@SpringBootTest` everywhere (slow)
❌ Not mocking dependencies in controller tests
❌ Testing multiple layers in unit test

---

# 🎯 Interview One-Liner

> Spring Boot provides specialized testing annotations like `@SpringBootTest`, `@WebMvcTest`, and `@DataJpaTest` to test different layers efficiently.


---

# 🔹 8. AOP Annotations

---

# What is AOP?

👉 **Aspect-Oriented Programming (AOP)** separates cross-cutting concerns like:

* Logging
* Security
* Transactions

👉 Instead of repeating code, we apply logic using **aspects**

---

# 1. `@Aspect`

👉 Marks a class as an **Aspect (cross-cutting logic)**

👉 Required to define advice methods

```java
@Aspect
@Component
public class LoggingAspect {
}
```

---

# 2. `@EnableAspectJAutoProxy`

👉 Enables AOP support in Spring

👉 Creates proxy objects for beans

```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
}
```

👉 In Spring Boot → usually auto-enabled ✅

---

# 3. `@Before`

👉 Runs **before** the method execution

```java
@Before("execution(* com.app.service.*.*(..))")
public void beforeAdvice() {
    System.out.println("Before method execution");
}
```

---

# 4. `@After`

👉 Runs **after method execution (always)**

👉 Executes even if exception occurs

```java
@After("execution(* com.app.service.*.*(..))")
public void afterAdvice() {
    System.out.println("After method execution");
}
```

---

# 5. `@AfterReturning`

👉 Runs only when method completes **successfully**

👉 Can access return value

```java
@AfterReturning(pointcut = "execution(* com.app.service.*.*(..))", returning = "result")
public void afterReturningAdvice(Object result) {
    System.out.println("Returned: " + result);
}
```

---

# 6. `@AfterThrowing`

👉 Runs when method throws an exception

👉 Used for error logging

```java
@AfterThrowing(pointcut = "execution(* com.app.service.*.*(..))", throwing = "ex")
public void afterThrowingAdvice(Exception ex) {
    System.out.println("Exception: " + ex.getMessage());
}
```

---

# 7. `@Around` (MOST IMPORTANT 🔥)

👉 Runs **before and after method execution**

👉 Gives full control over method execution

```java
@Around("execution(* com.app.service.*.*(..))")
public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
    System.out.println("Before");
    Object result = pjp.proceed(); // call actual method
    System.out.println("After");
    return result;
}
```

---

## 🎯 Why important?

👉 Can:

* Modify input
* Skip method execution
* Change return value

---

# 8. `@Pointcut`

👉 Defines reusable expression for selecting methods

```java
@Pointcut("execution(* com.app.service.*.*(..))")
public void serviceMethods() {}
```

---

## Usage:

```java
@Before("serviceMethods()")
```

---

# 9. `@Order`

👉 Defines priority when multiple aspects exist

👉 Lower value = higher priority

```java
@Aspect
@Order(1)
public class FirstAspect {}
```

---

# 10. `JoinPoint` / `ProceedingJoinPoint`

👉 Provides runtime information about method execution

👉 Used inside advice

```java
@Before("execution(* com.app.service.*.*(..))")
public void log(JoinPoint jp) {
    System.out.println(jp.getSignature().getName());
}
```

---

# 🔥 Full Working Example

---

```java
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.app.service.*.*(..))")
    public void serviceLayer() {}

    @Before("serviceLayer()")
    public void before() {
        System.out.println("Before execution");
    }

    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void afterReturning(Object result) {
        System.out.println("Result: " + result);
    }

    @AfterThrowing(pointcut = "serviceLayer()", throwing = "ex")
    public void afterThrowing(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }

    @Around("serviceLayer()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Around before");
        Object result = pjp.proceed();
        System.out.println("Around after");
        return result;
    }
}
```

---

# 🔥 Execution Flow

```text
@Around (before)
   ↓
@Before
   ↓
Method Execution
   ↓
@AfterReturning / @AfterThrowing
   ↓
@After
   ↓
@Around (after)
```

---

# 🎯 Interview Questions

---

## ❓ Difference: `@Before` vs `@Around`

> `@Before` runs before method, while `@Around` can control full execution.

---

## ❓ Which is most powerful?

> `@Around`

---

## ❓ What is a pointcut?

> Expression that defines where advice should be applied.

---

# 🔥 Common Mistakes

❌ Forgetting `@Component` on aspect
❌ Wrong pointcut expression
❌ Overusing `@Around`

---

# 🎯 Interview One-Liner

> Spring AOP annotations allow applying cross-cutting concerns like logging and security using aspects, advices, and pointcuts without modifying business logic.


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
