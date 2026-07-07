<%-- 
    Document   : Producto
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
        <title>Productos</title>
    </head>
    <body>
        <div class="page-wrap">
            <div class="page-heading">
                <div>
                    <div class="section-title">Productos</div>
                    <div class="text-muted">Catalogo, precios, stock y disponibilidad</div>
                </div>
                <a class="btn btn-outline-dark" href="Controlador?menu=Producto&accion=Listar">Nuevo</a>
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
                        <div class="card-header">Producto</div>
                        <div class="card-body">
                            <form action="Controlador?menu=Producto" method="POST">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="idProducto" value="${productoForm.getId()}">
                                <div class="form-group">
                                    <label>Nombre</label>
                                    <input type="text" value="${productoForm.getNom()}" name="txtNombres" class="form-control" maxlength="120" required>
                                </div>
                                <div class="form-group">
                                    <label>Precio</label>
                                    <input type="number" value="${productoForm.getPre()}" name="txtPrecio" class="form-control" min="0" step="0.01" required>
                                </div>
                                <div class="form-group">
                                    <label>Stock</label>
                                    <input type="number" value="${productoForm.getStock()}" name="txtStock" class="form-control" min="0" required>
                                </div>
                                <div class="form-group">
                                    <label>Estado</label>
                                    <select name="txtEstado" class="form-control">
                                        <option value="Activo" ${productoForm.getEstado() eq 'Activo' ? 'selected' : ''}>Activo</option>
                                        <option value="Inactivo" ${productoForm.getEstado() eq 'Inactivo' ? 'selected' : ''}>Inactivo</option>
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
                                        <th>Producto</th>
                                        <th>Precio</th>
                                        <th>Stock</th>
                                        <th>Estado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="pr" items="${productos}">
                                        <tr>
                                            <td><c:out value="${pr.getId()}"/></td>
                                            <td><c:out value="${pr.getNom()}"/></td>
                                            <td>S/. <c:out value="${pr.getPre()}"/></td>
                                            <td><span class="${pr.getStock() le 5 ? 'badge-warn' : 'badge-soft'}"><c:out value="${pr.getStock()}"/></span></td>
                                            <td><span class="badge-soft"><c:out value="${pr.getEstado()}"/></span></td>
                                            <td class="table-actions">
                                                <a class="btn btn-sm btn-outline-dark" href="Controlador?menu=Producto&accion=Editar&id=${pr.getId()}">Editar</a>
                                                <form action="Controlador?menu=Producto" method="POST" onsubmit="return confirm('Desactivar este producto?');">
                                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                    <input type="hidden" name="accion" value="Delete">
                                                    <input type="hidden" name="id" value="${pr.getId()}">
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
