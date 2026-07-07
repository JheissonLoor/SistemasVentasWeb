USE bd_ventas;

DELIMITER $$

CREATE PROCEDURE add_index_if_missing(
  IN p_table_name VARCHAR(64),
  IN p_index_name VARCHAR(64),
  IN p_sql TEXT
)
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = p_table_name
      AND index_name = p_index_name
  ) THEN
    SET @ddl = p_sql;
    PREPARE stmt FROM @ddl;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END$$

DELIMITER ;

CALL add_index_if_missing('empleado', 'idx_empleado_estado_rol', 'ALTER TABLE empleado ADD INDEX idx_empleado_estado_rol (Estado, Rol)');
CALL add_index_if_missing('cliente', 'idx_cliente_estado', 'ALTER TABLE cliente ADD INDEX idx_cliente_estado (Estado)');
CALL add_index_if_missing('producto', 'idx_producto_estado_stock', 'ALTER TABLE producto ADD INDEX idx_producto_estado_stock (Estado, Stock)');
CALL add_index_if_missing('ventas', 'idx_ventas_fecha', 'ALTER TABLE ventas ADD INDEX idx_ventas_fecha (FechaVentas)');
CALL add_index_if_missing('ventas', 'idx_ventas_estado', 'ALTER TABLE ventas ADD INDEX idx_ventas_estado (Estado)');
CALL add_index_if_missing('ventas', 'idx_ventas_cliente', 'ALTER TABLE ventas ADD INDEX idx_ventas_cliente (IdCliente)');
CALL add_index_if_missing('ventas', 'idx_ventas_empleado', 'ALTER TABLE ventas ADD INDEX idx_ventas_empleado (IdEmpleado)');
CALL add_index_if_missing('detalle_ventas', 'idx_detalle_producto', 'ALTER TABLE detalle_ventas ADD INDEX idx_detalle_producto (IdProducto)');
CALL add_index_if_missing('detalle_ventas', 'idx_detalle_ventas_producto', 'ALTER TABLE detalle_ventas ADD INDEX idx_detalle_ventas_producto (IdVentas, IdProducto)');

DROP PROCEDURE add_index_if_missing;
