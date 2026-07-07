<%-- 
    Document   : Registrarventa
    Created on : 19 may. 2024, 21:20:17
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
        <title>Nueva venta</title>
        <style>
            @media print {
                .parte1, .btn, .accion {
                    display: none;
                }
            }
        </style>
    </head>
    <body>
        <div class="page-wrap">
            <div class="page-heading">
                <div>
                    <div class="section-title">Nueva venta</div>
                    <div class="text-muted">Cliente, productos y detalle de comprobante</div>
                </div>
                <span class="metric-pill">Total: S/. <c:out value="${totalpagar}"/></span>
            </div>
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert"><c:out value="${error}"/></div>
            </c:if>
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success" role="alert"><c:out value="${mensaje}"/></div>
            </c:if>
            <div class="row">
                <div class="col-lg-5 parte1 mb-4">
                    <div class="card panel-card mb-4">
                        <div class="card-header">Cliente</div>
                        <div class="card-body">
                            <form action="Controlador?menu=NuevaVenta" method="POST">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <div class="form-row">
                                    <div class="form-group col-md-7">
                                        <label>DNI</label>
                                        <input type="text" name="Codigocliente" value="${c.getDni()}" class="form-control" placeholder="12345678" maxlength="20" required>
                                    </div>
                                    <div class="form-group col-md-5 d-flex align-items-end">
                                        <button type="submit" name="accion" value="BuscarCliente" class="btn btn-outline-dark btn-block">Buscar</button>
                                    </div>
                                </div>
                                <div class="form-group mb-0">
                                    <label>Nombre</label>
                                    <input type="text" name="nombrescliente" value="${c.getNom()}" class="form-control" readonly>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="card panel-card">
                        <div class="card-header">Producto</div>
                        <div class="card-body">
                            <form action="Controlador?menu=NuevaVenta" method="POST">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <div class="form-row">
                                    <div class="form-group col-md-7">
                                        <label>Codigo</label>
                                        <input type="number" name="codigoproducto" value="${producto.getId()}" class="form-control" placeholder="1" min="1" required>
                                    </div>
                                    <div class="form-group col-md-5 d-flex align-items-end">
                                        <button type="submit" name="accion" value="BuscarProducto" class="btn btn-outline-dark btn-block">Buscar</button>
                                    </div>
                                </div>
                            </form>
                            <form action="Controlador?menu=NuevaVenta" method="POST">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="idProducto" value="${producto.getId()}">
                                <div class="form-group">
                                    <label>Producto</label>
                                    <input type="text" name="nomproducto" value="${producto.getNom()}" class="form-control" readonly>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Precio</label>
                                        <input type="text" name="precio" value="${producto.getPre()}" class="form-control" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Cantidad</label>
                                        <input type="number" value="1" min="1" name="cant" class="form-control" required>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Stock</label>
                                        <input type="text" name="stock" value="${producto.getStock()}" class="form-control" readonly>
                                    </div>
                                </div>
                                <button type="submit" name="accion" value="Agregar" class="btn btn-dark btn-block">Agregar producto</button>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-lg-7">
                    <div class="card panel-card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <span>Detalle de venta</span>
                            <span class="badge-soft">Items: ${lista.size()}</span>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead>
                                    <tr>
                                        <th>Nro</th>
                                        <th>Codigo</th>
                                        <th>Descripcion</th>
                                        <th>Precio</th>
                                        <th>Cantidad</th>
                                        <th>Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="list" items="${lista}">
                                        <tr>
                                            <td><c:out value="${list.getItem()}"/></td>
                                            <td><c:out value="${list.getIdproducto()}"/></td>
                                            <td><c:out value="${list.getDescripcionP()}"/></td>
                                            <td>S/. <c:out value="${list.getPrecio()}"/></td>
                                            <td><c:out value="${list.getCantidad()}"/></td>
                                            <td>S/. <c:out value="${list.getSubtotal()}"/></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty lista}">
                                        <tr>
                                            <td colspan="6" class="text-center text-muted py-4">Agrega productos para construir la venta.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                        <div class="card-footer d-flex justify-content-between align-items-center flex-wrap">
                            <div class="mb-2 mb-md-0">
                                <form action="Controlador?menu=NuevaVenta" method="POST" class="d-inline" onsubmit="return confirm('Generar esta venta?');">
                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                    <button type="submit" name="accion" value="GenerarVenta" class="btn btn-dark">Generar venta</button>
                                </form>
                                <form action="Controlador?menu=NuevaVenta" method="POST" class="d-inline">
                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                    <button type="submit" name="accion" value="Limpiar" class="btn btn-outline-secondary">Limpiar</button>
                                </form>
                            </div>
                            <input type="text" name="txtTotal" value="S/. ${totalpagar}" class="form-control total-box" readonly>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
    </body>
</html>
