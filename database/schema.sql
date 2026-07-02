CREATE DATABASE IF NOT EXISTS bd_ventas
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE bd_ventas;

CREATE TABLE IF NOT EXISTS empleado (
  IdEmpleado INT AUTO_INCREMENT PRIMARY KEY,
  Dni VARCHAR(20) NOT NULL UNIQUE,
  Nombres VARCHAR(120) NOT NULL,
  Telefono VARCHAR(30) NULL,
  Estado VARCHAR(20) NOT NULL DEFAULT 'Activo',
  User VARCHAR(50) NOT NULL UNIQUE,
  PasswordHash VARCHAR(255) NOT NULL,
  Rol VARCHAR(30) NOT NULL DEFAULT 'Vendedor'
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS cliente (
  IdCliente INT AUTO_INCREMENT PRIMARY KEY,
  Dni VARCHAR(20) NOT NULL UNIQUE,
  Nombres VARCHAR(120) NOT NULL,
  Direccion VARCHAR(180) NULL,
  Estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS producto (
  IdProducto INT AUTO_INCREMENT PRIMARY KEY,
  Nombres VARCHAR(120) NOT NULL,
  Precio DECIMAL(10,2) NOT NULL,
  Stock INT NOT NULL DEFAULT 0,
  Estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ventas (
  IdVentas INT AUTO_INCREMENT PRIMARY KEY,
  IdCliente INT NOT NULL,
  IdEmpleado INT NOT NULL,
  NumeroSerie VARCHAR(30) NOT NULL,
  FechaVentas DATE NOT NULL,
  Monto DECIMAL(10,2) NOT NULL,
  Estado VARCHAR(20) NOT NULL DEFAULT 'Emitida',
  CONSTRAINT fk_ventas_cliente FOREIGN KEY (IdCliente) REFERENCES cliente(IdCliente),
  CONSTRAINT fk_ventas_empleado FOREIGN KEY (IdEmpleado) REFERENCES empleado(IdEmpleado)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS detalle_ventas (
  IdDetalle INT AUTO_INCREMENT PRIMARY KEY,
  IdVentas INT NOT NULL,
  IdProducto INT NOT NULL,
  Cantidad INT NOT NULL,
  PrecioVenta DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_detalle_ventas FOREIGN KEY (IdVentas) REFERENCES ventas(IdVentas),
  CONSTRAINT fk_detalle_producto FOREIGN KEY (IdProducto) REFERENCES producto(IdProducto)
) ENGINE=InnoDB;

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
