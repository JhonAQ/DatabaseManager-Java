#!/bin/bash

echo "=== EJECUTANDO DATABASE MANAGER ==="

# Verificar que existe el archivo JAR de MySQL
if [ ! -f "lib/mysql-connector-j-9.3.0.jar" ]; then
    echo "Error: No se encuentra el driver MySQL en lib/mysql-connector-j-9.3.0.jar"
    exit 1
fi

# Compilar si es necesario
if [ ! -f "db/DatabaseManagerGUI.class" ]; then
    echo "Compilando clases Java..."
    javac -cp "lib/mysql-connector-j-9.3.0.jar" -d . src/*.java
    if [ $? -ne 0 ]; then
        echo "Error en la compilación"
        exit 1
    fi
fi

# Ejecutar la aplicación (NOTA: usar : en Linux/Mac)
echo "Ejecutando aplicación..."
java -cp "lib/mysql-connector-j-9.3.0.jar:." db.DatabaseManagerGUI
