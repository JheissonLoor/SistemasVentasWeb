<%-- 
    Document   : index
    Created on : 19 may. 2024, 15:41:47
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
    <body class="login-page">
        <div class="card login-card">
            <div class="card-body p-4 p-md-5">
                <form class="form-sign" action="Validar" method="POST">
                    <div class="form-group text-center mb-4">
                        <span class="brand-logo mb-3" role="img" aria-label="Logo La 31"></span>
                        <h3 class="mb-1">Abarrotes La 31</h3>
                        <p class="text-muted mb-0">Sistema de ventas</p>
                    </div>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">${error}</div>
                    </c:if>
                    <div class="form-group">
                        <label>Usuario</label>
                        <input type="text" name="txtuser" class="form-control" autocomplete="username" required>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="txtpass" class="form-control" autocomplete="current-password" required>
                    </div>
                    <button type="submit" name="accion" value="Ingresar" class="btn btn-warning btn-block">Ingresar</button>
                </form>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
    </body>
</html>
