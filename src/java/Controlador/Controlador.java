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
    private static final String CSRF_TOKEN = "csrfToken";

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

        asegurarToken(session);
        String menu = valueOrDefault(request.getParameter("menu"), "Principal");
        String accion = valueOrDefault(request.getParameter("accion"), "default");

        try {
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                validarToken(request, session);
            }
            switch (menu) {
                case "Principal":
                    request.getRequestDispatcher("Principal.jsp").forward(request, response);
                    break;
                case "Empleado":
                    validarAdministrador(usuario);
                    procesarEmpleado(request, response, accion);
                    break;
                case "Cliente":
                    validarAdministrador(usuario);
                    procesarCliente(request, response, accion);
                    break;
                case "Producto":
                    validarAdministrador(usuario);
                    procesarProducto(request, response, accion);
                    break;
                case "NuevaVenta":
                    procesarVenta(request, response, session, usuario, accion);
                    break;
                case "Ventas":
                    procesarListadoVentas(request, response, usuario, accion);
                    break;
                default:
                    request.setAttribute("error", "Menu no reconocido: " + menu);
                    request.getRequestDispatcher("Principal.jsp").forward(request, response);
                    break;
            }
        } catch (DatabaseException | IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Error procesando menu " + menu + " accion " + accion, e);
            request.setAttribute("error", e.getMessage());
            prepararPantallaConError(request, response, session, usuario, menu);
        }
    }

    private void procesarEmpleado(HttpServletRequest request, HttpServletResponse response, String accion)
            throws ServletException, IOException {
        switch (accion) {
            case "Agregar":
                requerirPost(request);
                Empleado nuevo = leerEmpleado(request);
                String password = request.getParameter("txtPassword");
                if (!tieneTexto(password)) {
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
                requerirPost(request);
                Empleado actualizado = leerEmpleado(request);
                actualizado.setId(parseInt(request.getParameter("idEmpleado"), "ID de empleado invalido."));
                empleadoDAO.actualizar(actualizado, request.getParameter("txtPassword"));
                request.setAttribute("mensaje", "Empleado actualizado correctamente.");
                break;
            case "Delete":
                requerirPost(request);
                int idEliminar = parseInt(request.getParameter("id"), "ID de empleado invalido.");
                empleadoDAO.delete(idEliminar);
                request.setAttribute("mensaje", "Empleado desactivado correctamente.");
                break;
            case "Listar":
            case "default":
                break;
            default:
                throw new IllegalArgumentException("Accion de empleado no reconocida: " + accion);
        }

        prepararEmpleado(request);
        request.getRequestDispatcher("Empleado.jsp").forward(request, response);
    }

    private void procesarCliente(HttpServletRequest request, HttpServletResponse response, String accion)
            throws ServletException, IOException {
        switch (accion) {
            case "Agregar":
                requerirPost(request);
                clienteDAO.agregar(leerCliente(request));
                request.setAttribute("mensaje", "Cliente creado correctamente.");
                break;
            case "Editar":
                int idEditar = parseInt(request.getParameter("id"), "ID de cliente invalido.");
                request.setAttribute("cliente", clienteDAO.listarId(idEditar));
                break;
            case "Actualizar":
                requerirPost(request);
                Cliente actualizado = leerCliente(request);
                actualizado.setId(parseInt(request.getParameter("idCliente"), "ID de cliente invalido."));
                clienteDAO.actualizar(actualizado);
                request.setAttribute("mensaje", "Cliente actualizado correctamente.");
                break;
            case "Delete":
                requerirPost(request);
                int idEliminar = parseInt(request.getParameter("id"), "ID de cliente invalido.");
                clienteDAO.delete(idEliminar);
                request.setAttribute("mensaje", "Cliente desactivado correctamente.");
                break;
            case "Listar":
            case "default":
                break;
            default:
                throw new IllegalArgumentException("Accion de cliente no reconocida: " + accion);
        }

        prepararCliente(request);
        request.getRequestDispatcher("Cliente.jsp").forward(request, response);
    }

    private void procesarProducto(HttpServletRequest request, HttpServletResponse response, String accion)
            throws ServletException, IOException {
        switch (accion) {
            case "Agregar":
                requerirPost(request);
                productoDAO.agregar(leerProducto(request));
                request.setAttribute("mensaje", "Producto creado correctamente.");
                break;
            case "Editar":
                int idEditar = parseInt(request.getParameter("id"), "ID de producto invalido.");
                request.setAttribute("productoForm", productoDAO.listarId(idEditar));
                break;
            case "Actualizar":
                requerirPost(request);
                Producto actualizado = leerProducto(request);
                actualizado.setId(parseInt(request.getParameter("idProducto"), "ID de producto invalido."));
                productoDAO.actualizar(actualizado);
                request.setAttribute("mensaje", "Producto actualizado correctamente.");
                break;
            case "Delete":
                requerirPost(request);
                int idEliminar = parseInt(request.getParameter("id"), "ID de producto invalido.");
                productoDAO.delete(idEliminar);
                request.setAttribute("mensaje", "Producto desactivado correctamente.");
                break;
            case "Listar":
            case "default":
                break;
            default:
                throw new IllegalArgumentException("Accion de producto no reconocida: " + accion);
        }

        prepararProducto(request);
        request.getRequestDispatcher("Producto.jsp").forward(request, response);
    }

    private void procesarVenta(HttpServletRequest request, HttpServletResponse response, HttpSession session,
            Empleado usuario, String accion) throws ServletException, IOException {
        List<Venta> carrito = obtenerCarrito(session);

        switch (accion) {
            case "BuscarCliente":
                Cliente cliente = clienteDAO.buscar(request.getParameter("Codigocliente"));
                if (cliente.getId() == 0) {
                    request.setAttribute("error", "No se encontro cliente activo con ese DNI.");
                }
                session.setAttribute("clienteVenta", cliente);
                break;
            case "BuscarProducto":
                int idProducto = parseInt(request.getParameter("codigoproducto"), "Codigo de producto invalido.");
                Producto producto = productoDAO.buscarActivo(idProducto);
                if (producto.getId() == 0) {
                    request.setAttribute("error", "No se encontro producto activo con ese codigo.");
                }
                request.setAttribute("producto", producto);
                break;
            case "Agregar":
                requerirPost(request);
                agregarProductoAlCarrito(request, carrito);
                session.setAttribute("carritoVenta", carrito);
                break;
            case "Limpiar":
                requerirPost(request);
                session.removeAttribute("carritoVenta");
                session.removeAttribute("clienteVenta");
                carrito = obtenerCarrito(session);
                request.setAttribute("mensaje", "Venta limpiada.");
                break;
            case "GenerarVenta":
                requerirPost(request);
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

    private void procesarListadoVentas(HttpServletRequest request, HttpServletResponse response, Empleado usuario,
            String accion) throws ServletException, IOException {
        switch (accion) {
            case "Anular":
                validarAdministrador(usuario);
                requerirPost(request);
                ventaDAO.anularVenta(parseInt(request.getParameter("id"), "ID de venta invalido."));
                request.setAttribute("mensaje", "Venta anulada y stock restaurado.");
                break;
            case "Listar":
            case "default":
                break;
            default:
                throw new IllegalArgumentException("Accion de ventas no reconocida: " + accion);
        }

        prepararVentas(request);
        request.getRequestDispatcher("Ventas.jsp").forward(request, response);
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

        Producto producto = productoDAO.buscarActivo(idProducto);
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

    private void prepararPantallaConError(HttpServletRequest request, HttpServletResponse response, HttpSession session,
            Empleado usuario, String menu) throws ServletException, IOException {
        if ("Empleado".equals(menu) && esAdministrador(usuario)) {
            prepararEmpleado(request);
            request.getRequestDispatcher("Empleado.jsp").forward(request, response);
        } else if ("Cliente".equals(menu) && esAdministrador(usuario)) {
            prepararCliente(request);
            request.getRequestDispatcher("Cliente.jsp").forward(request, response);
        } else if ("Producto".equals(menu) && esAdministrador(usuario)) {
            prepararProducto(request);
            request.getRequestDispatcher("Producto.jsp").forward(request, response);
        } else if ("NuevaVenta".equals(menu)) {
            prepararVenta(request, session);
            request.getRequestDispatcher("Registrarventa.jsp").forward(request, response);
        } else if ("Ventas".equals(menu)) {
            prepararVentas(request);
            request.getRequestDispatcher("Ventas.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("Principal.jsp").forward(request, response);
        }
    }

    private void prepararEmpleado(HttpServletRequest request) {
        if (request.getAttribute("empleado") == null) {
            request.setAttribute("empleado", new Empleado());
        }
        request.setAttribute("empleados", empleadoDAO.listar());
    }

    private void prepararCliente(HttpServletRequest request) {
        if (request.getAttribute("cliente") == null) {
            request.setAttribute("cliente", new Cliente());
        }
        request.setAttribute("clientes", clienteDAO.listar());
    }

    private void prepararProducto(HttpServletRequest request) {
        if (request.getAttribute("productoForm") == null) {
            request.setAttribute("productoForm", new Producto());
        }
        request.setAttribute("productos", productoDAO.listar());
    }

    private void prepararVenta(HttpServletRequest request, HttpSession session) {
        List<Venta> carrito = obtenerCarrito(session);
        Object cliente = session.getAttribute("clienteVenta");
        request.setAttribute("c", cliente == null ? new Cliente() : cliente);
        if (request.getAttribute("producto") == null) {
            request.setAttribute("producto", new Producto());
        }
        request.setAttribute("lista", carrito);
        request.setAttribute("totalpagar", calcularTotal(carrito));
    }

    private void prepararVentas(HttpServletRequest request) {
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        List<Venta> ventas = ventaDAO.listar(fechaInicio, fechaFin);
        request.setAttribute("ventas", ventas);
        request.setAttribute("productosMasVendidos", ventaDAO.listarProductosMasVendidos(fechaInicio, fechaFin));
        request.setAttribute("fechaInicio", fechaInicio);
        request.setAttribute("fechaFin", fechaFin);
        request.setAttribute("totalVentas", ventas.size());
        request.setAttribute("montoTotal", calcularMontoVentas(ventas));
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

    private double calcularMontoVentas(List<Venta> ventas) {
        double total = 0.0;
        for (Venta venta : ventas) {
            if (!"Anulada".equalsIgnoreCase(venta.getEstado())) {
                total += venta.getMonto();
            }
        }
        return total;
    }

    private Empleado leerEmpleado(HttpServletRequest request) {
        Empleado em = new Empleado();
        em.setDni(requerirTexto(request.getParameter("txtDni"), "El DNI es obligatorio."));
        em.setNom(requerirTexto(request.getParameter("txtNombres"), "El nombre es obligatorio."));
        em.setTel(request.getParameter("txtTel"));
        em.setEstado(valueOrDefault(request.getParameter("txtEstado"), "Activo"));
        em.setUser(requerirTexto(request.getParameter("txtUser"), "El usuario es obligatorio."));
        em.setRol(valueOrDefault(request.getParameter("txtRol"), "Vendedor"));
        return em;
    }

    private Cliente leerCliente(HttpServletRequest request) {
        Cliente c = new Cliente();
        c.setDni(requerirTexto(request.getParameter("txtDni"), "El DNI es obligatorio."));
        c.setNom(requerirTexto(request.getParameter("txtNombres"), "El nombre del cliente es obligatorio."));
        c.setDir(request.getParameter("txtDireccion"));
        c.setEstado(valueOrDefault(request.getParameter("txtEstado"), "Activo"));
        return c;
    }

    private Producto leerProducto(HttpServletRequest request) {
        Producto p = new Producto();
        p.setNom(requerirTexto(request.getParameter("txtNombres"), "El nombre del producto es obligatorio."));
        p.setPre(parseDouble(request.getParameter("txtPrecio"), "Precio invalido."));
        p.setStock(parseInt(request.getParameter("txtStock"), "Stock invalido."));
        if (p.getPre() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (p.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        p.setEstado(valueOrDefault(request.getParameter("txtEstado"), "Activo"));
        return p;
    }

    private void asegurarToken(HttpSession session) {
        if (session.getAttribute(CSRF_TOKEN) == null) {
            session.setAttribute(CSRF_TOKEN, UUID.randomUUID().toString());
        }
    }

    private void validarToken(HttpServletRequest request, HttpSession session) {
        String esperado = (String) session.getAttribute(CSRF_TOKEN);
        String recibido = request.getParameter(CSRF_TOKEN);
        if (esperado == null || recibido == null || !esperado.equals(recibido)) {
            throw new IllegalArgumentException("La sesion del formulario expiro. Vuelva a intentar.");
        }
    }

    private void requerirPost(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            throw new IllegalArgumentException("Esta accion debe enviarse desde el formulario.");
        }
    }

    private void validarAdministrador(Empleado usuario) {
        if (!esAdministrador(usuario)) {
            throw new IllegalArgumentException("No tienes permiso para acceder a este modulo.");
        }
    }

    private int parseInt(String value, String errorMessage) {
        try {
            if (!tieneTexto(value)) {
                throw new NumberFormatException("empty");
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

    private double parseDouble(String value, String errorMessage) {
        try {
            if (!tieneTexto(value)) {
                throw new NumberFormatException("empty");
            }
            return Double.parseDouble(value.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

    private String requerirTexto(String value, String errorMessage) {
        if (!tieneTexto(value)) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value.trim();
    }

    private String valueOrDefault(String value, String defaultValue) {
        return tieneTexto(value) ? value.trim() : defaultValue;
    }

    private boolean tieneTexto(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean esAdministrador(Empleado usuario) {
        return usuario != null && "Administrador".equalsIgnoreCase(usuario.getRol());
    }
}
