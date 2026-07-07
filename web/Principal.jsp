<%-- 
    Document   : Principal
    Created on : 19 may. 2024, 15:42:46
    Author     : Jheisson Loor
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <link rel="stylesheet" href="css/app.css">
        <title>Abarrotes La 31</title>
    </head>
    <body class="app-shell">
        <nav class="navbar navbar-expand-lg navbar-dark topbar">
            <a class="navbar-brand d-flex align-items-center" href="Controlador?menu=Principal">
                <span class="brand-mark mr-2" aria-hidden="true"></span>
                <span>Abarrotes La 31</span>
            </a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav nav-actions mr-auto">
                    <li class="nav-item">
                        <a class="btn btn-warning" href="Controlador?menu=NuevaVenta&accion=default" target="myFrame">Nueva venta</a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-outline-light" href="Controlador?menu=Ventas&accion=Listar" target="myFrame">Ventas</a>
                    </li>
                    <c:if test="${sessionScope.usuario.rol eq 'Administrador'}">
                        <li class="nav-item">
                            <a class="btn btn-outline-light" href="Controlador?menu=Cliente&accion=Listar" target="myFrame">Clientes</a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-outline-light" href="Controlador?menu=Producto&accion=Listar" target="myFrame">Productos</a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-outline-light" href="Controlador?menu=Empleado&accion=Listar" target="myFrame">Empleados</a>
                        </li>
                    </c:if>
                </ul>
                <div class="dropdown">
                    <button class="btn btn-outline-light dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown">
                        <c:out value="${sessionScope.usuario.getNom()}"/>
                    </button>
                    <div class="dropdown-menu dropdown-menu-right">
                        <span class="dropdown-item-text font-weight-bold"><c:out value="${sessionScope.usuario.getUser()}"/></span>
                        <span class="dropdown-item-text text-muted">Rol: <c:out value="${sessionScope.usuario.rol}"/></span>
                        <div class="dropdown-divider"></div>
                        <form action="Validar" method="POST">
                            <button name="accion" value="Salir" class="dropdown-item">Cerrar sesion</button>
                        </form>
                    </div>
                </div>
            </div>
        </nav>
        <main class="page-wrap shell-main">
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert"><c:out value="${error}"/></div>
            </c:if>
            <iframe name="myFrame" class="content-frame" src="Controlador?menu=Ventas&accion=Listar"></iframe>
        </main>
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
    </body>
</html>
