package com.utn.climalert.service;

import com.utn.climalert.model.WeatherRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Punto 3 de la consigna: envia por correo electronico el detalle
 * completo del clima a las entidades correspondientes cuando se genera
 * una alerta.
 */
@Slf4j
@Service
public class EmailNotificationService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final JavaMailSender mailSender;
    private final String fromAddress;

    @Value("${climalert.alert.recipients}")
    private String[] destinatarios;

    public EmailNotificationService(JavaMailSender mailSender,
                                     @Value("${spring.mail.username}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    public void enviarAlerta(WeatherRecord registro) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(fromAddress);
        mensaje.setTo(destinatarios);
        mensaje.setSubject("[Climalert] Alerta meteorologica - " + registro.getLocation());
        mensaje.setText(construirCuerpo(registro));

        try {
            mailSender.send(mensaje);
            log.info("Correo de alerta enviado a {}", (Object) destinatarios);
        } catch (MailException e) {
            // No relanzamos la excepcion: un fallo de SMTP no debe tumbar
            // el ciclo del scheduler, se reintentara en el proximo analisis.
            log.error("No se pudo enviar el correo de alerta: {}", e.getMessage());
        }
    }

    private String construirCuerpo(WeatherRecord r) {
        return """
                Se ha detectado una condicion climatica critica.

                Ubicacion: %s
                Fecha/hora del dato (proveedor): %s
                Fecha/hora de deteccion (sistema): %s

                --- Detalle completo del clima ---
                Temperatura: %.1f C
                Sensacion termica: %.1f C
                Humedad: %d %%
                Viento: %.1f km/h
                Presion: %.1f mb
                Condicion: %s

                Umbrales de alerta: temperatura > 35C y humedad > 60%%.

                Este es un mensaje automatico generado por Climalert.
                """.formatted(
                r.getLocation(),
                r.getProviderLastUpdated(),
                r.getFetchedAt().format(FORMATTER),
                r.getTemperature(),
                r.getFeelsLike(),
                r.getHumidity(),
                r.getWindKph(),
                r.getPressure(),
                r.getCondition()
        );
    }
}
