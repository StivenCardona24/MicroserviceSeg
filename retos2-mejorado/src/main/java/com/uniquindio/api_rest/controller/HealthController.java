package com.uniquindio.api_rest.controller;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Health", description = "Operaciones relacionadas con la salud del servicio")
public class HealthController {

    private final Instant  startTime = Instant.now();

    @Value("${app.version}")
    private String appVersion;

    private String getUptime() {
        Duration uptime = Duration.between(startTime, Instant.now());
        long seconds = uptime.get(ChronoUnit.SECONDS);
        if (seconds < 60) {
            return "a few seconds";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " minute" + (minutes > 1 ? "s" : "");
        } else {
            long hours = seconds / 3600;
            return hours + " hour" + (hours > 1 ? "s" : "");
        }
    }

    @Operation(summary = "Función para verificar la salud del servicio", description = "Retorna un objeto Health con la versión de la aplicación y el tiempo de inicio")
    @GetMapping("/health")
    public Health health() {
        return Health.up()
                .withDetail("version", appVersion)
                .withDetail("uptime", getUptime())
                .withDetail("checks", new Object[]{
            new Object() {
                public String name = "Readiness check";
                public String status = "UP";
                public Object data = new Object() {
                    public String from = startTime.toString();
                    public String status = "READY";
                };
            },
            new Object() {
                public String name = "Liveness check";
                public String status = "UP";
                public Object data = new Object() {
                    public String from = startTime.toString();
                    public String status = "ALIVE";
                };
            }
        })
                .build();
    }

    @Operation(summary = "Función para verificar si el servicio está vivo", description = "Retorna un objeto Health con el estado ALIVE y el tiempo de inicio")
    @GetMapping("/health/live")
    public Health live() {
        return Health.up()
                .withDetail("status", "ALIVE")
                .withDetail("from", startTime)
                .build();
    }

    @Operation(summary = "Función para verificar si el servicio está listo", description = "Retorna un objeto Health con el estado READY y el tiempo de inicio")
    @GetMapping("/health/ready")
    public Health ready() {
        // Aquí podrías agregar más lógica para verificar que el servicio esté listo, como verificar la conexión a la base de datos
        return Health.up()
                .withDetail("status", "READY")
                .withDetail("from", startTime)
                .build();
    }
}
