## WeatherPan

WeatherPan API offers you a chance to check you rcurrent weather a your city
Yes, I can provide the previous response in English.

---

### System Requirements

* **Java 21** or higher
* **Gradle 8.x** or higher

-----

###  Getting Started

#### 1. Clone the Repository

```bash
git clone https://github.com/your-user/WeatherPan.git
cd WeatherPan
```

#### 2. Compile the Project

```bash
# Compiles the entire project, including dependencies
./gradlew build
```

#### 3. Start the Service

You can run the service in two ways:

**a) With Gradle:**

```bash
./gradlew bootRun
```

**b) With the executable JAR:**

After compiling the project with `./gradlew build`, you will find the JAR file in the `build/libs` folder.

```bash
java -jar build/libs/WeatherPan-0.0.1-SNAPSHOT.jar
```

-----

### Running Tests

To run the entire test suite, use the following command:

```bash
./gradlew test
```

-----

### API Endpoints

This section lists the most important endpoints for interacting with the service.

| **Method** | **Endpoint**              | **Description**                                                     |
|:---|:--------------------------|:--------------------------------------------------------------------|
| `GET` | `/api/v1/weatherpan/info` | Retrieves a json detailing endpoints and supported cities.          |
| `GET` | `/api/v1/weatherpan/{city}`          | Retrieves the present weather of city `city`. |

For a complete reference and the structure of requests/responses (payloads), refer to the **API documentation** generated with tools like OpenAPI or Swagger.

-----

### Online Documentation

You can explore the application through
`/swagger-ui/index.htmly`

-----

### Configuration

The service uses the `src/main/resources/application.yml` file for its configuration.  
Common parameters include:

* **Server Port:** `server.port=8080`
* **Logging Configuration:** `logging.level.root=INFO`

#### Suported Cities

Suported cities are Kyoto, Osaka and Tokyo.  
You can add a new city by adding properties like:

```properties
  locations:
    - code: newyork
      name: "New York"
      latitude: 40.7128
      longitude: 74.0060
```

-----

### Technologies Used

* **Framework:** Spring Boot 3
* **Language:** Java 21
* **Dependency Manager:** Gradle
* **Testing:** JUnit 5, Mockito
* **Documentation:** OpenAPI (Swagger)