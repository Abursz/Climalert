# Climalert

Sistema de monitoreo climático y envío automático de alertas. Es un servicio
**autónomo, sin interfaz gráfica**, desarrollado para la cátedra Diseño de
Sistemas de Información (UTN.BA).

El sistema se conecta periódicamente a [WeatherAPI](https://www.weatherapi.com/docs/)
para obtener el clima de una ubicación fija (CABA), lo guarda como registro
histórico, y si detecta condiciones críticas (temperatura > 35° y humedad
> 60%) envía un correo de alerta con el detalle completo del clima.

* Java 21. :warning: El proyecto no lo limita explícitamente en el `pom.xml`, pero fue desarrollado y probado con esta versión.
* Spring Boot 3.3 (Web, Data JPA, Mail).
* H2 como base de datos en memoria para el registro histórico.
* JUnit 5 + Mockito para los tests.
* Maven 3.9 o superior (recomendado).

## Configuración previa

Antes de correr el proyecto hay que definir estas variables de entorno
(los valores en `application.properties` son solo placeholders):

| Variable              | Descripción                                          |
|------------------------|--------------------------------------------------------|
| `WEATHERAPI_KEY`      | API key de WeatherAPI                                |
| `WEATHERAPI_LOCATION` | Ubicación a consultar (default `CABA`)               |
| `MAIL_USERNAME`       | Correo remitente                                      |
| `MAIL_PASSWORD`       | Contraseña de aplicación (no la contraseña normal)   |
| `MAIL_HOST` / `MAIL_PORT` | Servidor SMTP (default Gmail, puerto 587)        |

En IntelliJ se configuran desde **Run/Debug Configurations > Environment variables**.
Por consola (Linux/Mac):

```
export WEATHERAPI_KEY=tu_api_key
export MAIL_USERNAME=tu_correo@gmail.com
export MAIL_PASSWORD=tu_password_de_aplicacion
```

## Ejecutar el proyecto

```
mvn spring-boot:run
```

También se puede correr directamente la clase `ClimalertApplication` desde
el IDE. Al arrancar, el servicio queda corriendo de forma autónoma: no
requiere ninguna interacción manual.

## Ejecutar los tests

```
mvn test
```

## Compilar y empaquetar

```
mvn clean package
java -jar target/climalert-0.0.1-SNAPSHOT.jar
```

## Ver el registro histórico (consola H2)

Con la app corriendo, entrar a `http://localhost:8080/h2-console` y conectar con:

* **JDBC URL**: `jdbc:h2:mem:climalert`
* **User Name**: `sa`
* **Password**: (vacío)

## Configuración del IDE (IntelliJ)

### Usar el SDK de Java 21

1. En **File/Project Structure...**, ir a **Project Settings | Project**.
2. En **Project SDK** seleccionar la versión 21 y en **Project language level** seleccionar el nivel 21 (coincidente con el SDK).

### Habilitar el procesamiento de anotaciones (Lombok)

El proyecto usa Lombok para reducir boilerplate en las entidades y servicios.

1. Instalar el plugin [Lombok](https://plugins.jetbrains.com/plugin/6317-lombok).
2. En **File/Settings...**, ir a **Build, Execution, Deployment | Compiler | Annotation Processors**.
3. Tildar **Enable annotation processing**.

## Estructura del proyecto

```
src/main/java/com/utn/climalert
├── ClimalertApplication.java
├── config/AppConfig.java              # Bean de RestTemplate
├── model/WeatherRecord.java           # Entidad JPA + regla de alerta
├── repository/WeatherRecordRepository.java
├── dto/WeatherApiResponse.java        # Records para el JSON de WeatherAPI
├── client/WeatherApiClient.java       # Consumo de WeatherAPI
├── service/
│   ├── WeatherIngestService.java      # Obtiene y guarda el clima
│   ├── AlertService.java              # Analiza el último registro
│   └── EmailNotificationService.java  # Envía el correo de alerta
└── scheduler/ClimalertScheduler.java  # Procesos programados (5 min / 1 min)
```
