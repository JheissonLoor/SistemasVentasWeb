package Controlador;

import Modelo.Empleado;
import Modelo.EmpleadoDAO;
import config.DatabaseException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Validar extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Validar.class.getName());
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cerrarSesion(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("Salir".equalsIgnoreCase(accion)) {
            cerrarSesion(request, response);
            return;
        }
        if (!"Ingresar".equalsIgnoreCase(accion)) {
            response.sendRedirect("index.jsp");
            return;
        }

        String user = request.getParameter("txtuser");
        String password = request.getParameter("txtpass");
        try {
            Empleado empleado = empleadoDAO.Validar(user, password);
            if (empleado.getUser() != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", empleado);
                response.sendRedirect("Controlador?menu=Principal");
            } else {
                request.setAttribute("error", "Usuario o password incorrectos.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (DatabaseException e) {
            LOGGER.log(Level.SEVERE, "Error al iniciar sesion", e);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("index.jsp");
    }
}
