### 1. Revision History

| Date         | Updates by | Details                                                                              |
|:-------------| :--- |:-------------------------------------------------------------------------------------|
| Sep 15, 2025 | Emilio | Initial Draft, <br/>Resarch on posible weather providers, tests and response analis. |
| Sep 15, 2025 | Emilio | Proof of concept. Initial design. TDD.                                               |
| Sep 16, 2025 | Emilio | Model refinement. Border cases. QA. Swagger                                          |
| Sep 17, 2025 | Emilio | External Api refinement to be configured through parameters.                         |
| TBD          | TBD | TBD                                                                                  |

---

### 2. Basic Information

| Item                                | Type | IN/OUT | Required | Details                                                                             |
|:------------------------------------| :--- |:-------| :--- |:------------------------------------------------------------------------------------|
| **Service Name**                    | RESTful API |        | Yes | WeatherPan                                                                          |
| **Weather Endpoint**                | GET | IN     | Yes | `/api/v1/weatherpan/{city}`                                                         |
| **Weather Endpoint Argument: city** | String | IN     | Yes | A lowercase string representing a valid city code (e.g., `tokyo`, `osaka`).         |
| **Weather Endpoint Response**       | JSON Object | OUT    | Yes | Contains weather information: city, temperature, humidity, and weather description. |
| **Info Endpoint**                   | GET | IN     | No | `/api/v1/weatherpan/info`                                                           |
| **Info Endpoint Response**          | JSON Object | OUT    | Yes | Provides service status, a list of valid city codes, and related URLs.              |
| **External API**                    | GET | IN     | Yes | `api.open-meteo.com` provides support to produce weather information.               |

---

### 3. Process Flow

1.  **Request Reception**: The `WeatherController` receives a `GET` request at the `/api/v1/weatherpan/{city}` endpoint.
2.  **Input Validation**: The `city` path variable is converted to lowercase to ensure consistency.
3.  **Service Call**: The controller delegates the request to the `WeatherService`'s `checkCity` method, passing the lowercase city code.
4.  **Configuration Check**: The `WeatherService` attempts to find the corresponding `CityLocation` object from its pre-configured map of locations.
5.  **Location Found**:
    * If the city code is found, the service calls the external `WeatherApi` with the city's latitude and longitude.
    * The `WeatherApi` sends an HTTP `GET` request to the Open-Meteo API using the pre-formatted URL.
    * The response from the external API is deserialized into a `WeatherForecastDTO`.
    * The `WeatherService` then transforms this DTO into a user-friendly `WeatherResultDTO`, using the configured weather codes to provide a human-readable description.
    * The `WeatherResultDTO` is returned to the controller.
6.  **Location Not Found**:
    * If the city code is not in the configuration map, the `WeatherService` returns a `WeatherResultDTO` with `NOT_AVAILABLE` status for all weather fields.
7.  **Response Handling**: The `WeatherController` wraps the received `WeatherResultDTO` in a `ResponseEntity` with a `200 OK` status and sends it back to the client.
8.  **Error Handling**: If any part of the process (e.g., external API call, deserialization) throws an exception, the `ExceptionHandler` in the `WeatherController` catches it, logs the error, and returns a `500 Internal Server Error` with the message "Service unavailable."

---

### 4. Processing Details

* **API Integration**: The `WeatherApi` component uses Spring's `RestClient` to connect to the external Open-Meteo API. The base URL and parameters are managed through an externalized property (`weather.api.url`).
* **Data Mapping**:
    * The `WeatherConfig` class is responsible for loading city locations and weather code mappings from an `application.yml` file.
    * It uses `@ConfigurationProperties` to automatically bind the properties to its fields.
    * The `setLocations` method performs a custom mapping, converting a list of `CityLocation` objects into a `Map<String, CityLocation>`, using the city's code as the key for quick lookups.
* **Response Generation**:
    * The `WeatherResultDTO` acts as a data transfer object for the final response, ensuring a consistent and clean output for the client.
    * It includes a static factory method (`from`) to facilitate the conversion from raw API data (`WeatherForecastDTO`) to a formatted result, including unit-based strings (e.g., "°C", "%").
* **Robustness**:
    * The application gracefully handles requests for unsupported cities by returning a "not available" status.
    * A global `ExceptionHandler` in the controller ensures that any unexpected exceptions during the request-response cycle are caught and a generic `500` error is returned to prevent sensitive information from being exposed to the client.

---

### 5. Internal Methods

* `WeatherService.checkCity(String cityCode)`:
    * **Input**: `cityCode` (String) - The code of the city to look up.
    * **Function**: Retrieves city location, calls `WeatherApi`, and converts the result to a `WeatherResultDTO`. Returns a `WeatherResultDTO` with a "not available" status if the city is not found.
    * **Output**: `WeatherResultDTO`
* `WeatherService.info()`:
    * **Input**: None.
    * **Function**: Generates and populates a map with service metadata, including status and valid city codes.
    * **Output**: `Map<String, String>`
* `WeatherApi.check(Double lat, Double lon)`:
    * **Input**: `lat` (Double), `lon` (Double) - Latitude and longitude of the location.
    * **Function**: Constructs and sends a `GET` request to the external weather API and deserializes the response.
    * **Output**: `WeatherForecastDTO`
* `WeatherConfig.setLocations(List<CityLocation> props)`:
    * **Input**: `props` (`List<CityLocation>`) - A list of city locations populated by Spring's `@ConfigurationProperties`.
    * **Function**: Converts the input list into a `Map<String, CityLocation>` using the city's code as the key.
    * **Output**: Void (updates an internal `locations` map).