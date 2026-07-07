# Climalert

Sistema de monitoreo climático y envío automático de alertas, desarrollado
como práctica de la cátedra Diseño de Sistemas de Información (UTN.BA).

Climalert es un servicio **autónomo, sin interfaz gráfica**, que:

1. Cada **5 minutos**, se conecta vía REST con [WeatherAPI](https://www.weatherapi.com/docs/)
   (endpoint `/current.json`) para obtener el clima actual de una ubicación
   fija (CABA) y lo almacena localmente para registro histórico.
2. Cada **1 minuto**, analiza el último registro disponible. Si la
   temperatura es mayor a 35° y la humedad mayor a 60%, se considera una
   condición de alerta.
3. Ante una alerta, envía un correo electrónico con el detalle completo
   del clima a `admin@clima.com`, `emergencias@clima.com` y
   `meteorologia@clima.com`.

## Tecnologías

- Java 21
- Spring Boot 3.3 (Web, Data JPA, Mail)
- H2 (base de datos en memoria)
- Maven

## Estructura del proyecto

```
src/main/java/com/utn/climalert
├── ClimalertApplication.java
├── config/
│   └── AppConfig.java              # Bean de RestTemplate con timeouts
├── model/
│   └── WeatherRecord.java          # Entidad JPA + regla de alerta
├── repository/
│   └── WeatherRecordRepository.java
├── dto/
│   └── WeatherApiResponse.java     # Records para el JSON de WeatherAPI
├── client/
│   └── WeatherApiClient.java       # Consumo de WeatherAPI
├── service/
│   ├── WeatherIngestService.java   # Obtiene y guarda el clima (punto 1)
│   ├── AlertService.java           # Analiza el ultimo registro (punto 2)
│   └── EmailNotificationService.java  # Envia el correo (punto 3)
└── scheduler/
    └── ClimalertScheduler.java     # @Scheduled cada 5 min y cada 1 min
```

## Configuración

Antes de correr el proyecto hay que definir estas variables de entorno
(los valores por defecto en `application.properties` son solo placeholders):

| Variable            | Descripción                                        |
|---------------------|------------------------------------------------------|
| `WEATHERAPI_KEY`    | API key de WeatherAPI                                |
| `WEATHERAPI_LOCATION` | Ubicación a consultar (default `CABA`)             |
| `MAIL_USERNAME`     | Correo remitente                                      |
| `MAIL_PASSWORD`     | Contraseña de aplicación (no la contraseña normal de la cuenta) |
| `MAIL_HOST` / `MAIL_PORT` | Servidor SMTP (default Gmail, puerto 587)      |

```bash
export WEATHERAPI_KEY=23312586d8644f21abb01240260707
export MAIL_USERNAME=bursztyn.alexis@gmail.com
export MAIL_PASSWORD=Simeone10Simeone
```

## Cómo ejecutar

Con el wrapper de Maven incluido:

```bash
./mvnw clean spring-boot:run
```

O compilando el jar:

```bash
./mvnw clean package
java -jar target/climalert-0.0.1-SNAPSHOT.jar
```

Al iniciar, la aplicación empieza a correr de forma autónoma: el scheduler
de ingesta arranca de inmediato y el de análisis de alertas cada 1 minuto.
Se puede ver el histórico guardado accediendo a la consola de H2 en
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:climalert`).

## Tests

```bash
./mvnw test
```

- `WeatherRecordAlertTest`: valida la regla de negocio de la alerta.
- `AlertServiceTest`: valida que el servicio de alertas dispare (o no)
  la notificación según corresponda, usando mocks del repositorio y del
  servicio de mail.
- `ClimalertApplicationTests`: smoke test que valida que el contexto de
  Spring levanta correctamente.
