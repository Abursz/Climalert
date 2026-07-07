package com.utn.climalert.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTOs que representan la respuesta del endpoint /current.json de WeatherAPI
 * (https://www.weatherapi.com/docs/). Se usan records de Java 21 porque son
 * inmutables y no necesitan getters/setters manuales.
 */
public class WeatherApiResponse {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Root(Location location, Current current) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Location(String name, String region, String country) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Current(
            String last_updated,
            Double temp_c,
            Integer humidity,
            Double feelslike_c,
            Double wind_kph,
            Double pressure_mb,
            Condition condition
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Condition(String text) {
    }
}
