<%-- 
    Document   : Ventas
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
        <title>Ventas</title>
    </head>
    <body>
        <div class="page-wrap">
            <div class="page-heading">
                <div>
                    <div class="section-title">Ventas y reportes</div>
                    <div class="text-muted">Seguimiento de comprobantes, montos y productos mas vendidos</div>
                </div>
                <a class="btn btn-dark" href="Controlador?menu=NuevaVenta&accion=default">Nueva venta</a>
            </div>
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert"><c:out value="${error}"/></div>
            </c:if>
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success" role="alert"><c:out value="${mensaje}"/></div>
            </c:if>
            <div class="metric-grid mb-4">
                <div class="metric-card">
                    <span>Ventas registradas</span>
                    <strong><c:out value="${totalVentas}"/></strong>
                </div>
                <div class="metric-card">
                    <span>Monto emitido</span>
                    <strong>S/. <c:out value="${montoTotal}"/></strong>
                </div>
                <div class="metric-card">
                    <span>Periodo</span>
                    <strong><c:out value="${empty fechaInicio ? 'Todo' : fechaInicio}"/> / <c:out value="${empty fechaFin ? 'Todo' : fechaFin}"/></strong>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-8 mb-4">
                    <div class="card panel-card">
                        <div class="card-header">
                            <form class="form-inline report-filter" action="Controlador" method="GET">
                                <input type="hidden" name="menu" value="Ventas">
                                <input type="hidden" name="accion" value="Listar">
                                <label class="mr-2">Desde</label>
                                <input type="date" name="fechaInicio" value="${fechaInicio}" class="form-control mr-2 mb-2 mb-md-0">
                                <label class="mr-2">Hasta</label>
                                <input type="date" name="fechaFin" value="${fechaFin}" class="form-control mr-2 mb-2 mb-md-0">
                                <button class="btn btn-outline-dark" type="submit">Filtrar</button>
                            </form>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Serie</th>
                                        <th>Fecha</th>
                                        <th>Cliente</th>
                                        <th>Empleado</th>
                                        <th>Monto</th>
                                        <th>Estado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="v" items="${ventas}">
                                        <tr>
                                            <td><c:out value="${v.getId()}"/></td>
                                            <td><c:out value="${v.getNumserie()}"/></td>
                                            <td><c:out value="${v.getFecha()}"/></td>
                                            <td><c:out value="${v.getClienteNombre()}"/></td>
                                            <td><c:out value="${v.getEmpleadoNombre()}"/></td>
                                            <td>S/. <c:out value="${v.getMonto()}"/></td>
                                            <td>
                                                <span class="${v.getEstado() eq 'Anulada' ? 'badge-danger-soft' : 'badge-soft'}">
                                                    <c:out value="${v.getEstado()}"/>
                                                </span>
                                            </td>
                                            <td>
                                                <c:if test="${sessionScope.usuario.rol eq 'Administrador' and v.getEstado() ne 'Anulada'}">
                                                    <form action="Controlador?menu=Ventas" method="POST" onsubmit="return confirm('Anular esta venta y restaurar stock?');">
                                                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                        <input type="hidden" name="accion" value="Anular">
                                                        <input type="hidden" name="id" value="${v.getId()}">
                                                        <button type="submit" class="btn btn-sm btn-outline-danger">Anular</button>
                                                    </form>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty ventas}">
                                        <tr>
                                            <td colspan="8" class="text-center text-muted py-4">No hay ventas para el periodo seleccionado.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4 mb-4">
                    <div class="card panel-card">
                        <div class="card-header">Productos mas vendidos</div>
                        <div class="list-group list-group-flush">
                            <c:forEach var="p" items="${productosMasVendidos}">
                                <div class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <div class="font-weight-bold"><c:out value="${p.getDescripcionP()}"/></div>
                                        <div class="text-muted">S/. <c:out value="${p.getSubtotal()}"/></div>
                                    </div>
                                    <span class="badge-soft"><c:out value="${p.getCantidad()}"/></span>
                                </div>
                            </c:forEach>
                            <c:if test="${empty productosMasVendidos}">
                                <div class="list-group-item text-muted">Sin productos vendidos en este periodo.</div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
