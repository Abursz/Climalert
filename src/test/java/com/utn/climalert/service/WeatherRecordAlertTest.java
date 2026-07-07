package com.utn.climalert.service;

import com.utn.climalert.model.WeatherRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeatherRecordAlertTest {

    @Test
    void esCondicionDeAlerta_cuandoTempYHumedadSuperanUmbral_esTrue() {
        WeatherRecord registro = WeatherRecord.builder()
                .temperature(36.5)
                .humidity(65)
                .build();

        assertTrue(registro.esCondicionDeAlerta());
    }

    @Test
    void esCondicionDeAlerta_cuandoSoloTempSupera_esFalse() {
        WeatherRecord registro = WeatherRecord.builder()
                .temperature(36.5)
                .humidity(50)
                .build();

        assertFalse(registro.esCondicionDeAlerta());
    }

    @Test
    void esCondicionDeAlerta_cuandoSoloHumedadSupera_esFalse() {
        WeatherRecord registro = WeatherRecord.builder()
                .temperature(30.0)
                .humidity(70)
                .build();

        assertFalse(registro.esCondicionDeAlerta());
    }

    @Test
    void esCondicionDeAlerta_enValoresLimite_esFalse() {
        WeatherRecord registro = WeatherRecord.builder()
                .temperature(35.0)
                .humidity(60)
                .build();

        assertFalse(registro.esCondicionDeAlerta());
    }
}
