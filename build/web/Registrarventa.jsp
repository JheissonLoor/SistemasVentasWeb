<%-- 
    Document   : Registrarventa
    Created on : 19 may. 2024, 21:20:17
    Author     : antho
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <title>JSP Page</title>
        <style>
            @media print{
                .parte1, .btn, .accion{
                    display: none;
                }
            }
        </style>
    </head>
    <body>
        <div class="d-flex">
            <div class="col sm-5 parte1">
                <div class="card">
                    <form action="Controlador?menu=NuevaVenta" method="POST">
                        <div class="card-body">
                            <div class="form-group">
                                <label>Datos del cliente</label>
                            </div>
                            <div class="form-group d-flex">
                                <div class="col-sm-6 d-flex">
                                    <input type="text" name="Codigocliente" value="${c.getDni()}" class="form-control" placeholder="Código">
                                    <input type="submit" name="accion" value="BuscarCliente" class="btn btn-outline-dark">
                                </div>
                                <div class="col-sm-6">
                                    <input type="text" name="nombrescliente" value="${c.getNom()}" placeholder="Datos Cliente" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Datos Producto</label>
                            </div>
                            <div class="form-group d-flex">
                                <div class="col-sm-6 d-flex">
                                    <input type="text" name="codigoproducto" value="${producto.getId()}" class="form-control" placeholder="codigo">
                                    <button type="submit" name="accion" value="BuscarProducto" class="btn btn-outline-dark">Buscar</button>
                                </div>
                                <div class="col-sm-6">
                                    <input type="text" name="nomproducto" value="${producto.getNom()}" placeholder="Datos Producto" class="form-control">
                                </div>
                            </div>
                            <div class="form-grou d-flex">
                                <div class="col-sm-6 d-flex">
                                    <input type="text" name="precio" value="${producto.getPre()}" class="form-control" placeholder="S/.00">
                                </div>
                                <div class="col-sm-3">
                                    <input type="number" value="1" name="cant" placeholder="" class="form-control">
                                </div>
                                <div class="col-sm-3">
                                    <input type="text" name="stock" placeholder="Stock" value="${producto.getStock()}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm">
                                    <button type="submit" name="accion" value="Agregar" class="btn btn-outline-dark">Agregar Producto</button>
                                </div>                                
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="col sm-8">
                <div class="card">
                    <div class="card-body">
                        <br>
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Nro</th>
                                    <th>Codigo</th>
                                    <th>Decripcion</th>
                                    <th>Precio</th>
                                    <th>Cantidad</th>
                                    <th>SubTotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="list" items="${lista}"> 
                                    <tr>
                                        <td>${list.getItem()}</td>
                                        <td>${list.getIdproducto()}</td>
                                        <td>${list.getDescripcionP()}</td>
                                        <td>${list.getPrecio()}</td>
                                        <td>${list.getCantidad()}</td>
                                        <td>${list.getSubtotal()}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>

                        </table>
                    </div>
                    <div class="card-footer d-flex">
                        <div class="colum-sm-6">
                            <a href="Controlador?menu=NuevaVenta&accion=GenerarVenta" onclick="print()" class="btn btn-dark">Generar Venta</a>
                        </div>
                        <div class="colum-sm-4 ml-auto">
                            <input type="text" name="txtTotal" value="S/. ${totalpagar}0" class="form-control">
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
