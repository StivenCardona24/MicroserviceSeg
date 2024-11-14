Feature: Gestión de usuarios de la API
  La API debe permitir registrar, listar, editar, eliminar y recuperar usuarios de manera segura usando JWT.


  @Test
  Scenario: Crear un nuevo usuario exitosamente
    Given que el cliente tiene un payload válido con nombre, apellido, cédula, email, contraseña, teléfono, fecha de nacimiento y dirección
    When el cliente envía una solicitud POST a "http://localhost:80/api/auth/registrarse"
    Then el sistema debe devolver un código de estado 200
    And la respuesta debe contener el mensaje de creación "Usuario {userId} registrado correctamente"
  @Test
  Scenario: Login exitoso
    Given que el cliente tiene credenciales válidas con un email y contraseña
    When el cliente envía una solicitud POST a "http://localhost:80/api/auth/login"
    Then el sistema debe devolver un código de estado 200
    And la respuesta debe contener un token JWT válido

  @Test
  Scenario: Recuperar detalles de un usuario exitosamente
    Given que el cliente tiene un token JWT válido y un ID de usuario existente
    When el cliente envia una solicitud GET a "http://localhost:80/api/users/detalle" para obtener los detalles del usuario con ID 3
    Then el sistema debe devolver un código de estado 200
    And la respuesta debe contener los detalles del usuario con nombre "camilo" y email "juan.carlos.04@example.com"

  @Test
  Scenario: Listar todos los usuarios exitosamente
    Given que el cliente tiene un token JWT válido y parámetros de paginación opcionales
    When el cliente envía una solicitud GET a "http://localhost:80/api/users/listar-todos?page=0&size=10" con paginación
    Then el sistema debe devolver un código de estado 200
    And la respuesta debe contener una lista paginada de usuarios
  @Test
  Scenario: Editar usuario exitosamente
    Given que el cliente tiene un token JWT válido y un payload con datos actualizados para un usuario existente
    When el cliente envía una solicitud PUT a "http://localhost:80/api/users/editar"
    Then el metodo de edicion debe devolver un código de estado 200
    And la respuesta debe contener el mensaje de edición "Usuario actualizado correctamente"
  @Test
  Scenario: Eliminar usuario exitosamente
    Given que el cliente tiene un token JWT válido y un ID de usuario existente para eliminar
    When el cliente envía una solicitud DELETE a "http://localhost:80/api/users/eliminar" para eliminar el usuario con ID 1
    Then el metodo de eliminar debe devolver un código de estado 200

  @Test
  Scenario: Link Recuperar contraseña exitosamente
    Given que el cliente proporciona un email válido asociado a una cuenta
    When el cliente envía una solicitud GET a "http://localhost:80/api/auth/recuperar-passwd" con el email "juancarlos09@yopmail.com"
    Then el motodo de recuperacion debe devolver un código de estado 200
    And la respuesta debe contener el mensaje "Se ha enviado el correo con el link de recuperación."

  @Test
  Scenario: Cambiar contraseña exitosamente
    Given que el cliente tiene un token de recuperación válido y una nueva contraseña válida
    When el cliente envía una solicitud POST a "http://localhost:80/api/auth/cambiar-passwd"
    Then el sistema debe devolver un código de estado 200
    And la respuesta debe contener el mensaje "Contraseña actualizada con éxito."



