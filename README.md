# SistemasVentasWeb

Sistema web universitario para gestionar ventas de una tienda de abarrotes. El proyecto fue desarrollado como práctica académica con Java Web clásico y ha sido ordenado para que pueda ejecutarse, revisarse y mejorarse como repositorio de portafolio.

## De Qué Trata

La aplicación modela un punto de venta básico para **Abarrotes La 31**. Permite iniciar sesión con empleados, gestionar usuarios internos y registrar ventas con cliente, productos, detalle de venta y descuento de stock.

## Funcionalidades

- Login con usuarios de la tabla `empleado`.
- Contraseñas almacenadas con hash PBKDF2.
- Roles de acceso:
  - `Administrador`: puede gestionar empleados y registrar ventas.
  - `Vendedor`: puede registrar ventas, pero no administrar empleados.
- CRUD básico de empleados.
- Búsqueda de cliente por DNI.
- Búsqueda de producto por código.
- Carrito de venta por sesión.
- Generación de venta con detalle y actualización de stock.
- Base de datos reproducible con datos demo.

## Stack

- Java Web: Servlets + JSP
- JDBC
- MySQL/MariaDB
- JSTL
- Bootstrap 4
- NetBeans / Ant
- Apache Tomcat 9

## Base De Datos

El esquema reproducible está en:

```text
database/schema.sql
```

En el entorno local usado para revisar este repo se levantó MariaDB de XAMPP en el puerto `3307`.

```bash
mysql -h 127.0.0.1 -P 3307 -u root < database/schema.sql
```

Configuración por defecto:

```text
Host: 127.0.0.1
Puerto: 3307
Base: bd_ventas
Usuario: root
Password: vacío
```

La conexión también puede configurarse con variables o propiedades Java:

- `DB_URL`
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`

## Credenciales Demo

Administrador:

```text
usuario: admin
password: admin123
```

Vendedor:

```text
usuario: empleado
password: empleado123
```

## Datos Demo

- Cliente: `12345678`
- Productos: `1`, `2`, `3`, `4`

## Ejecución Local

1. Levantar MySQL/MariaDB.
2. Importar `database/schema.sql`.
3. Abrir el proyecto en NetBeans o desplegar `web/` en Tomcat 9.
4. Abrir:

```text
http://127.0.0.1:8081/SistemasVentasWeb/
```

## Mejoras Ya Aplicadas

- Se dejó de usar el DNI como contraseña.
- Se agregó hashing de passwords con PBKDF2.
- Se corrigió el login del usuario vendedor demo.
- Se agregó control de roles en servidor.
- Se eliminaron consultas SQL concatenadas en DAOs principales.
- Se reemplazaron errores silenciosos por excepciones con mensaje.
- Se corrigió el flujo de venta para guardar cabecera, detalle y descontar stock en una transacción.
- Se movió el carrito de venta a la sesión para evitar mezcla de datos entre usuarios.
- Se agregó un esquema SQL reproducible.
- Se removió dependencia de rutas locales antiguas de NetBeans.

## Próximas Mejoras Recomendadas

- Migrar a Maven o Gradle.
- Agregar CRUD completo de clientes y productos.
- Agregar listado y anulación de ventas.
- Agregar filtros/reportes por fecha.
- Agregar pruebas automatizadas.
- Migrar a Spring Boot, Spring Security y JPA como versión 2.0.

## Nota

Este es un proyecto universitario antiguo modernizado gradualmente. La intención actual es conservar su valor académico, hacerlo ejecutable y prepararlo para futuras mejoras sin perder de vista su origen.
