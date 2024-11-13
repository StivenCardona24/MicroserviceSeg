Feature: Gestión de usuarios de la API
  La API debe permitir registrar, listar, editar, eliminar y recuperar usuarios de manera segura usando JWT.

  Scenario: Crear un nuevo usuario exitosamente
    Given que el cliente tiene un payload válido con nombre, apellido, cédula, email, contraseña, teléfono, fecha de nacimiento y dirección
    When el cliente envía una solicitud POST a "http://localhost:80/api/auth/registrarse"
    Then el sistema debe devolver un código de estado 200
    And la respuesta debe contener el mensaje de creación "Usuario {userId} registrado correctamente"

  Scenario: Login exitoso
    Given que el cliente tiene credenciales válidas con un email y contraseña
    When el cliente envía una solicitud POST a "http://localhost:80/api/auth/login"
    Then el sistema debe devolver un código de estado 200
    And la respuesta debe contener un token JWT válido

  Scenario: Login fallido por credenciales incorrectas
    Given que el cliente tiene credenciales inválidas con un email incorrecto o una contraseña incorrecta
    When el cliente envía una solicitud POST a "http://localhost:80/api/auth/login"
    Then el sistema debe devolver un código de estado 401
    And la respuesta debe contener el mensaje "Credenciales inválidas"

  Scenario: Recuperar detalles de un usuario exitosamente
    Given que el cliente tiene un token JWT válido y un ID de usuario existente
    When el cliente envia una solicitud GET a "http://localhost:80/api/users/detalle" para obtener los detalles del usuario con ID 3
    Then el sistema debe devolver un código de estado 200
    And la respuesta debe contener los detalles del usuario con nombre "camilo" y email "juan.carlos.04@example.com"

  Scenario: Usuario no encontrado al intentar recuperar detalles
    Given que el cliente tiene un token JWT válido y un ID de usuario inexistente
    When el cliente envía una solicitud GET a "http://localhost:80/api/users/detalle/{id}"
    Then el sistema debe devolver un código de estado 404
    And la respuesta debe contener el mensaje "Usuario no encontrado"

  Scenario: Listar todos los usuarios exitosamente
    Given que el cliente tiene un token JWT válido y parámetros de paginación opcionales
    When el cliente envía una solicitud GET a "http://localhost:80/api/users/listar-todos?page=0&size=10" con paginación
    Then el sistema debe devolver un código de estado 200
    And la respuesta debe contener una lista paginada de usuarios

  Scenario: Error al listar usuarios por parámetros de paginación inválidos
    Given que el cliente tiene un token JWT válido y parámetros de paginación inválidos
    When el cliente envía una solicitud GET a "http://localhost:80/api/users/listar-todos?page=-1&size=10"
    Then el sistema debe devolver un código de estado 400
    And la respuesta debe contener el mensaje "Parámetros de paginación inválidos"

  Scenario: Editar usuario exitosamente
    Given que el cliente tiene un token JWT válido y un payload con datos actualizados para un usuario existente
    When el cliente envía una solicitud PUT a "http://localhost:80/api/users/editar"
    Then el metodo de edicion debe devolver un código de estado 200
    And la respuesta debe contener el mensaje de edición "Usuario actualizado correctamente"

  Scenario: Error al editar usuario no encontrado
    Given que el cliente tiene un token JWT válido y un payload con un ID de usuario inexistente
    When el cliente envía una solicitud PUT a "http://localhost:80/api/users/editar"
    Then el sistema debe devolver un código de estado 404
    And la respuesta debe contener el mensaje "Usuario no encontrado"

  Scenario: Eliminar usuario exitosamente
    Given que el cliente tiene un token JWT válido y un ID de usuario existente para eliminar
    When el cliente envía una solicitud DELETE a "http://localhost:80/api/users/eliminar" para eliminar el usuario con ID 1
    Then el metodo de eliminar debe devolver un código de estado 200

  Scenario: Error al eliminar usuario no encontrado
    Given que el cliente tiene un token JWT válido y un ID de usuario inexistente
    When el cliente envía una solicitud DELETE a "http://localhost:80/api/users/eliminar/{id}"
    Then el sistema debe devolver un código de estado 404
    And la respuesta debe contener el mensaje "Usuario no encontrado"

  Scenario: Link Recuperar contraseña exitosamente
    Given que el cliente proporciona un email válido asociado a una cuenta
    When el cliente envía una solicitud GET a "http://localhost:80/api/auth/recuperar-passwd" con el email "juancarlos09@yopmail.com"
    Then el motodo de recuperacion debe devolver un código de estado 200
    And la respuesta debe contener el mensaje "Se ha enviado el correo con el link de recuperación."


  Scenario: Error al enviar link para recuperar contraseña de usuario no encontrado
    Given que el cliente proporciona un email no registrado en el sistema
    When el cliente envía una solicitud POST a "http://localhost:80/api/auth/recuperar-passwd/{email}"
    Then el sistema debe devolver un código de estado 404
    And la respuesta debe contener el mensaje "Usuario no encontrado"

  Scenario: Cambiar contraseña exitosamente
    Given que el cliente tiene un token de recuperación válido y una nueva contraseña válida
    When el cliente envía una solicitud POST a "http://localhost:80/api/auth/cambiar-passwd"
    Then el sistema debe devolver un código de estado 200
    And la respuesta debe contener el mensaje "Contraseña actualizada con éxito."

  Scenario: Error al cambiar contraseña por token inválido
    Given que el cliente proporciona un token de recuperación inválido o expirado
    When el cliente envía una solicitud POST a "http://localhost:80/api/auth/cambiar-passwd"
    Then el sistema debe devolver un código de estado 400
    And la respuesta debe contener el mensaje "Token inválido o formato de contraseña incorrecto"


