-- Para eliminar si ya esta creada la base de datos
-- Eliminar la base de datos completa
DROP DATABASE IF EXISTS datospersonales;

-- Eliminar el usuario si existe
DROP USER IF EXISTS 'appuser'@'localhost';

-- Confirmar cambios
FLUSH PRIVILEGES;

SELECT 'Base de datos y usuario eliminados exitosamente' as Mensaje;


-- Sistema de Gestión de Currículums
-- Script de creación de base de datos

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS datospersonales;
USE datospersonales;

-- Crear usuario para la aplicación
CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'labDbAbet';
GRANT ALL PRIVILEGES ON datospersonales.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;

-- Tabla principal de personas
CREATE TABLE PERSONA (
    PKPerCod INT PRIMARY KEY AUTO_INCREMENT,
    PerNom VARCHAR(100) NOT NULL,
    PerDir VARCHAR(200),
    PerTel VARCHAR(20),
    PerEma VARCHAR(100)
);

-- Tabla catálogo de idiomas
CREATE TABLE CATALOGO_IDIOMAS (
    CodIdioma VARCHAR(5) PRIMARY KEY,
    NombreIdioma VARCHAR(50) NOT NULL
);

-- Tabla de idiomas por persona
CREATE TABLE IDIOMA (
    PKIdiCod INT PRIMARY KEY AUTO_INCREMENT,
    FKPerCod INT NOT NULL,
    CodIdioma VARCHAR(5) NOT NULL,
    NivelIdioma VARCHAR(20),
    FOREIGN KEY (FKPerCod) REFERENCES PERSONA(PKPerCod) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (CodIdioma) REFERENCES CATALOGO_IDIOMAS(CodIdioma) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Tabla catálogo de empresas
CREATE TABLE EXP_EMPRESA (
    PKExpEmp INT PRIMARY KEY AUTO_INCREMENT,
    NombreEmpresa VARCHAR(100) NOT NULL
);

-- Tabla catálogo de cargos
CREATE TABLE CARGO (
    PKExpCar INT PRIMARY KEY AUTO_INCREMENT,
    DescripcionCargo VARCHAR(100) NOT NULL
);

-- Tabla de experiencia laboral
CREATE TABLE EXPERIENCIA (
    PKExpCod INT PRIMARY KEY AUTO_INCREMENT,
    FKPerCod INT NOT NULL,
    FKExpEmp INT NOT NULL,
    FKExpCar INT NOT NULL,
    ExpCarIni DATE,
    ExpCarFin DATE,
    FOREIGN KEY (FKPerCod) REFERENCES PERSONA(PKPerCod) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (FKExpEmp) REFERENCES EXP_EMPRESA(PKExpEmp) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (FKExpCar) REFERENCES CARGO(PKExpCar) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Tabla catálogo de tipos de formación
CREATE TABLE TIPO_FORMACION (
    PKCodTipoFor INT PRIMARY KEY AUTO_INCREMENT,
    DescripcionTipoFormacion VARCHAR(50) NOT NULL
);

-- Tabla de formación académica
CREATE TABLE FORMACION_ACADEMICA (
    PKForCod INT PRIMARY KEY AUTO_INCREMENT,
    FKPerCod INT NOT NULL,
    FKCodTipoFor INT NOT NULL,
    ForAca VARCHAR(200) NOT NULL,
    FechaInicio DATE,
    FechaFin DATE,
    Institucion VARCHAR(100),
    FOREIGN KEY (FKPerCod) REFERENCES PERSONA(PKPerCod) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (FKCodTipoFor) REFERENCES TIPO_FORMACION(PKCodTipoFor) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Tabla catálogo de tipos de habilidad
CREATE TABLE TIPO_HABILIDAD (
    PKCodTipoHab INT PRIMARY KEY AUTO_INCREMENT,
    DescripcionTipoHabilidad VARCHAR(50) NOT NULL
);

-- Tabla de habilidades
CREATE TABLE HABILIDAD (
    PKHabCod INT PRIMARY KEY AUTO_INCREMENT,
    FKPerCod INT NOT NULL,
    FKCodTipoHab INT NOT NULL,
    HabPer VARCHAR(100) NOT NULL,
    NivelHabilidad VARCHAR(20),
    FOREIGN KEY (FKPerCod) REFERENCES PERSONA(PKPerCod) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (FKCodTipoHab) REFERENCES TIPO_HABILIDAD(PKCodTipoHab) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Tabla de cursos y certificados
CREATE TABLE CURSO_CERTIFICADO (
    PKCurCod INT PRIMARY KEY AUTO_INCREMENT,
    FKPerCod INT NOT NULL,
    CurCer VARCHAR(200) NOT NULL,
    Institucion VARCHAR(100),
    FechaCertificacion DATE,
    FOREIGN KEY (FKPerCod) REFERENCES PERSONA(PKPerCod) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Tabla de certificaciones
CREATE TABLE CERTIFICACION (
    PKCertCod INT PRIMARY KEY AUTO_INCREMENT,
    FKPerCod INT NOT NULL,
    NombreCert VARCHAR(200) NOT NULL,
    InstitucionCert VARCHAR(100),
    FechaCertificacion DATE,
    FechaVencimiento DATE,
    FOREIGN KEY (FKPerCod) REFERENCES PERSONA(PKPerCod) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Tabla de información adicional
CREATE TABLE INFORMACION_ADICIONAL (
    PKInfoCod INT PRIMARY KEY AUTO_INCREMENT,
    FKPerCod INT NOT NULL,
    TipoInfo VARCHAR(50) NOT NULL,
    DetalleInfo TEXT,
    FOREIGN KEY (FKPerCod) REFERENCES PERSONA(PKPerCod) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Tabla de otros datos
CREATE TABLE OTRO_DATO (
    PKOtrCod INT PRIMARY KEY AUTO_INCREMENT,
    FKPerCod INT NOT NULL,
    OtrDat TEXT,
    FOREIGN KEY (FKPerCod) REFERENCES PERSONA(PKPerCod) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Insertar datos iniciales en catálogos
INSERT INTO CATALOGO_IDIOMAS (CodIdioma, NombreIdioma) VALUES 
('ES', 'Español'),
('EN', 'Inglés'),
('FR', 'Francés'),
('PT', 'Portugués'),
('DE', 'Alemán'),
('IT', 'Italiano');

INSERT INTO TIPO_FORMACION (DescripcionTipoFormacion) VALUES 
('Educación Básica'),
('Educación Media'),
('Técnico'),
('Tecnólogo'),
('Universitario'),
('Especialización'),
('Maestría'),
('Doctorado');

INSERT INTO TIPO_HABILIDAD (DescripcionTipoHabilidad) VALUES 
('Técnica'),
('Interpersonal'),
('Liderazgo'),
('Comunicación'),
('Analítica'),
('Creativa');

SELECT 'Base de datos creada exitosamente' as Mensaje;