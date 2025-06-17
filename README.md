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
│   └── TestConexion.java        # Clase de prueba
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
- **Contraseña:** `apppassword123`

### 3. Cambiar credenciales (opcional)
Si deseas cambiar las credenciales, modifica estas constantes en `ConexionDB.java`:
```java
private static final String USUARIO = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

## Compilación y Ejecución

### 1. Compilar el proyecto
```bash
# Compilar con el driver MySQL en el classpath
javac -cp "lib/mysql-connector-j-9.3.0.jar" src/*.java
```

### 2. Ejecutar la prueba de conexión
```bash
# Ejecutar desde el directorio raíz del proyecto
java -cp "lib/mysql-connector-j-9.3.0.jar:src" TestConexion
```

## Uso de la Clase ConexionDB

### Obtener una conexión
```java
ConexionDB db = ConexionDB.getInstance();
Connection conn = db.getConexion();
```

### Ejecutar consultas SELECT
```java
// Consulta simple
ResultSet rs = db.ejecutarConsulta("SELECT * FROM PERSONA");

// Consulta con parámetros
ResultSet rs = db.ejecutarConsultaPreparada(
    "SELECT * FROM PERSONA WHERE PKPerCod = ?", 
    idPersona
);
```

### Ejecutar INSERT, UPDATE, DELETE
```java
// Inserción simple
int filas = db.ejecutarActualizacion(
    "INSERT INTO PERSONA (PerNom, PerEma) VALUES ('Juan', 'juan@email.com')"
);

// Inserción con parámetros y retorno de ID
int nuevoId = db.ejecutarInsertConId(
    "INSERT INTO PERSONA (PerNom, PerEma) VALUES (?, ?)",
    "Juan Pérez", "juan@email.com"
);

// Actualización con parámetros
int filas = db.ejecutarActualizacionPreparada(
    "UPDATE PERSONA SET PerNom = ? WHERE PKPerCod = ?",
    "Juan Carlos Pérez", idPersona
);
```

### Manejo de transacciones
```java
try {
    db.iniciarTransaccion();
    
    // Ejecutar múltiples operaciones
    db.ejecutarActualizacionPreparada("INSERT INTO ...", params1);
    db.ejecutarActualizacionPreparada("UPDATE ...", params2);
    
    db.confirmarTransaccion();
} catch (SQLException e) {
    db.revertirTransaccion();
    throw e;
}
```

## Características de la Clase ConexionDB

- **Patrón Singleton:** Una sola instancia de conexión por aplicación
- **Conexión automática:** Reconexión automática si se pierde la conexión
- **Métodos preparados:** Prevención de inyección SQL
- **Manejo de transacciones:** Soporte completo para transacciones
- **Logging:** Registro de eventos y errores
- **Thread-safe:** Seguro para uso en aplicaciones multihilo

## Esquema de Base de Datos

El sistema incluye las siguientes tablas principales:
- `PERSONA` - Datos básicos de las personas
- `EXPERIENCIA` - Experiencia laboral
- `FORMACION_ACADEMICA` - Formación académica
- `HABILIDAD` - Habilidades técnicas y personales
- `IDIOMA` - Idiomas y niveles
- `CERTIFICACION` - Certificaciones profesionales
- `CURSO_CERTIFICADO` - Cursos realizados

## Requisitos

- Java 8 o superior
- MySQL 5.7 o superior
- Driver MySQL Connector/J (incluido en `lib/`)

## Troubleshooting

### Error de conexión
1. Verificar que MySQL esté ejecutándose
2. Confirmar que la base de datos `datospersonales` existe
3. Verificar credenciales del usuario `appuser`

### Error de driver
1. Verificar que `mysql-connector-j-9.3.0.jar` esté en el classpath
2. Usar la versión correcta del driver para tu versión de Java

### Problemas de permisos
```sql
-- Otorgar permisos manualmente si es necesario
GRANT ALL PRIVILEGES ON datospersonales.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;
```