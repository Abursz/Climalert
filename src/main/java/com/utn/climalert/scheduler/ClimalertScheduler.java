package com.utn.climalert.scheduler;

import com.utn.climalert.service.AlertService;
import com.utn.climalert.service.WeatherIngestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Climalert es un servicio autonomo, sin interfaz grafica: toda su
 * responsabilidad funcional corre a traves de estos dos procesos
 * programados.
 * <p>
 * Ambos metodos capturan cualquier excepcion inesperada para que un
 * fallo puntual (por ejemplo, WeatherAPI caida) no detenga el scheduler
 * ni afecte al proximo ciclo.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClimalertScheduler {

    private final WeatherIngestService weatherIngestService;
    private final AlertService alertService;

    /**
     * Punto 1 de la consigna: cada 5 minutos obtiene el clima actual y lo
     * almacena localmente.
     */
    @Scheduled(fixedRateString = "${climalert.fetch.fixed-rate-ms}")
    public void ejecutarIngestaClima() {
        try {
            weatherIngestService.obtenerYGuardarClimaActual();
        } catch (Exception e) {
            log.error("Error inesperado durante la ingesta del clima.", e);
        }
    }

    /**
     * Punto 2 de la consigna: cada 1 minuto analiza el ultimo registro
     * disponible y dispara una alerta si corresponde.
     */
    @Scheduled(fixedRateString = "${climalert.alert.fixed-rate-ms}")
    public void ejecutarAnalisisDeAlertas() {
        try {
            alertService.analizarUltimoRegistro();
        } catch (Exception e) {
            log.error("Error inesperado durante el analisis de alertas.", e);
        }
    }
}
