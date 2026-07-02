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
}
