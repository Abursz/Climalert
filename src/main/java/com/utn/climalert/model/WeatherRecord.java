package com.utn.climalert.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private Double temperature;
    private Integer humidity;
    private Double feelsLike;
    private Double windKph;
    private Double pressure;
    private String condition;
    private String providerLastUpdated;
    private LocalDateTime fetchedAt;
    private boolean alertaNotificada;

    /**
     * Regla de negocio de la consigna: se considera "alerta" cuando la
     * temperatura supera los 35° y la humedad supera el 60%.
     */
    public boolean esCondicionDeAlerta() {
        return temperature != null && humidity != null
                && temperature > 35.0 && humidity > 60;
    }
}
