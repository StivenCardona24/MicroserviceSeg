package com.uniquindio.api_rest.steps;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.javafaker.Faker;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LogStepDefinitions {

    private Response response;
    private String userId;

    private Map<String, Object> requestBody;

    private final Faker faker = new Faker();


    // Escenario: Crear un nuevo usuario exitosamente
    @Given("datos cliente nombre, apellido, cédula, email, contraseña, teléfono, fecha de nacimiento y dirección")
    public void givenClienteConPayloadValido() {
        requestBody = new HashMap<>();
        requestBody.put("name", faker.name().firstName());
        requestBody.put("lastname", faker.name().lastName());
        requestBody.put("cedula", faker.number().digits(10));
        requestBody.put("email", faker.internet().emailAddress());
        requestBody.put("password", faker.internet().password(8, 16));
        requestBody.put("phone", "+1" + faker.number().digits(9));
        requestBody.put("birthdate", "1990-01-01T00:00:00.000Z");
        requestBody.put("address", "Calle " + faker.address().streetName() + " " + faker.address().buildingNumber());
        System.out.println(requestBody);
    }

    @When("registro un nuevo usuario con datos válidos a {string}")
    public void registroNuevoUsuario(String url) {
        response = RestAssured
                .given()
                .contentType("application/json")
                .body(requestBody)
                .post(url);

        // Asume que "respuesta" contiene el ID del usuario registrado
        String userIdResponse = response.jsonPath().getString("respuesta");
        // Utilizamos una expresión regular para extraer solo el número del userId
        userId = userIdResponse.replaceAll("[^0-9]", "");
        System.out.println("User ID extraído: " + userId);
    }

    @Then("el log debe estar registrado en el servicio de logs")
    public void verificarLogEnServicioDeLogs() {
        // Convertir el userId en la descripción esperada

        String expectedDescription = "El usuario con ID " + userId + " fue registrado correctamente";

        System.out.println(expectedDescription);

        // Obtener la marca de tiempo actual antes de realizar la operación
        long currentTime = System.currentTimeMillis() - 10000;

        // Esperar hasta 10 segundos para que el log sea registrado
        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            // Realizar la petición GET al microservicio de logs
            Response logResponse = RestAssured
                    .given()
                    .queryParam("application", "UserService")
                    .queryParam("logType", "INFO")
                    .queryParam("startDate", currentTime) // Filtrar por logs recientes
                    .get("http://localhost:3000/api/logs");

            // Verificar que la respuesta tenga un código de estado 200
            assertEquals(200, logResponse.statusCode());

            // Asegurarse de que al menos un log haya sido registrado
            List<String> descriptions = logResponse.jsonPath().getList("logs.description");
            System.out.println(descriptions);
            assertNotNull(descriptions);
            assertFalse(descriptions.isEmpty());

            // Verificar que la descripción del log sea la esperada
            boolean logFound = descriptions.stream().anyMatch(description -> description.equals(expectedDescription));
            assertTrue(logFound, "El log correspondiente no se encontró en el servicio de logs.");
        });
    }
}

