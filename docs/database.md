# Base De Datos

La base principal es `bd_ventas`. El script completo para crear tablas y datos
demo esta en `database/schema.sql`.

## Migraciones

Los scripts separados estan en `database/migrations/`:

- `001_create_schema.sql`: crea la base y tablas.
- `002_seed_demo_data.sql`: inserta usuarios, clientes y productos demo.
- `003_add_reporting_indexes.sql`: agrega indices para consultas y reportes.

## Entidades

```mermaid
erDiagram
    empleado ||--o{ ventas : registra
    cliente ||--o{ ventas : compra
    ventas ||--o{ detalle_ventas : contiene
    producto ||--o{ detalle_ventas : aparece

    empleado {
        int IdEmpleado PK
        string Dni
        string Nombres
        string User
        string PasswordHash
        string Rol
        string Estado
    }

    cliente {
        int IdCliente PK
        string Dni
        string Nombres
        string Direccion
        string Estado
    }

    producto {
        int IdProducto PK
        string Nombres
        decimal Precio
        int Stock
        string Estado
    }

    ventas {
        int IdVentas PK
        int IdCliente FK
        int IdEmpleado FK
        string NumeroSerie
        date FechaVentas
        decimal Monto
        string Estado
    }

    detalle_ventas {
        int IdDetalle PK
        int IdVentas FK
        int IdProducto FK
        int Cantidad
        decimal PrecioVenta
    }
```

## Indices

- `empleado(Estado, Rol)` para login y permisos.
- `cliente(Estado)` para ventas con clientes activos.
- `producto(Estado, Stock)` para busqueda y control de stock.
- `ventas(FechaVentas)` para filtros por fecha.
- `ventas(Estado)` para excluir ventas anuladas en reportes.
- `detalle_ventas(IdProducto)` para productos mas vendidos.

## Notas

- Las eliminaciones de empleados, clientes y productos se manejan como baja
  logica (`Estado='Inactivo'`) para no romper ventas historicas.
- La anulacion de ventas restaura stock en una transaccion.
- Los passwords demo estan hasheados con PBKDF2.
