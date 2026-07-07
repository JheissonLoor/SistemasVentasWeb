<%-- 
    Document   : Cliente
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
        <title>Clientes</title>
    </head>
    <body>
        <div class="page-wrap">
            <div class="page-heading">
                <div>
                    <div class="section-title">Clientes</div>
                    <div class="text-muted">Base de clientes para emision de ventas</div>
                </div>
                <a class="btn btn-outline-dark" href="Controlador?menu=Cliente&accion=Listar">Nuevo</a>
            </div>
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert"><c:out value="${error}"/></div>
            </c:if>
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success" role="alert"><c:out value="${mensaje}"/></div>
            </c:if>
            <div class="row">
                <div class="col-lg-4 mb-4">
                    <div class="card panel-card">
                        <div class="card-header">Cliente</div>
                        <div class="card-body">
                            <form action="Controlador?menu=Cliente" method="POST">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="idCliente" value="${cliente.getId()}">
                                <div class="form-group">
                                    <label>DNI</label>
                                    <input type="text" value="${cliente.getDni()}" name="txtDni" class="form-control" maxlength="20" required>
                                </div>
                                <div class="form-group">
                                    <label>Nombres</label>
                                    <input type="text" value="${cliente.getNom()}" name="txtNombres" class="form-control" maxlength="120" required>
                                </div>
                                <div class="form-group">
                                    <label>Direccion</label>
                                    <input type="text" value="${cliente.getDir()}" name="txtDireccion" class="form-control" maxlength="180">
                                </div>
                                <div class="form-group">
                                    <label>Estado</label>
                                    <select name="txtEstado" class="form-control">
                                        <option value="Activo" ${cliente.getEstado() eq 'Activo' ? 'selected' : ''}>Activo</option>
                                        <option value="Inactivo" ${cliente.getEstado() eq 'Inactivo' ? 'selected' : ''}>Inactivo</option>
                                    </select>
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
                                        <th>Direccion</th>
                                        <th>Estado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="cl" items="${clientes}">
                                        <tr>
                                            <td><c:out value="${cl.getId()}"/></td>
                                            <td><c:out value="${cl.getDni()}"/></td>
                                            <td><c:out value="${cl.getNom()}"/></td>
                                            <td><c:out value="${cl.getDir()}"/></td>
                                            <td><span class="badge-soft"><c:out value="${cl.getEstado()}"/></span></td>
                                            <td class="table-actions">
                                                <a class="btn btn-sm btn-outline-dark" href="Controlador?menu=Cliente&accion=Editar&id=${cl.getId()}">Editar</a>
                                                <form action="Controlador?menu=Cliente" method="POST" onsubmit="return confirm('Desactivar este cliente?');">
                                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                    <input type="hidden" name="accion" value="Delete">
                                                    <input type="hidden" name="id" value="${cl.getId()}">
                                                    <button type="submit" class="btn btn-sm btn-outline-danger">Desactivar</button>
                                                </form>
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
