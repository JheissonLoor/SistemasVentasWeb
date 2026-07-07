package Modelo;

import config.Conexion;
import config.DatabaseException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public int guardarVenta(Venta venta, List<Venta> detalle) {
        String sqlVenta = "insert into ventas(IdCliente, IdEmpleado, NumeroSerie, FechaVentas, Monto, Estado) values(?,?,?,?,?,?)";
        String sqlDetalle = "insert into detalle_ventas(IdVentas, IdProducto, Cantidad, PrecioVenta) values(?,?,?,?)";
        ProductoDAO productoDAO = new ProductoDAO();

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);
            try {
                int idVenta;
                try (PreparedStatement ps = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, venta.getIdcliente());
                    ps.setInt(2, venta.getIdempleado());
                    ps.setString(3, venta.getNumserie());
                    ps.setDate(4, Date.valueOf(LocalDate.now()));
                    ps.setDouble(5, venta.getMonto());
                    ps.setString(6, venta.getEstado());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("No se obtuvo el ID de la venta generada.");
                        }
                        idVenta = keys.getInt(1);
                    }
                }

                try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                    for (Venta item : detalle) {
                        productoDAO.actualizarStock(con, item.getIdproducto(), item.getCantidad());
                        psDetalle.setInt(1, idVenta);
                        psDetalle.setInt(2, item.getIdproducto());
                        psDetalle.setInt(3, item.getCantidad());
                        psDetalle.setDouble(4, item.getPrecio());
                        psDetalle.addBatch();
                    }
                    psDetalle.executeBatch();
                }

                con.commit();
                return idVenta;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo generar la venta.", e);
        }
    }

    public List<Venta> listar(String fechaInicio, String fechaFin) {
        StringBuilder sql = new StringBuilder();
        sql.append("select v.IdVentas, v.NumeroSerie, v.FechaVentas, v.Monto, v.Estado, ");
        sql.append("c.Nombres as Cliente, e.Nombres as Empleado ");
        sql.append("from ventas v ");
        sql.append("inner join cliente c on c.IdCliente = v.IdCliente ");
        sql.append("inner join empleado e on e.IdEmpleado = v.IdEmpleado ");
        sql.append("where 1=1 ");
        if (tieneTexto(fechaInicio)) {
            sql.append("and v.FechaVentas >= ? ");
        }
        if (tieneTexto(fechaFin)) {
            sql.append("and v.FechaVentas <= ? ");
        }
        sql.append("order by v.IdVentas desc");

        List<Venta> lista = new ArrayList<>();
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int index = 1;
            if (tieneTexto(fechaInicio)) {
                ps.setDate(index++, Date.valueOf(fechaInicio));
            }
            if (tieneTexto(fechaFin)) {
                ps.setDate(index++, Date.valueOf(fechaFin));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venta v = new Venta();
                    v.setId(rs.getInt("IdVentas"));
                    v.setNumserie(rs.getString("NumeroSerie"));
                    v.setFecha(rs.getString("FechaVentas"));
                    v.setMonto(rs.getDouble("Monto"));
                    v.setEstado(rs.getString("Estado"));
                    v.setClienteNombre(rs.getString("Cliente"));
                    v.setEmpleadoNombre(rs.getString("Empleado"));
                    lista.add(v);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo listar ventas.", e);
        }
    }

    public List<Venta> listarProductosMasVendidos(String fechaInicio, String fechaFin) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.Nombres, sum(d.Cantidad) as Cantidad, sum(d.Cantidad * d.PrecioVenta) as Subtotal ");
        sql.append("from detalle_ventas d ");
        sql.append("inner join producto p on p.IdProducto = d.IdProducto ");
        sql.append("inner join ventas v on v.IdVentas = d.IdVentas ");
        sql.append("where v.Estado <> 'Anulada' ");
        if (tieneTexto(fechaInicio)) {
            sql.append("and v.FechaVentas >= ? ");
        }
        if (tieneTexto(fechaFin)) {
            sql.append("and v.FechaVentas <= ? ");
        }
        sql.append("group by p.IdProducto, p.Nombres order by Cantidad desc, Subtotal desc limit 5");

        List<Venta> lista = new ArrayList<>();
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int index = 1;
            if (tieneTexto(fechaInicio)) {
                ps.setDate(index++, Date.valueOf(fechaInicio));
            }
            if (tieneTexto(fechaFin)) {
                ps.setDate(index++, Date.valueOf(fechaFin));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venta v = new Venta();
                    v.setDescripcionP(rs.getString("Nombres"));
                    v.setCantidad(rs.getInt("Cantidad"));
                    v.setSubtotal(rs.getDouble("Subtotal"));
                    lista.add(v);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo generar reporte de productos.", e);
        }
    }

    public void anularVenta(int idVenta) {
        String sqlEstado = "select Estado from ventas where IdVentas=?";
        String sqlDetalle = "select IdProducto, Cantidad from detalle_ventas where IdVentas=?";
        String sqlStock = "update producto set Stock = Stock + ? where IdProducto=?";
        String sqlVenta = "update ventas set Estado='Anulada' where IdVentas=?";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(sqlEstado)) {
                    ps.setInt(1, idVenta);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Venta no encontrada.");
                        }
                        if ("Anulada".equalsIgnoreCase(rs.getString("Estado"))) {
                            throw new SQLException("La venta ya estaba anulada.");
                        }
                    }
                }

                try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
                     PreparedStatement psStock = con.prepareStatement(sqlStock)) {
                    psDetalle.setInt(1, idVenta);
                    try (ResultSet rs = psDetalle.executeQuery()) {
                        while (rs.next()) {
                            psStock.setInt(1, rs.getInt("Cantidad"));
                            psStock.setInt(2, rs.getInt("IdProducto"));
                            psStock.addBatch();
                        }
                    }
                    psStock.executeBatch();
                }

                try (PreparedStatement ps = con.prepareStatement(sqlVenta)) {
                    ps.setInt(1, idVenta);
                    ps.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo anular la venta.", e);
        }
    }

    private boolean tieneTexto(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
