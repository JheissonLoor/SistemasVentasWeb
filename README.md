# SistemasVentasWeb

Sistema web universitario para gestionar ventas de una tienda de abarrotes.
El proyecto fue desarrollado como practica academica con Java Web clasico y
ordenado como repositorio de portafolio para poder ejecutarlo, revisarlo y
seguir mejorandolo.

**Autor:** Jheisson Loor

## De Que Trata

La aplicacion modela un punto de venta basico para **Abarrotes La 31**. Permite
iniciar sesion con empleados, gestionar usuarios internos y registrar ventas con
cliente, productos, detalle de comprobante y descuento de stock.

## Funcionalidades

- Login con usuarios registrados en la tabla `empleado`.
- Passwords almacenados con hash PBKDF2.
- Roles de acceso:
  - `Administrador`: gestiona empleados y registra ventas.
  - `Vendedor`: registra ventas sin acceso a la administracion de empleados.
- CRUD basico de empleados.
- Busqueda de cliente por DNI.
- Busqueda de producto por codigo.
- Carrito de venta por sesion.
- Generacion de venta con detalle y actualizacion de stock.
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

El esquema reproducible esta en:

```text
database/schema.sql
```

En XAMPP, MariaDB normalmente puede quedar en el puerto `3307` si el puerto
`3306` ya esta ocupado por otro MySQL local.

```bash
mysql -h 127.0.0.1 -P 3307 -u root < database/schema.sql
```

Configuracion por defecto:

```text
Host: 127.0.0.1
Puerto: 3307
Base: bd_ventas
Usuario: root
Password: vacio
```

La conexion tambien puede configurarse con variables de entorno o propiedades
Java:

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

## Ejecucion Local

1. Levantar MySQL/MariaDB.
2. Importar `database/schema.sql`.
3. Abrir el proyecto en NetBeans o desplegar `web/` en Tomcat 9.
4. Abrir la aplicacion:

```text
http://127.0.0.1:8081/SistemasVentasWeb/
```

## Mejoras Aplicadas

- Se dejo de usar el DNI como password.
- Se agrego hashing de passwords con PBKDF2.
- Se corrigio el login del usuario vendedor demo.
- Se agrego control de roles en servidor.
- Se eliminaron consultas SQL concatenadas en DAOs principales.
- Se reemplazaron errores silenciosos por excepciones con mensaje.
- Se corrigio el flujo de venta para guardar cabecera, detalle y descontar stock en una transaccion.
- Se movio el carrito de venta a la sesion para evitar mezcla de datos entre usuarios.
- Se agrego un esquema SQL reproducible.
- Se removio dependencia de rutas locales antiguas de NetBeans.
- Se actualizo la interfaz de login, menu principal, empleados y registro de ventas.

## Proximas Mejoras Recomendadas

- Migrar a Maven o Gradle.
- Agregar CRUD completo de clientes y productos.
- Agregar listado y anulacion de ventas.
- Agregar filtros y reportes por fecha.
- Agregar pruebas automatizadas.
- Migrar a Spring Boot, Spring Security y JPA como version 2.0.

## Nota

Este es un proyecto universitario antiguo modernizado gradualmente. La intencion
actual es conservar su valor academico, dejarlo ejecutable y prepararlo para
futuras mejoras sin perder de vista su origen.
