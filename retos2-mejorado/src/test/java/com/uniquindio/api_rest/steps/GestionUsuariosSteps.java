package com.uniquindio.api_rest.steps;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.javafaker.Faker;

import static io.restassured.RestAssured.given;



public class GestionUsuariosSteps {

    private Response response;
    private Map<String, Object> requestBody;
    private final TestContext testContext = new TestContext();
    private final Faker faker = new Faker();


    // Escenario: Crear un nuevo usuario exitosamente
    @Given("que el cliente tiene un payload válido con nombre, apellido, cédula, email, contraseña, teléfono, fecha de nacimiento y dirección")
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

    @When("el cliente envía una solicitud POST a {string}")
    public void whenEnvioSolicitudPOST(String url) {
        response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(url)
                .then()
                .extract()
                .response();
    }

    @Then("el sistema debe devolver un código de estado {int}")
    public void thenCodigoDeEstado(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Then("la respuesta debe contener el mensaje de creación {string}")
    public void thenRespuestaContieneMensajeCrear(String mensajeEsperado) {

        String mensajeReal = response.jsonPath().getString("respuesta");
        // Creamos una expresión regular para capturar el userId del mensaje real
        Pattern pattern = Pattern.compile("Usuario (\\d+) registrado correctamente");
        Matcher matcher = pattern.matcher(mensajeReal);

        if (matcher.find()) {
            String userId = matcher.group(1);
            // Reemplazamos {userId} en el mensaje esperado con el userId capturado
            String mensajeConUserId = mensajeEsperado.replace("{userId}", userId);
            Assert.assertEquals(mensajeConUserId, mensajeReal);
        } else {
            Assert.fail("El formato del mensaje no es el esperado: " + mensajeReal);
        }
    }

    // Escenario: Login exitoso
    @Before
    @Given("que el cliente tiene credenciales válidas con un email y contraseña")
    public void givenClienteConCredencialesValidas() {
        requestBody = new HashMap<>();
        requestBody.put("email", "juancarlos05@example.com");
        requestBody.put("passwd", "password123");
    }

    @When("el cliente envía una solicitud POST a {string} para login")
    public void whenEnvioSolicitudPOSTParaLogin(String url) {
        response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(url)
                .then()
                .extract()
                .response();
        String jwtToken = response.jsonPath().getString("respuesta.token");
        this.testContext.setJwtToken(jwtToken);


    }

    @Then("la respuesta debe contener un token JWT válido")
    public void thenDevolverTokenJWT() {
        String jwtToken = response.jsonPath().getString("respuesta.token"); // guardar el token JWT
        this.testContext.setJwtToken(jwtToken);
        System.out.println(this.testContext.getJwtToken());
        System.out.println(jwtToken);
        Assert.assertNotNull("Token JWT es nulo", jwtToken);
    }

    // Escenario: Obtener detalles de usuario exitosamente

    @Given("que el cliente tiene un token JWT válido y un ID de usuario existente")
    public void givenJWTValidoConID() {

    }
    @When("el cliente envia una solicitud GET a {string} para obtener los detalles del usuario con ID {int}")
    public void whenEnvioSolicitudGETParaObtenerDetalles(String url, int id) {
        String jwtToken = realizarLoginYObtenerToken("juancarlos05@example.com", "password123" );

        response = given()
                .header("Authorization", "Bearer " + jwtToken) // Usa el token JWT aquí
                .when()
                .get(url + "/" + id)
                .then()
                .extract()
                .response();
    }


    @Then("la respuesta debe contener los detalles del usuario con nombre {string} y email {string}")
    public void thenRespuestaContieneDetalles(String nombre, String email) {
        String nombreReal = response.jsonPath().getString("respuesta.name");
        String emailReal = response.jsonPath().getString("respuesta.email");

        Assert.assertEquals(nombre, nombreReal);
        Assert.assertEquals(email, emailReal);
    }

    // Escenario: Editar un usuario exitosamente

    @Given("que el cliente tiene un token JWT válido y un payload con datos actualizados para un usuario existente")
    public void givenJWTValidoConIDEdit() {
        //Payload con los datos del usuario que deseas actualizar
        requestBody = new HashMap<>();
        requestBody.put("id", 1);
        requestBody.put("name", "Usuario Actualizado");
        requestBody.put("lastname", "Doe");
        requestBody.put("cedula", "1113313722");
        requestBody.put("email", "juancarlos2000@example.com");
        requestBody.put("phone", "+7376543995");
        requestBody.put("birthdate", "1990-01-01T00:00:00.000Z");
        requestBody.put("address", "Calle Falsa 123");

    }
    @When("el cliente envía una solicitud PUT a {string}")
    public void whenEnvioSolicitudEDIT(String url) {
        String jwtToken = realizarLoginYObtenerToken("juancarlos05@example.com", "password123" );
        response = given()
                .header("Authorization", "Bearer " + jwtToken) // Usa el token JWT aquí
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put(url)
                .then()
                .extract()
                .response();
    }

    @Then("el metodo de edicion debe devolver un código de estado {int}")
    public void thenEditarUsusario(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }
    @Then("la respuesta debe contener el mensaje de edición {string}")
    public void thenEditarUsuario(String mensajeEsperado) {
        String mensajeRespuesta = response.jsonPath().getString("respuesta");
        Assert.assertEquals(mensajeEsperado, mensajeRespuesta);
    }

    // Escenario: Eliminar un usuario exitosamente

    @Given("que el cliente tiene un token JWT válido y un ID de usuario existente para eliminar")
    public void givenJWTValidoConIDDelete() {
    }

    @When("el cliente envía una solicitud DELETE a {string} para eliminar el usuario con ID {int}")
    public void whenEnvioSolicitudDELETE(String url, int id) {
        String jwtToken = realizarLoginYObtenerToken("juancarlos05@example.com", "password123" );
        response = given()
                .header("Authorization", "Bearer " + jwtToken) // Usa el token JWT aquí
                .when()
                .delete(url + "/" + id)
                .then()
                .extract()
                .response();
    }


    @Then("el metodo de eliminar debe devolver un código de estado {int}")
    public void thenEliminarUsuario(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    // Escenario: Listar todos los usuarios

    @Given("que el cliente tiene un token JWT válido y parámetros de paginación opcionales")
    public void givenJWTValidoConPaginacion() {

    }
    @When("el cliente envía una solicitud GET a {string} con paginación")
    public void whenEnvioSolicitudGETConPaginacion(String url) {
        String jwtToken = realizarLoginYObtenerToken("juancarlos05@example.com", "password123" );
        response = given()
                .header("Authorization", "Bearer " + jwtToken )
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(url)
                .then()
                .extract()
                .response();
    }

    @Then("la respuesta debe contener una lista paginada de usuarios")
    public void thenRespuestaContieneListaDeUsuarios() {
        Assert.assertTrue(response.jsonPath().getList("respuesta").size() > 0);
    }

    // Escenario: Link Recuperar contraseña exitosamente

    @Given("que el cliente proporciona un email válido asociado a una cuenta")
    public void givenEmailValidoParaRecuperarContrasena() {
    }

    @When("el cliente envía una solicitud GET a {string} con el email {string}")
    public void whenEnvioSolicitudGETParaRecuperarContrasena(String url, String email) {

        response = given()
                .header("Content-Type", "application/json")
                .when()
                .get(url + "/" + email)  // Añade el email al final de la URL
                .then()
                .extract()
                .response();
    }

    @Then("el motodo de recuperacion debe devolver un código de estado {int}")
    public void thenCodigoDeEstadoRecuperarContrasena(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Then("la respuesta debe contener el mensaje {string}")
    public void thenRespuestaContieneMensajeRecuperar(String mensajeEsperado) {
        String mensajeReal = response.jsonPath().getString("respuesta");
        Assert.assertEquals(mensajeEsperado, mensajeReal);
    }

// Escenario: Cambiar contraseña exitosamente

    @Given("que el cliente tiene un token de recuperación válido y una nueva contraseña válida")
    public void givenTokenRecuperacionYNuevaContrasena() {
        requestBody = new HashMap<>();
        requestBody.put("token", "MTM7MjAyNC0xMS0xMVQxNzoyMjozMi4zNjc2NTc0MzQ=");  // Token que llega al gmail
        requestBody.put("nuevaPasswd", "la_nueva_contraseña");  // Nueva contraseña válida
    }

    @When("el cliente envía una solicitud POST a {string} para cambiar la contraseña")
    public void whenEnvioSolicitudPOSTParaCambiarContrasena(String url) {
        response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(url)
                .then()
                .extract()
                .response();
        System.out.println(response.asString());
    }

    @Then("el sistema debe devolver un código de estado {int} para cambio de contraseña")
    public void thenCodigoDeEstadoCambioContrasena(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Then("la respuesta debe contener el mensaje de cambio de contraseña {string}")
    public void thenRespuestaContieneMensajeCambio(String mensajeEsperado) {
        String mensajeReal = response.jsonPath().getString("respuesta");
        Assert.assertEquals(mensajeEsperado, mensajeReal);
    }


    public String realizarLoginYObtenerToken(String email, String password) {
        Map<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("email", email);
        loginRequest.put("passwd", password);  // Verifica si es "passwd" o "password"

        // Realiza la solicitud de login
        Response loginResponse = given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post("http://localhost:80/api/auth/login") // Asegúrate de cambiar la URL por la correcta
                .then()
                .extract()
                .response();

        // Extrae el token JWT de la respuesta
        String jwtToken = loginResponse.jsonPath().getString("respuesta.token");

        // Verifica si el token es nulo y lanza una excepción si es necesario
        if (jwtToken == null) {
            throw new RuntimeException("Token JWT no encontrado en la respuesta");
        }

        return jwtToken;
    }
}
