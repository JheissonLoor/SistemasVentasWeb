package Controlador;

import Modelo.Cliente;
import Modelo.ClienteDAO;
import Modelo.Empleado;
import Modelo.EmpleadoDAO;
import Modelo.Producto;
import Modelo.ProductoDAO;
import Modelo.Venta;
import Modelo.VentaDAO;
import config.DatabaseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Controlador extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Controlador.class.getName());

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final VentaDAO ventaDAO = new VentaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Empleado usuario = session == null ? null : (Empleado) session.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String menu = valueOrDefault(request.getParameter("menu"), "Principal");
        String accion = valueOrDefault(request.getParameter("accion"), "default");

        try {
            switch (menu) {
                case "Principal":
                    request.getRequestDispatcher("Principal.jsp").forward(request, response);
                    break;
                case "Empleado":
                    if (!esAdministrador(usuario)) {
                        request.setAttribute("error", "No tienes permiso para gestionar empleados.");
                        request.getRequestDispatcher("Principal.jsp").forward(request, response);
                        return;
                    }
                    procesarEmpleado(request, response, accion);
                    break;
                case "NuevaVenta":
                    procesarVenta(request, response, session, usuario, accion);
                    break;
                default:
                    request.setAttribute("error", "Menu no reconocido: " + menu);
                    request.getRequestDispatcher("Principal.jsp").forward(request, response);
                    break;
            }
        } catch (DatabaseException | IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Error procesando menu " + menu + " accion " + accion, e);
            request.setAttribute("error", e.getMessage());
            if ("NuevaVenta".equals(menu)) {
                prepararVenta(request, session);
                request.getRequestDispatcher("Registrarventa.jsp").forward(request, response);
            } else if ("Empleado".equals(menu) && esAdministrador(usuario)) {
                request.setAttribute("empleados", empleadoDAO.listar());
                request.getRequestDispatcher("Empleado.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("Principal.jsp").forward(request, response);
            }
        }
    }

    private void procesarEmpleado(HttpServletRequest request, HttpServletResponse response, String accion)
            throws ServletException, IOException {
        switch (accion) {
            case "Agregar":
                Empleado nuevo = leerEmpleado(request);
                String password = request.getParameter("txtPassword");
                if (password == null || password.trim().isEmpty()) {
                    throw new IllegalArgumentException("El password es obligatorio para crear un empleado.");
                }
                empleadoDAO.agregar(nuevo, password);
                request.setAttribute("mensaje", "Empleado creado correctamente.");
                break;
            case "Editar":
                int idEditar = parseInt(request.getParameter("id"), "ID de empleado invalido.");
                request.setAttribute("empleado", empleadoDAO.listarId(idEditar));
                break;
            case "Actualizar":
                Empleado actualizado = leerEmpleado(request);
                actualizado.setId(parseInt(request.getParameter("idEmpleado"), "ID de empleado invalido."));
                empleadoDAO.actualizar(actualizado, request.getParameter("txtPassword"));
                request.setAttribute("mensaje", "Empleado actualizado correctamente.");
                break;
            case "Delete":
                int idEliminar = parseInt(request.getParameter("id"), "ID de empleado invalido.");
                empleadoDAO.delete(idEliminar);
                request.setAttribute("mensaje", "Empleado eliminado correctamente.");
                break;
            case "Listar":
            case "default":
                break;
            default:
                throw new IllegalArgumentException("Accion de empleado no reconocida: " + accion);
        }

        request.setAttribute("empleados", empleadoDAO.listar());
        request.getRequestDispatcher("Empleado.jsp").forward(request, response);
    }

    private void procesarVenta(HttpServletRequest request, HttpServletResponse response, HttpSession session,
            Empleado usuario, String accion) throws ServletException, IOException {
        List<Venta> carrito = obtenerCarrito(session);

        switch (accion) {
            case "BuscarCliente":
                Cliente cliente = clienteDAO.buscar(request.getParameter("Codigocliente"));
                if (cliente.getId() == 0) {
                    request.setAttribute("error", "No se encontro cliente con ese DNI.");
                }
                session.setAttribute("clienteVenta", cliente);
                break;
            case "BuscarProducto":
                int idProducto = parseInt(request.getParameter("codigoproducto"), "Codigo de producto invalido.");
                Producto producto = productoDAO.listarId(idProducto);
                if (producto.getId() == 0) {
                    request.setAttribute("error", "No se encontro producto activo con ese codigo.");
                }
                request.setAttribute("producto", producto);
                break;
            case "Agregar":
                agregarProductoAlCarrito(request, carrito);
                session.setAttribute("carritoVenta", carrito);
                break;
            case "Limpiar":
                session.removeAttribute("carritoVenta");
                session.removeAttribute("clienteVenta");
                carrito = obtenerCarrito(session);
                request.setAttribute("mensaje", "Venta limpiada.");
                break;
            case "GenerarVenta":
                generarVenta(request, session, usuario, carrito);
                carrito = obtenerCarrito(session);
                break;
            case "default":
                break;
            default:
                throw new IllegalArgumentException("Accion de venta no reconocida: " + accion);
        }

        prepararVenta(request, session);
        request.getRequestDispatcher("Registrarventa.jsp").forward(request, response);
    }

    private void generarVenta(HttpServletRequest request, HttpSession session, Empleado usuario, List<Venta> carrito) {
        Cliente cliente = (Cliente) session.getAttribute("clienteVenta");
        if (cliente == null || cliente.getId() == 0) {
            throw new IllegalArgumentException("Seleccione un cliente antes de generar la venta.");
        }
        if (carrito.isEmpty()) {
            throw new IllegalArgumentException("Agregue al menos un producto antes de generar la venta.");
        }

        Venta venta = new Venta();
        venta.setIdcliente(cliente.getId());
        venta.setIdempleado(usuario.getId());
        venta.setNumserie("V-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        venta.setMonto(calcularTotal(carrito));
        venta.setEstado("Emitida");
        int idVenta = ventaDAO.guardarVenta(venta, carrito);

        session.removeAttribute("carritoVenta");
        session.removeAttribute("clienteVenta");
        request.setAttribute("mensaje", "Venta generada correctamente. ID: " + idVenta);
    }

    private void agregarProductoAlCarrito(HttpServletRequest request, List<Venta> carrito) {
        int idProducto = parseInt(request.getParameter("idProducto"), "Debe buscar y seleccionar un producto valido.");
        int cantidad = parseInt(request.getParameter("cant"), "Cantidad invalida.");
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        Producto producto = productoDAO.listarId(idProducto);
        if (producto.getId() == 0) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        if (cantidad > producto.getStock()) {
            throw new IllegalArgumentException("Stock insuficiente para " + producto.getNom() + ".");
        }

        Venta item = new Venta();
        item.setItem(carrito.size() + 1);
        item.setIdproducto(producto.getId());
        item.setDescripcionP(producto.getNom());
        item.setPrecio(producto.getPre());
        item.setCantidad(cantidad);
        item.setSubtotal(producto.getPre() * cantidad);
        carrito.add(item);
        request.setAttribute("mensaje", "Producto agregado.");
    }

    private void prepararVenta(HttpServletRequest request, HttpSession session) {
        List<Venta> carrito = obtenerCarrito(session);
        request.setAttribute("c", session.getAttribute("clienteVenta"));
        request.setAttribute("lista", carrito);
        request.setAttribute("totalpagar", calcularTotal(carrito));
    }

    @SuppressWarnings("unchecked")
    private List<Venta> obtenerCarrito(HttpSession session) {
        List<Venta> carrito = (List<Venta>) session.getAttribute("carritoVenta");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carritoVenta", carrito);
        }
        return carrito;
    }

    private double calcularTotal(List<Venta> carrito) {
        double total = 0.0;
        for (Venta item : carrito) {
            total += item.getSubtotal();
        }
        return total;
    }

    private Empleado leerEmpleado(HttpServletRequest request) {
        Empleado em = new Empleado();
        em.setDni(request.getParameter("txtDni"));
        em.setNom(request.getParameter("txtNombres"));
        em.setTel(request.getParameter("txtTel"));
        em.setEstado(valueOrDefault(request.getParameter("txtEstado"), "Activo"));
        em.setUser(request.getParameter("txtUser"));
        em.setRol(valueOrDefault(request.getParameter("txtRol"), "Vendedor"));
        return em;
    }

    private int parseInt(String value, String errorMessage) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

    private String valueOrDefault(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }

    private boolean esAdministrador(Empleado usuario) {
        return usuario != null && "Administrador".equalsIgnoreCase(usuario.getRol());
    }
}
