package com.utn.climalert.client;

import com.utn.climalert.dto.WeatherApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class WeatherApiClient {

    private final RestTemplate restTemplate;

    @Value("${weatherapi.base-url}")
    private String baseUrl;

    @Value("${weatherapi.api-key}")
    private String apiKey;

    @Value("${weatherapi.location}")
    private String location;

    public WeatherApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Consulta el clima actual para la ubicacion configurada.
     * Devuelve null si el proveedor externo falla, para que quien la
     * invoque decida como manejarlo sin romper el ciclo del scheduler.
     */
    public WeatherApiResponse.Root getCurrentWeather() {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/current.json")
                .queryParam("key", apiKey)
                .queryParam("q", location)
                .queryParam("aqi", "no")
                .toUriString();

        try {
            return restTemplate.getForObject(url, WeatherApiResponse.Root.class);
        } catch (RestClientException e) {
            log.error("Error al consultar WeatherAPI: {}", e.getMessage());
            return null;
        }
    }
}
