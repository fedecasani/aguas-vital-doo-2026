PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS pago;
DROP TABLE IF EXISTS detallepedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS vendedor;
DROP TABLE IF EXISTS barrio;
DROP TABLE IF EXISTS distribuidor;
DROP TABLE IF EXISTS zona;

CREATE TABLE zona (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE
);

CREATE TABLE distribuidor (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    legajo TEXT NOT NULL UNIQUE,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    capacidad_diaria INTEGER NOT NULL CHECK (capacidad_diaria > 0),
    zona_id INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (zona_id) REFERENCES zona(id)
);

CREATE TABLE barrio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE,
    zona_id INTEGER NOT NULL,
    FOREIGN KEY (zona_id) REFERENCES zona(id)
);

CREATE TABLE cliente (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo_documento TEXT NOT NULL,
    nro_documento TEXT NOT NULL UNIQUE,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    razon_social TEXT,
    direccion TEXT NOT NULL,
    telefono TEXT NOT NULL,
    barrio_id INTEGER NOT NULL,
    activo INTEGER NOT NULL DEFAULT 1 CHECK (activo IN (0, 1)),
    FOREIGN KEY (barrio_id) REFERENCES barrio(id)
);

CREATE TABLE producto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo TEXT NOT NULL UNIQUE,
    nombre TEXT NOT NULL,
    tipo TEXT NOT NULL CHECK (tipo IN ('AGUA_MINERAL', 'SODA')),
    capacidad_litros INTEGER NOT NULL CHECK (capacidad_litros > 0),
    precio REAL NOT NULL CHECK (precio >= 0),
    activo INTEGER NOT NULL DEFAULT 1 CHECK (activo IN (0, 1))
);

CREATE TABLE pedido (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nro INTEGER NOT NULL UNIQUE,
    fecha_solicitud TEXT NOT NULL,
    fecha_estimada TEXT NOT NULL,
    fecha_entrega TEXT,
    cliente_id INTEGER NOT NULL,
    distribuidor_id INTEGER NOT NULL,
    estado TEXT NOT NULL CHECK (estado IN ('PENDIENTE', 'ENTREGADO', 'CANCELADO')),
    observacion TEXT,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (distribuidor_id) REFERENCES distribuidor(id)
);

CREATE TABLE detallepedido (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pedido_id INTEGER NOT NULL,
    producto_id INTEGER NOT NULL,
    cantidad REAL NOT NULL CHECK (cantidad > 0),
    precio_venta REAL NOT NULL CHECK (precio_venta >= 0),
    UNIQUE (pedido_id, producto_id),
    FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

CREATE TABLE pago (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pedido_id INTEGER NOT NULL UNIQUE,
    fecha_hora TEXT NOT NULL,
    forma_pago TEXT NOT NULL CHECK (
        forma_pago IN ('EFECTIVO', 'TARJETA_DEBITO', 'TARJETA_CREDITO', 'TRANSFERENCIA')
    ),
    monto REAL NOT NULL CHECK (monto >= 0),
    FOREIGN KEY (pedido_id) REFERENCES pedido(id)
);

INSERT INTO zona (nombre) VALUES
    ('Zona Norte'),
    ('Zona Centro'),
    ('Zona Sur');

INSERT INTO distribuidor (legajo, nombre, apellido, capacidad_diaria, zona_id) VALUES
    ('DIST-001', 'Juan', 'Gomez', 8, 1),
    ('DIST-002', 'Mariana', 'Jerez', 10, 2),
    ('DIST-003', 'Lucas', 'Pereyra', 8, 3);

INSERT INTO barrio (nombre, zona_id) VALUES
    ('Cerro de las Rosas', 1),
    ('Alta Cordoba', 1),
    ('Centro', 2),
    ('Nueva Cordoba', 2),
    ('Jardin', 3),
    ('Villa El Libertador', 3);

INSERT INTO cliente (
    tipo_documento, nro_documento, nombre, apellido, razon_social,
    direccion, telefono, barrio_id, activo
) VALUES
    ('DNI', '20369875', 'Juan', 'Perez', NULL, 'Rafael Nunez 4200', '3515550101', 1, 1),
    ('DNI', '25687411', 'Mariana', 'Lopez', NULL, 'Colon 850', '3515550102', 3, 1),
    ('CUIT', '30711223344', 'Carlos', 'Diaz', 'Almacen Jardin', 'Richieri 2800', '3515550103', 5, 1);

INSERT INTO producto (codigo, nombre, tipo, capacidad_litros, precio, activo) VALUES
    ('AGUA-06', 'Bidon de agua mineral 6 litros', 'AGUA_MINERAL', 6, 2200, 1),
    ('AGUA-10', 'Bidon de agua mineral 10 litros', 'AGUA_MINERAL', 10, 3000, 1),
    ('AGUA-12', 'Bidon de agua mineral 12 litros', 'AGUA_MINERAL', 12, 3400, 1),
    ('AGUA-20', 'Bidon de agua mineral 20 litros', 'AGUA_MINERAL', 20, 4800, 1),
    ('SODA-01', 'Sifon de soda 1 litro', 'SODA', 1, 1200, 1);

PRAGMA foreign_keys = ON;
