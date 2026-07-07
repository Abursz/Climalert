package com.utn.climalert.service;

import com.utn.climalert.client.WeatherApiClient;
import com.utn.climalert.dto.WeatherApiResponse;
import com.utn.climalert.model.WeatherRecord;
import com.utn.climalert.repository.WeatherRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Punto 1 de la consigna: integracion con WeatherAPI y almacenamiento
 * local para registro historico.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherIngestService {

    private final WeatherApiClient weatherApiClient;
    private final WeatherRecordRepository weatherRecordRepository;

    public void obtenerYGuardarClimaActual() {
        WeatherApiResponse.Root response = weatherApiClient.getCurrentWeather();

        if (response == null || response.current() == null) {
            log.warn("No se pudo obtener el clima actual desde WeatherAPI. Se omite este ciclo.");
            return;
        }

        WeatherApiResponse.Current current = response.current();
        String nombreUbicacion = response.location() != null ? response.location().name() : "desconocida";

        WeatherRecord registro = WeatherRecord.builder()
                .location(nombreUbicacion)
                .temperature(current.temp_c())
                .humidity(current.humidity())
                .feelsLike(current.feelslike_c())
                .windKph(current.wind_kph())
                .pressure(current.pressure_mb())
                .condition(current.condition() != null ? current.condition().text() : null)
                .providerLastUpdated(current.last_updated())
                .fetchedAt(LocalDateTime.now())
                .alertaNotificada(false)
                .build();

        weatherRecordRepository.save(registro);
        log.info("Clima almacenado: {} - {}°C, {}% humedad", nombreUbicacion, current.temp_c(), current.humidity());
    }
}
