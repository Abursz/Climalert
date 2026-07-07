package com.utn.climalert.service;

import com.utn.climalert.model.WeatherRecord;
import com.utn.climalert.repository.WeatherRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Punto 2 de la consigna: analiza la ultima informacion climatica
 * disponible y dispara una alerta cuando corresponde.
 * <p>
 * Se mantiene independiente de {@link EmailNotificationService} (que es
 * quien sabe "como" se notifica) para que ambas responsabilidades se
 * puedan mantener y testear por separado.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final WeatherRecordRepository weatherRecordRepository;
    private final EmailNotificationService emailNotificationService;

    public void analizarUltimoRegistro() {
        Optional<WeatherRecord> ultimo = weatherRecordRepository.findTopByOrderByFetchedAtDesc();

        if (ultimo.isEmpty()) {
            log.debug("Todavia no hay registros climaticos para analizar.");
            return;
        }

        WeatherRecord registro = ultimo.get();

        if (registro.isAlertaNotificada()) {
            log.debug("El ultimo registro ya genero una alerta notificada previamente.");
            return;
        }

        if (!registro.esCondicionDeAlerta()) {
            log.debug("Condiciones normales (temp={}, humedad={}). No se genera alerta.",
                    registro.getTemperature(), registro.getHumidity());
            return;
        }

        log.warn("Condicion critica detectada (temp={}, humedad={}). Enviando alerta...",
                registro.getTemperature(), registro.getHumidity());

        emailNotificationService.enviarAlerta(registro);

        registro.setAlertaNotificada(true);
        weatherRecordRepository.save(registro);
    }
}
