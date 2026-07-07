USE bd_ventas;

INSERT INTO empleado (IdEmpleado, Dni, Nombres, Telefono, Estado, User, PasswordHash, Rol)
VALUES
  (1, '00000000', 'Administrador Demo', '999999999', 'Activo', 'admin',
   'pbkdf2_sha256$120000$U2lzdGVtYXNWZW50YXNBZG1pbg==$oWhqq3OnOqBlKyucVhnwDQUWPFRGFcxl+XmjokNw8U4=', 'Administrador'),
  (2, '11111111', 'Empleado Demo', '988888888', 'Activo', 'empleado',
   'pbkdf2_sha256$120000$w9IGOJbuMPnxe5obP3rSPA==$j5wXYs0CMw0o//hiKAz4anBeH6MlKsZsvFhOfXy3/SQ=', 'Vendedor')
ON DUPLICATE KEY UPDATE
  Nombres = VALUES(Nombres),
  Telefono = VALUES(Telefono),
  Estado = VALUES(Estado),
  PasswordHash = VALUES(PasswordHash),
  Rol = VALUES(Rol);

INSERT INTO cliente (IdCliente, Dni, Nombres, Direccion, Estado)
VALUES
  (1, '12345678', 'Cliente Demo', 'Av. Peru 123', 'Activo'),
  (2, '87654321', 'Cliente Mayorista', 'Jr. Comercio 456', 'Activo')
ON DUPLICATE KEY UPDATE
  Nombres = VALUES(Nombres),
  Direccion = VALUES(Direccion),
  Estado = VALUES(Estado);

INSERT INTO producto (IdProducto, Nombres, Precio, Stock, Estado)
VALUES
  (1, 'Arroz Costeno 1kg', 4.50, 50, 'Activo'),
  (2, 'Aceite Vegetal 1L', 8.90, 35, 'Activo'),
  (3, 'Azucar Rubia 1kg', 3.80, 40, 'Activo'),
  (4, 'Leche Evaporada', 4.20, 60, 'Activo')
ON DUPLICATE KEY UPDATE
  Nombres = VALUES(Nombres),
  Precio = VALUES(Precio),
  Stock = VALUES(Stock),
  Estado = VALUES(Estado);
