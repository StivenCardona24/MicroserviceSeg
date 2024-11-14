Feature: Registro de logs en acciones del usuario

  Scenario: Registrar un nuevo usuario y generar un log
    Given datos cliente nombre, apellido, cédula, email, contraseña, teléfono, fecha de nacimiento y dirección
    When registro un nuevo usuario con datos válidos a "http://localhost:80/api/auth/registrarse"
    Then el log debe estar registrado en el servicio de logs
