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
  Rol VARCHAR(30) NOT NULL DEFAULT 'Vendedor',
  KEY idx_empleado_estado_rol (Estado, Rol)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS cliente (
  IdCliente INT AUTO_INCREMENT PRIMARY KEY,
  Dni VARCHAR(20) NOT NULL UNIQUE,
  Nombres VARCHAR(120) NOT NULL,
  Direccion VARCHAR(180) NULL,
  Estado VARCHAR(20) NOT NULL DEFAULT 'Activo',
  KEY idx_cliente_estado (Estado)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS producto (
  IdProducto INT AUTO_INCREMENT PRIMARY KEY,
  Nombres VARCHAR(120) NOT NULL,
  Precio DECIMAL(10,2) NOT NULL,
  Stock INT NOT NULL DEFAULT 0,
  Estado VARCHAR(20) NOT NULL DEFAULT 'Activo',
  KEY idx_producto_estado_stock (Estado, Stock)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ventas (
  IdVentas INT AUTO_INCREMENT PRIMARY KEY,
  IdCliente INT NOT NULL,
  IdEmpleado INT NOT NULL,
  NumeroSerie VARCHAR(30) NOT NULL,
  FechaVentas DATE NOT NULL,
  Monto DECIMAL(10,2) NOT NULL,
  Estado VARCHAR(20) NOT NULL DEFAULT 'Emitida',
  KEY idx_ventas_fecha (FechaVentas),
  KEY idx_ventas_estado (Estado),
  KEY idx_ventas_cliente (IdCliente),
  KEY idx_ventas_empleado (IdEmpleado),
  CONSTRAINT fk_ventas_cliente FOREIGN KEY (IdCliente) REFERENCES cliente(IdCliente),
  CONSTRAINT fk_ventas_empleado FOREIGN KEY (IdEmpleado) REFERENCES empleado(IdEmpleado)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS detalle_ventas (
  IdDetalle INT AUTO_INCREMENT PRIMARY KEY,
  IdVentas INT NOT NULL,
  IdProducto INT NOT NULL,
  Cantidad INT NOT NULL,
  PrecioVenta DECIMAL(10,2) NOT NULL,
  KEY idx_detalle_producto (IdProducto),
  KEY idx_detalle_ventas_producto (IdVentas, IdProducto),
  CONSTRAINT fk_detalle_ventas FOREIGN KEY (IdVentas) REFERENCES ventas(IdVentas),
  CONSTRAINT fk_detalle_producto FOREIGN KEY (IdProducto) REFERENCES producto(IdProducto)
) ENGINE=InnoDB;
