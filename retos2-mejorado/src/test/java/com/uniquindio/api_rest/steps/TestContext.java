package com.uniquindio.api_rest.steps;

import lombok.Getter;
import org.springframework.stereotype.Component;


@Getter
public class TestContext {
    private String jwtToken;

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
