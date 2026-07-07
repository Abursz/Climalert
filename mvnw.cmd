@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script (Windows)
@REM
@REM Generado por Spring Initializr (Maven Wrapper). Permite compilar y correr
@REM el proyecto sin tener Maven instalado globalmente: mvnw.cmd clean install
@REM ----------------------------------------------------------------------------
@echo off

set BASE_DIR=%~dp0
set WRAPPER_JAR=%BASE_DIR%.mvn\wrapper\maven-wrapper.jar
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

if not "%JAVA_HOME%"=="" (
  set JAVACMD=%JAVA_HOME%\bin\java.exe
) else (
  set JAVACMD=java.exe
)

"%JAVACMD%" %MAVEN_OPTS% -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%BASE_DIR%" %WRAPPER_LAUNCHER% %*
