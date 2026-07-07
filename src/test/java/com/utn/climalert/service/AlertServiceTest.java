package com.utn.climalert.service;

import com.utn.climalert.model.WeatherRecord;
import com.utn.climalert.repository.WeatherRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private WeatherRecordRepository weatherRecordRepository;

    @Mock
    private EmailNotificationService emailNotificationService;

    private AlertService alertService;

    @BeforeEach
    void setUp() {
        alertService = new AlertService(weatherRecordRepository, emailNotificationService);
    }

    @Test
    void siHayCondicionCritica_seEnviaLaAlertaYSeMarcaComoNotificada() {
        WeatherRecord registro = registro(36.0, 65, false);
        when(weatherRecordRepository.findTopByOrderByFetchedAtDesc()).thenReturn(Optional.of(registro));

        alertService.analizarUltimoRegistro();

        verify(emailNotificationService, times(1)).enviarAlerta(registro);
        verify(weatherRecordRepository, times(1)).save(argThat(WeatherRecord::isAlertaNotificada));
    }

    @Test
    void siNoHayCondicionCritica_noSeEnviaAlerta() {
        WeatherRecord registro = registro(28.0, 40, false);
        when(weatherRecordRepository.findTopByOrderByFetchedAtDesc()).thenReturn(Optional.of(registro));

        alertService.analizarUltimoRegistro();

        verify(emailNotificationService, never()).enviarAlerta(any());
    }

    @Test
    void siLaAlertaYaFueNotificada_noSeReenvia() {
        WeatherRecord registro = registro(38.0, 70, true);
        when(weatherRecordRepository.findTopByOrderByFetchedAtDesc()).thenReturn(Optional.of(registro));

        alertService.analizarUltimoRegistro();

        verify(emailNotificationService, never()).enviarAlerta(any());
    }

    @Test
    void siNoHayRegistros_noHaceNada() {
        when(weatherRecordRepository.findTopByOrderByFetchedAtDesc()).thenReturn(Optional.empty());

        alertService.analizarUltimoRegistro();

        verifyNoInteractions(emailNotificationService);
    }

    private WeatherRecord registro(double temp, int humedad, boolean notificada) {
        return WeatherRecord.builder()
                .id(1L)
                .location("CABA")
                .temperature(temp)
                .humidity(humedad)
                .feelsLike(temp)
                .windKph(10.0)
                .pressure(1013.0)
                .condition("Despejado")
                .providerLastUpdated("2026-07-05 12:00")
                .fetchedAt(LocalDateTime.now())
                .alertaNotificada(notificada)
                .build();
    }
}
