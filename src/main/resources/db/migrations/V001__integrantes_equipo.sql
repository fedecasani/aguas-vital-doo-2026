CREATE TABLE IF NOT EXISTS integrante_equipo (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    UNIQUE (nombre, apellido)
);

INSERT OR IGNORE INTO integrante_equipo (nombre, apellido) VALUES
    ('Victoria', 'Rossi'),
    ('Federico', 'Casani'),
    ('Yefim', 'Carivalli');
