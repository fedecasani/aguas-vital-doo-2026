INSERT OR IGNORE INTO cliente (
    tipo_documento, nro_documento, nombre, apellido, razon_social,
    direccion, telefono, barrio_id, activo
) VALUES
    ('DNI', '90000001', 'Victoria', 'Rossi', NULL, 'Domicilio de prueba 101', '3510000001', 1, 1),
    ('DNI', '90000002', 'Federico', 'Casani', NULL, 'Domicilio de prueba 202', '3510000002', 3, 1),
    ('DNI', '90000003', 'Yefim', 'Carivalli', NULL, 'Domicilio de prueba 303', '3510000003', 5, 1);
