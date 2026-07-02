package Modelo;

import config.Conexion;
import config.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public Producto buscar(int id) {
        return listarId(id);
    }

    public Producto listarId(int id) {
        String sql = "select IdProducto, Nombres, Precio, Stock, Estado from producto where IdProducto=? and Estado='Activo'";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
            return new Producto();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo buscar producto.", e);
        }
    }

    public List<Producto> listar() {
        String sql = "select IdProducto, Nombres, Precio, Stock, Estado from producto order by IdProducto";
        List<Producto> lista = new ArrayList<>();
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(map(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo listar productos.", e);
        }
    }

    public void actualizarStock(Connection con, int id, int cantidadVendida) throws SQLException {
        String sql = "update producto set Stock = Stock - ? where IdProducto=? and Stock >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidadVendida);
            ps.setInt(2, id);
            ps.setInt(3, cantidadVendida);
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("Stock insuficiente para el producto " + id);
            }
        }
    }

    public void agregar(Producto p) {
        String sql = "insert into producto(Nombres, Precio, Stock, Estado) values(?,?,?,?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPre());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getEstado());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo agregar producto.", e);
        }
    }

    public void actualizar(Producto p) {
        String sql = "update producto set Nombres=?, Precio=?, Stock=?, Estado=? where IdProducto=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPre());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getEstado());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo actualizar producto.", e);
        }
    }

    public void delete(int id) {
        String sql = "delete from producto where IdProducto=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo eliminar producto.", e);
        }
    }

    private Producto map(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("IdProducto"));
        p.setNom(rs.getString("Nombres"));
        p.setPre(rs.getDouble("Precio"));
        p.setStock(rs.getInt("Stock"));
        p.setEstado(rs.getString("Estado"));
        return p;
    }
}
