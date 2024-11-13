package com.uniquindio.api_rest.dto;

import com.uniquindio.api_rest.model.User;

public record ItemUserDTO(
        int id,
        String name,

        String lastname
) {

    public ItemUserDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getLastname()
        );
    }
}
