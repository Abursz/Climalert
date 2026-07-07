#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Start Up Batch script
#
# Generado por Spring Initializr (Maven Wrapper). Permite compilar y correr
# el proyecto sin tener Maven instalado globalmente: ./mvnw clean install
# ----------------------------------------------------------------------------

if [ -z "$MAVEN_SKIP_RC" ]; then
  if [ -f /usr/local/etc/mavenrc ]; then
    . /usr/local/etc/mavenrc
  fi
  if [ -f /etc/mavenrc ]; then
    . /etc/mavenrc
  fi
  if [ -f "$HOME/.mavenrc" ]; then
    . "$HOME/.mavenrc"
  fi
fi

BASE_DIR=$(dirname "$0")
WRAPPER_JAR="$BASE_DIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES="$BASE_DIR/.mvn/wrapper/maven-wrapper.properties"
WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

if [ -z "$JAVA_HOME" ]; then
  JAVACMD="$(command -v java)"
else
  JAVACMD="$JAVA_HOME/bin/java"
fi

if [ ! -x "$JAVACMD" ]; then
  echo "Error: no se encontro un ejecutable de Java. Configura JAVA_HOME." >&2
  exit 1
fi

exec "$JAVACMD" $MAVEN_OPTS \
  -classpath "$WRAPPER_JAR" \
  "-Dmaven.multiModuleProjectDirectory=$BASE_DIR" \
  $WRAPPER_LAUNCHER "$@"
