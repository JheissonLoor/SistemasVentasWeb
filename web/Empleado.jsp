<%-- 
    Document   : Empleado
    Created on : 19 may. 2024, 21:20:09
    Author     : Jheisson Loor
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <link rel="stylesheet" href="css/app.css">
        <title>Empleados</title>
    </head>
    <body>
        <div class="page-wrap">
            <div class="d-flex align-items-center justify-content-between mb-3">
                <div>
                    <div class="section-title">Gestion de empleados</div>
                    <div class="text-muted">Usuarios internos y permisos del sistema</div>
                </div>
            </div>
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">${error}</div>
            </c:if>
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success" role="alert">${mensaje}</div>
            </c:if>
            <div class="row">
                <div class="col-lg-4 mb-4">
                    <div class="card panel-card">
                        <div class="card-header">Empleado</div>
                        <div class="card-body">
                            <form action="Controlador?menu=Empleado" method="POST">
                                <input type="hidden" name="idEmpleado" value="${empleado.getId()}">
                                <div class="form-group">
                                    <label>DNI</label>
                                    <input type="text" value="${empleado.getDni()}" name="txtDni" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label>Nombres</label>
                                    <input type="text" value="${empleado.getNom()}" name="txtNombres" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label>Telefono</label>
                                    <input type="text" value="${empleado.getTel()}" name="txtTel" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label>Estado</label>
                                    <input type="text" value="${empty empleado.getEstado() ? 'Activo' : empleado.getEstado()}" name="txtEstado" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label>Usuario</label>
                                    <input type="text" value="${empleado.getUser()}" name="txtUser" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label>Password</label>
                                    <input type="password" name="txtPassword" class="form-control" placeholder="Obligatorio al crear">
                                </div>
                                <div class="form-group">
                                    <label>Rol</label>
                                    <input type="text" value="${empty empleado.getRol() ? 'Vendedor' : empleado.getRol()}" name="txtRol" class="form-control">
                                </div>
                                <div class="d-flex">
                                    <button type="submit" name="accion" value="Agregar" class="btn btn-dark mr-2">Agregar</button>
                                    <button type="submit" name="accion" value="Actualizar" class="btn btn-outline-dark">Actualizar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-lg-8">
                    <div class="card panel-card">
                        <div class="card-header">Listado</div>
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>DNI</th>
                                        <th>Nombres</th>
                                        <th>Telefono</th>
                                        <th>Estado</th>
                                        <th>Usuario</th>
                                        <th>Rol</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="em" items="${empleados}">
                                        <tr>
                                            <td>${em.getId()}</td>
                                            <td>${em.getDni()}</td>
                                            <td>${em.getNom()}</td>
                                            <td>${em.getTel()}</td>
                                            <td><span class="badge-soft">${em.getEstado()}</span></td>
                                            <td>${em.getUser()}</td>
                                            <td>${em.getRol()}</td>
                                            <td>
                                                <a class="btn btn-sm btn-outline-dark" href="Controlador?menu=Empleado&accion=Editar&id=${em.getId()}">Editar</a>
                                                <a class="btn btn-sm btn-outline-danger" href="Controlador?menu=Empleado&accion=Delete&id=${em.getId()}">Eliminar</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
