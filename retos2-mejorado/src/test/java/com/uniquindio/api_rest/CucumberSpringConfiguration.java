package com.uniquindio.api_rest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest // O puedes usar WebEnvironment.DEFINED_PORT si prefieres un puerto específico.
@ContextConfiguration(classes = ApiRestApplicationTests.class)
public class CucumberSpringConfiguration {


}
