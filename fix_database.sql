USE datospersonales;

-- Agregar columna EstadoRegistro a TIPO_FORMACION si no existe
ALTER TABLE TIPO_FORMACION ADD COLUMN EstadoRegistro CHAR(1) DEFAULT 'A';

-- Agregar columna EstadoRegistro a TIPO_HABILIDAD si no existe  
ALTER TABLE TIPO_HABILIDAD ADD COLUMN EstadoRegistro CHAR(1) DEFAULT 'A';

-- Actualizar registros existentes
UPDATE TIPO_FORMACION SET EstadoRegistro = 'A' WHERE EstadoRegistro IS NULL;
UPDATE TIPO_HABILIDAD SET EstadoRegistro = 'A' WHERE EstadoRegistro IS NULL;

SELECT 'Columnas EstadoRegistro agregadas exitosamente' as Mensaje;
