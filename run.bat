@echo off
echo === EJECUTANDO DATABASE MANAGER ===

REM Verificar que existe el archivo JAR de MySQL
if not exist "lib\mysql-connector-j-9.3.0.jar" (
    echo Error: No se encuentra el driver MySQL en lib\mysql-connector-j-9.3.0.jar
    pause
    exit /b 1
)

REM Compilar si es necesario
if not exist "db\DatabaseManagerGUI.class" (
    echo Compilando clases Java...
    javac -cp "lib\mysql-connector-j-9.3.0.jar" -d . src\*.java
    if errorlevel 1 (
        echo Error en la compilacion
        pause
        exit /b 1
    )
)

REM Ejecutar la aplicacion (NOTA: usar ; en Windows en lugar de :)
echo Ejecutando aplicacion...
java -cp "lib\mysql-connector-j-9.3.0.jar;." db.DatabaseManagerGUI

pause
