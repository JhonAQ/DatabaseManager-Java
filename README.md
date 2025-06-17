# Database Manager - Java

Sistema de gestión de base de datos para currículums y datos personales.

## Estructura del Proyecto

```
DatabaseManager-Java/
├── db/
│   └── DatosPersonales.sql      # Script de base de datos con tablas y usuario
├── lib/
│   └── mysql-connector-j-9.3.0.jar  # Driver MySQL
├── src/
│   ├── ConexionDB.java          # Clase principal de conexión a BD
│   ├── DatabaseManagerGUI.java  # Interfaz gráfica
│   └── TestConexion.java        # Clase de prueba
├── run.bat                      # Script de ejecución (Windows)
├── run.sh                       # Script de ejecución (Linux/Mac)
├── build.bat                    # Script de construcción JAR (Windows)
├── build.sh                     # Script de construcción JAR (Linux/Mac)
├── MANIFEST.MF                  # Manifiesto para JAR ejecutable
└── .gitignore                   # Archivos a ignorar en Git
```

## Configuración de la Base de Datos

### 1. Crear la base de datos
Ejecuta el archivo `db/DatosPersonales.sql` en tu servidor MySQL:

```bash
mysql -u root -p < db/DatosPersonales.sql
```

### 2. Configuración del usuario
El script crea automáticamente:
- **Base de datos:** `datospersonales`
- **Usuario:** `appuser`
- **Contraseña:** `labDbAbet`

## Ejecución Rápida

### Windows
```cmd
run.bat
```

### Linux/Mac
```bash
chmod +x run.sh
./run.sh
```

## Compilación y Ejecución Manual

### Windows
```cmd
# Compilar
javac -cp "lib\mysql-connector-j-9.3.0.jar" -d . src\*.java

# Ejecutar (IMPORTANTE: usar ; en Windows)
java -cp "lib\mysql-connector-j-9.3.0.jar;." db.DatabaseManagerGUI

# Ejecutar prueba de conexión
java -cp "lib\mysql-connector-j-9.3.0.jar;." db.TestConexion
```

### Linux/Mac
```bash
# Compilar
javac -cp "lib/mysql-connector-j-9.3.0.jar" -d . src/*.java

# Ejecutar (IMPORTANTE: usar : en Linux/Mac)
java -cp "lib/mysql-connector-j-9.3.0.jar:." db.DatabaseManagerGUI

# Ejecutar prueba de conexión
java -cp "lib/mysql-connector-j-9.3.0.jar:." db.TestConexion
```

## Construcción del JAR Ejecutable

### Windows
```cmd
build.bat
java -jar DatabaseManager.jar
```

### Linux/Mac
```bash
chmod +x build.sh
./build.sh
java -jar DatabaseManager.jar
```

## Resolución de Problemas Comunes

### Error "Could not find or load main class"
**Causa:** Separador de classpath incorrecto o clases no compiladas.

**Solución:**
- **Windows:** Usar `;` como separador: `java -cp "lib\mysql-connector-j-9.3.0.jar;." db.DatabaseManagerGUI`
- **Linux/Mac:** Usar `:` como separador: `java -cp "lib/mysql-connector-j-9.3.0.jar:." db.DatabaseManagerGUI`

### Error de interfaz gráfica en servidor
Si obtienes errores como `UnsatisfiedLinkError` relacionados con librerías gráficas:

1. **Ejecutar prueba de conexión sin GUI:**
```bash
java -cp "lib/mysql-connector-j-9.3.0.jar:." db.TestConexion
```

2. **Instalar entorno gráfico mínimo (Linux):**
```bash
sudo apt-get update
sudo apt-get install xvfb
xvfb-run -a java -cp "lib/mysql-connector-j-9.3.0.jar:." db.DatabaseManagerGUI
```

3. **Usar forwarding X11 con SSH:**
```bash
ssh -X usuario@servidor
java -cp "lib/mysql-connector-j-9.3.0.jar:." db.DatabaseManagerGUI
```

### Error de conexión a base de datos
1. Verificar que MySQL esté ejecutándose
2. Confirmar que la base de datos `datospersonales` existe
3. Verificar credenciales del usuario `appuser`

### Verificar configuración
```sql
-- Conectar como root y verificar
SHOW DATABASES LIKE 'datospersonales';
SELECT User, Host FROM mysql.user WHERE User = 'appuser';
```